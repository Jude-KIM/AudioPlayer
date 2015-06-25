package com.airplug.audioplug.player;

import java.io.Serializable;
import java.util.ArrayList;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.airplug.audioplug.channellist.Channel;
import com.airplug.audioplug.data.ConstData;
import com.airplug.audioplug.dev.R;
import com.airplug.audioplug.util.DisplayUtil;
import com.airplug.audioplug.util.ImageCache;
import com.airplug.audioplug.util.ObjectPreference;
import com.airplug.audioplug.widget.FourLongWidget;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class AudioService extends Service implements IAudioService, MusicFocusable{

	private static final String NAME = "AudioService";
	private final String CLASS = NAME + "@" + Integer.toHexString(hashCode());
	
	private static final Object[] sWait = new Object[0];
 	private static AudioService sInstance;
	public static boolean isRunning;		
	
	private static final int NOTIFICATION_ID = 1;
	AudioFocusHelper mAudioFocusHelper = null;
    
	public static final String ACTION_PLAY = "com.airplug.audioplug.dev.action.PLAY";
	public static final String ACTION_PAUSE = "com.airplug.audioplug.dev.action.PAUSE";
	public static final String ACTION_TOGGLE = "com.airplug.audioplug.dev.action.TOGGLE_PLAYBACK";
	public static final String ACTION_NEXT = "com.airplug.audioplug.dev.action.NEXT";
	public static final String ACTION_PREVIOUS = "com.airplug.audioplug.dev.action.PREVIOUS";
	public static final String ACTION_STOP = "com.airplug.audioplug.dev.action.STOP";
	public static final String ACTION_REMOVE = "com.airplug.audioplug.dev.action.REMOVE";
	
	

	public static final String SERVICECMD = "com.android.music.musicservicecommand";
	public static final String CMDNAME = "command";
	public static final String CMDTOGGLEPAUSE = "togglepause";
	public static final String CMDSTOP = "stop";
	public static final String CMDPAUSE = "pause";
	public static final String CMDPLAY = "play";
    public static final String CMDPREVIOUS = "previous";
    public static final String CMDNEXT = "next";
    public static final String TOGGLEPAUSE_ACTION = "com.android.music.musicservicecommand.togglepause";
    public static final String PAUSE_ACTION = "com.android.music.musicservicecommand.pause";
    public static final String PREVIOUS_ACTION = "com.android.music.musicservicecommand.previous";
	public static final String NEXT_ACTION = "com.android.music.musicservicecommand.next";
		
	
    // do we have audio focus?
    enum AudioFocus {
        NoFocusNoDuck,    // we don't have audio focus, and can't duck
        NoFocusCanDuck,   // we don't have focus, but can play at a low volume ("ducking")
        Focused           // we have full audio focus
    }
    
    AudioFocus mAudioFocus = AudioFocus.NoFocusNoDuck;
	
	public static interface IServiceListener {
		public void onAudioEntryInfo(AudioFile entry);

		String getName();
	}
	
	public class AudioServiceBinder extends Binder {
		public IAudioService getAudioService() {
			return AudioService.this;
		}
	}
	
	private final AudioServiceBinder binder = new AudioServiceBinder();	
	private AudioPlayer audioPlayer;
	private int netType = -1;	
	private Channel channel;
	private int index;
	private int state = ConstData.FLAG_NO_MEDIA;
	private static Context mContext;	
	private HandlerThread handlerThread;
	private Handler handler;

	public static String getAction(Context context) {
		mContext = context;
		return context.getPackageName() + ".intent.action.AUDIO_SERVICE";
	
	}
	
	public Handler workHandler() {
		String name = "WORK";
		handlerThread = new HandlerThread(name);
		name = name + "-" + handlerThread.getId();
		handlerThread.setName(name);
		handlerThread.start();
		handler = new Handler(handlerThread.getLooper(), null);
		return handler;		
	}
	
	private void initWidgets()
	{
		AppWidgetManager manager = AppWidgetManager.getInstance(this);
//		OneCellWidget.checkEnabled(this, manager);
//		FourSquareWidget.checkEnabled(this, manager);
		FourLongWidget.checkEnabled(this, manager);
		//FourLongWidgetLock.checkEnabled(this, manager);

//		WidgetD.checkEnabled(this, manager);
//		WidgetE.checkEnabled(this, manager);
	}

	@Override
	public int onStartCommand(final Intent intent, int flags, int startId) {
		Log.d(CLASS, "jude, onStartCommand: " + intent + " " + flags + " " + startId);		
		try{		
			if(intent != null){
				String action = intent.getAction();				
				if(action.equals(getAction(getApplicationContext()))) {				
					openPlayer(intent);
					//play();
				}
	//			else if(ACTION_PLAY.equals(action)){		
	//				if(audioPlayer == null){
	//					openPlayer(intent);
	//				}
	//				play();
	//			}
	//			else if(ACTION_PAUSE.equals(action)){
	//				pause();
	//			}
				else if(ACTION_TOGGLE.equals(action)){
					boolean temp = isPlaying();
					//getNotification();	
					if(temp){
						pause();
					}
					else{
						if(audioPlayer == null){
							openPlayer(intent);
						}
						play();
					}							
				}
				else if(ACTION_PREVIOUS.equals(action)){
					playPrevious();
				}
				else if(ACTION_NEXT.equals(action)){
					playNext();
				}
				else if(ACTION_STOP.equals(action)){
					stop();												
				}
				else if(ACTION_REMOVE.equals(action)){
					hideNotification();					
				}			

			}
		}catch(Throwable e){
			e.printStackTrace();			
		}
		
		isRunning = true;
		//return super.onStartCommand(intent, flags, startId);
		return START_NOT_STICKY;
	}
	
	
	@SuppressWarnings("unchecked")
	private void openPlayer(Intent intent) {
		if (intent != null) {
			playlist = (ArrayList<AudioFile>) intent.getSerializableExtra("entries");		
			if(playlist == null){
				Object obj = ObjectPreference.readObjectFromFile(this, "playlist");
				if(obj != null){
					playlist = (ArrayList<AudioFile>) obj;
				}else{
					state  = ConstData.FLAG_NO_MEDIA;
				}
			}
			channel = (Channel) intent.getSerializableExtra("channel");
			index = intent.getIntExtra("index",  -1);
			if(index == -1){
				SharedPreferences prefs = getSharedPreferences(
						ConstData.CurrentPlayPrefs, MODE_PRIVATE);
				index = prefs.getInt("index", -1);
			}
		}
		Log.d(CLASS, "jude, list: " + playlist);
		
		if (AudioList.isMMS(intent.getStringExtra("protocol"))) {
			audioPlayer = new AudioPlayerFFmpeg(this, playlist);
		} else {
			audioPlayer = new AudioPlayerStagefright(this, playlist);
		}
		audioPlayer.open(index);
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.d(CLASS, "jude, onBind: " + intent);
		return binder;
	}

	@Override
	public void setServiceListener(final IServiceListener listener) {
		workHandler().post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(audioPlayer != null)
					audioPlayer.setServiceListener(listener);
			}
		});
	}

	@Override
	public AudioFile getAudioEntry() {
		return audioPlayer != null? audioPlayer.getAudioEntry(): null;
	}

	@Override
	public boolean isPlaying() {		
		return audioPlayer != null? audioPlayer.isPlaying(): false;
	}
	
	@Override
	public int getDuration() {
		return audioPlayer != null? audioPlayer.getDuration(): 0;
	}

	@Override
	public int getBufferPercent() {
		return audioPlayer != null? audioPlayer.getBufferPercent(): 0;
	}

	@Override
	public int getCurrentPosition() {
		return audioPlayer != null? audioPlayer.getCurrentPosition(): 0;
	}
	
	@Override
	public Serializable getEntries() {
		return audioPlayer != null? audioPlayer.getEntries(): 0;
	}
	
	@Override
	public int getIndex() {
		return audioPlayer != null? audioPlayer.getIndex(): 0;
	}
	
	@Override
	public void play() {
		final Runnable runnable = new Runnable() {			

			@Override
			public void run() {
				// TODO Auto-generated method stub
				audioPlayer.play();
			}
		}; 
		
		tryToGetAudioFocus();
		workHandler().removeCallbacks(runnable);
		workHandler().post(runnable);
	}

	@Override
	public void pause() {
		final Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				audioPlayer.pause();	
			}
		}; 
		giveUpAudioFocus();
		stopForeground(true);
		workHandler().removeCallbacks(runnable);
		workHandler().post(runnable);
	}
	
	/* jude_test
	@Override
	public void stop() {
		hideNotification();
		
		getWorkHandler().post(new Runnable(NAME + ":onDestroy") {
			@Override
			protected void runs() {
				if(audioPlayer != null)
					audioPlayer.close();				
				if(agent != null) 
					agent.destroy();
				destroy("");
			}			
		});
				
	}
	*/
	
	
	@Override
	public void stop() {
		final Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				audioPlayer.stop();
			}
		}; 
		workHandler().removeCallbacks(runnable);
		workHandler().post(runnable);
	}
	

	@Override
	public void seekTo(final int msec) {
		Log.d("position","seekTo : " + msec);
		final Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				audioPlayer.seekTo(msec);				
			}
		}; 
		workHandler().removeCallbacks(runnable);
		workHandler().post(runnable);
	}

	@Override
	public void playNext() {
		final Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(audioPlayer != null)
					audioPlayer.playNext();
				
			}
		}; 
		workHandler().removeCallbacks(runnable);
		workHandler().post(runnable);
	}
	
	@Override
	public void playPrevious() {
		// TODO Auto-generated method stub
		final Runnable runnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(audioPlayer != null)
					audioPlayer.playPrevious();
				
			}
		}; 
		workHandler().removeCallbacks(runnable);
		workHandler().post(runnable);

		
	}

	@Override
	public void onCreate() {		
		super.onCreate();
				
		Log.d(CLASS, "jude, onCreate");		
				
		initWidgets();
		
		getNotification();
					
		IntentFilter commandFilter = new IntentFilter();
        commandFilter.addAction(SERVICECMD);
        commandFilter.addAction(TOGGLEPAUSE_ACTION);
        commandFilter.addAction(PAUSE_ACTION);
        commandFilter.addAction(NEXT_ACTION);
        commandFilter.addAction(PREVIOUS_ACTION);
		registerReceiver(mIntentReceiver,commandFilter);
								
				
		if (android.os.Build.VERSION.SDK_INT >= 8)
            mAudioFocusHelper = new AudioFocusHelper(getApplicationContext(), this);
        else
            mAudioFocus = AudioFocus.Focused; // no focus feature, so we always "have" audio focus	
		
		sInstance = this;
		synchronized (sWait) {
			sWait.notifyAll();
		}
	}

	@Override
	public void onDestroy() {				
		super.onDestroy();		
		Log.d(CLASS, "jude, onDestroy");
		hideNotification();
		Log.d("jude","AudioService : onDestroy ");				
		workHandler().post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(audioPlayer != null)
					audioPlayer.close();				
								
			}
		});
		
		unregisterReceiver(mIntentReceiver);
	}

	private ArrayList<AudioFile> playlist;
	
	public void playSound(int soundID) {		
		SoundPool soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
		soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
			
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
				soundPool.play(sampleId, 1, 1, 1, 0, 1f);
			}
		});
		
		soundPool.load(getApplicationContext(), soundID, 1);
	}
	
	public void startForeground() {		
		Log.i("jude","startForeground()");
		startForeground(NOTIFICATION_ID, null);
	}
		
	public void showNotification() {		
		Log.i("jude","showNotification()");
		NotificationManager manager =
				(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(NOTIFICATION_ID, getNotification());
	}
	
	public void hideNotification() {	
		Log.i("jude","hideNotification()");
		NotificationManager manager =
				(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		manager.cancelAll();
	}
			
//	@SuppressWarnings("deprecation")
//	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	
	private Notification getNotification(){
		Log.i("jude","getNotification()");		
		
		if(audioPlayer == null){			
			Log.e("jude","audioplayer is null");
			return null ;
		}
		
		AudioFile entry = audioPlayer.getAudioEntry();	
		if(entry == null){			
			Log.e("jude","entry is null");
			return null;
		}
		
		final RemoteViews rv = new RemoteViews(getPackageName(), R.layout.layout_notification);		
		
		ImageCache.getInstance(this).getImageLoader().loadImage(entry.thumbnail, null, ImageCache.getInstance(this).getOption(), 
        		new SimpleImageLoadingListener(){
        		@Override
        		public void onLoadingComplete(String imageUri,
	                    View view, Bitmap loadedImage) {        			
	                // TODO Auto-generated method stub
	                super.onLoadingComplete(imageUri, view,
	                        loadedImage);	          
	                Log.d("jude","bitmap : " + loadedImage);
	                rv.setImageViewBitmap(R.id.notImage, loadedImage);
	                //rv.setImageViewResource(R.id.notImage, R.drawable.test_thumbnail);
	            }
        	});        
		
		String fTitle = entry.title;		
		String cTitle = entry.channelTitle;		
		
		//Alignment of not-buttons		
//		rv.setInt(R.id.notButtonPrevious, "foregroundGravity", Gravity.CENTER);
//		rv.setInt(R.id.notButtonNext, "setGravity", Gravity.CENTER);
				//addView(viewId, nestedView)findViewById(R.id.playerThumbnail); 
				        
		//if(audioPlayer.isPlaying()){        	
		if(getState() == 1){
        	rv.setImageViewResource(R.id.notButtonToggle, android.R.drawable.ic_media_pause);        	
        }
        else{        	
        	rv.setImageViewResource(R.id.notButtonToggle, android.R.drawable.ic_media_play);
        }
        
        rv.setTextViewText(R.id.notChannelTitle, cTitle);
        //contentView.setTextViewSelected(R.id.notTitle,true);
        rv.setTextViewText(R.id.notFeedTitle, fTitle);
        
        
        Intent intent;
		PendingIntent buttonIntent;
		Context context = getApplicationContext();
		ComponentName service = new ComponentName(context, AudioService.class);

		intent = new Intent(ACTION_TOGGLE).setComponent(service);
		buttonIntent = PendingIntent.getService(context, 0, intent, 0);
		rv.setOnClickPendingIntent(R.id.notButtonToggle, buttonIntent);

		intent = new Intent(ACTION_NEXT).setComponent(service);
		buttonIntent = PendingIntent.getService(context, 0, intent, 0);
		rv.setOnClickPendingIntent(R.id.notButtonNext, buttonIntent);

		intent = new Intent(ACTION_PREVIOUS).setComponent(service);
		buttonIntent = PendingIntent.getService(context, 0, intent, 0);
		rv.setOnClickPendingIntent(R.id.notButtonPrevious, buttonIntent);
		
		intent = new Intent(ACTION_REMOVE).setComponent(service);
		buttonIntent = PendingIntent.getService(context, 0, intent, 0);
		rv.setOnClickPendingIntent(R.id.notButtonRemove, buttonIntent);
  
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, PlayerActivity.class)
				.putExtra("entries", audioPlayer.getEntries())
				.putExtra("channel", channel)
				.putExtra("index", audioPlayer.getIndex())
				.putExtra("playing", true),				
				PendingIntent.FLAG_UPDATE_CURRENT);		
				
		Notification.Builder builder = new Notification.Builder(getApplicationContext());
//		Builder builder = new NotificationCompat.Builder(getApplicationContext());
		
        builder
                .setContentTitle(cTitle)
                .setContentText(fTitle)
                .setSmallIcon(R.drawable.ic_launcher_web)
                .setLargeIcon(DisplayUtil.decodeSampledBitmapFromFile(entry.thumbnail, 120, 120))
                .setOngoing(true)
                .setWhen(0)
                .setTicker(fTitle)
                .setContent(rv)
                .setContentIntent(pendingIntent); //this intent override my contentView.setOnClickPendintIntent. I can't click the view. 
        
		Notification notification = builder.getNotification();		
		//notification.contentView = rv;
		NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(1, notification);
		
		return notification;  
	}
	
	
	@Override
	public void setContents( ArrayList<AudioFile> playlist, boolean notList, Channel channel ) {
		this.playlist = playlist ;		
		this.channel = channel;		
	}
		
	private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
	       @Override
     public void onReceive(Context context, Intent intent) {
	    	   	    	   
	    	   String action = intent.getAction();
	           String cmd = intent.getStringExtra("command");
	           Log.i("jude","receive!! : " + action + " " + cmd);
	           if (CMDNEXT.equals(cmd) || NEXT_ACTION.equals(action)) {
	                playNext();
	           } else if (CMDPREVIOUS.equals(cmd) || PREVIOUS_ACTION.equals(action)) {
	               //prev();
	           } else if (CMDTOGGLEPAUSE.equals(cmd) || TOGGLEPAUSE_ACTION.equals(action)) {
	        	   if (isPlaying()) {
                pause();                   
	               } 
	        	   else {
	                   play();
	               }

	           } else if (CMDPAUSE.equals(cmd) || PAUSE_ACTION.equals(action)) {
	        	   pause();		            
	        	   
		       } else if (CMDPLAY.equals(cmd)) {
		             play();
		             
		       } else if (CMDSTOP.equals(cmd)) {
		    	   pause();	               
		    	   seekTo(0);
		       }
//		       } else if (MediaAppWidgetProvider.CMDAPPWIDGETUPDATE.equals(cmd)) {
//             // Someone asked us to refresh a set of specific widgets, probably
//		               // because they were just added.
//		    	   int[] appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
//		    	   mAppWidgetProvider.performUpdate(MediaPlaybackService.this, appWidgetIds);
//		       }
	       }
		};	
	
	
//
//	public static AudioService get(Context context)
//	{
//		if (sInstance == null) {
//			context.startService(new Intent(context, AudioService.class));
//
//			while (sInstance == null) {
//				try {
//					synchronized (sWait) {
//						sWait.wait();
//					}
//				} catch (InterruptedException ignored) {
//				}
//			}
//		}
//
//		return sInstance;
//	}
//	
//	public static boolean hasInstance() {
//		return sInstance != null;
//	}	
//	

	@Override
	public int getState() {
		return state;
	}
	
	@Override
	public Channel getChannel() {
		return channel;
	}
	
	public void setState(int state){
		Log.d("updateWidget:(setState)", "setState:"+state);

		this.state = state;
	}

	
	/**
	 * Return the PlaybackService instance, creating one if needed.
	 */
//	public static AudioService get(Context context)
//	{
//		if (sInstance == null) {
//			context.startService(new Intent(context, AudioService.class));
//
//			while (sInstance == null) {
//				try {
//					synchronized (sWait) {
//						sWait.wait();
//					}
//				} catch (InterruptedException ignored) {
//				}
//			}
//		}
//
//		return sInstance;
//	}
//
//	/**
//	 * Returns true if a PlaybackService instance is active.
//	 */
//	public static boolean hasInstance()
//	{
//		return sInstance != null;
//	}

    void tryToGetAudioFocus() {
        if (mAudioFocus != AudioFocus.Focused && mAudioFocusHelper != null
                        && mAudioFocusHelper.requestFocus())
            mAudioFocus = AudioFocus.Focused;
    }

	@Override
	public void onGainedAudioFocus() {
		// TODO Auto-generated method stub
		  Toast.makeText(getApplicationContext(), "gained audio focus.", Toast.LENGTH_SHORT).show();
	        mAudioFocus = AudioFocus.Focused;
	        // restart media player with new focus settings
	        if (isPlaying())
	            configAndStartMediaPlayer();
		
	}

	@Override
	public void onLostAudioFocus(boolean canDuck) {
		// TODO Auto-generated method stub
		 Toast.makeText(getApplicationContext(), "lost audio focus." + (canDuck ? "can duck" :
		            "no duck"), Toast.LENGTH_SHORT).show();
		        mAudioFocus = canDuck ? AudioFocus.NoFocusCanDuck : AudioFocus.NoFocusNoDuck;

		        // start/restart/pause media player with new focus settings
		        if (isPlaying())
		            configAndStartMediaPlayer();		
	}
	
    void giveUpAudioFocus() {
        if (mAudioFocus == AudioFocus.Focused && mAudioFocusHelper != null
                                && mAudioFocusHelper.abandonFocus())
            mAudioFocus = AudioFocus.NoFocusNoDuck;
    }
    
    void configAndStartMediaPlayer() {
        if (mAudioFocus == AudioFocus.NoFocusNoDuck) {
            // If we don't have audio focus and can't duck, we have to pause, even if mState
            // is State.Playing. But we stay in the Playing state so that we know we have to resume
            // playback once we get the focus back.
            if (isPlaying()) pause();
            return;
        }
        //else if (mAudioFocus == AudioFocus.NoFocusCanDuck)
            //mPlayer.setVolume(DUCK_VOLUME, DUCK_VOLUME);  // we'll be relatively quiet
        //else
            //mPlayer.setVolume(1.0f, 1.0f); // we can be loud

        if (!isPlaying()) play();
    }
	
	
	public class AudioFocusHelper implements AudioManager.OnAudioFocusChangeListener {
	    AudioManager mAM;
	    MusicFocusable mFocusable;

	    public AudioFocusHelper(Context ctx, MusicFocusable focusable) {
	        mAM = (AudioManager) ctx.getSystemService(Context.AUDIO_SERVICE);
	        mFocusable = focusable;
	    }

	    /** Requests audio focus. Returns whether request was successful or not. */
	    public boolean requestFocus() {
	    	Log.e("test","requestfocus");
	        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
	            mAM.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
	    }

	    /** Abandons audio focus. Returns whether request was successful or not. */
	    public boolean abandonFocus() {
	    	Log.e("test","abandonfocus");
	        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == mAM.abandonAudioFocus(this);
	    }

	    /** 
	     * Called by AudioManager on audio focus changes. We implement this by calling our
	     * MusicFocusable appropriately to relay the message.
	     */
	    public void onAudioFocusChange(int focusChange) {
	    	Log.e("test","onAudioFocusChange!!! : " + focusChange);
	        if (mFocusable == null) return;
	        switch (focusChange) {
	            case AudioManager.AUDIOFOCUS_GAIN:
	                mFocusable.onGainedAudioFocus();
	                break;
	            case AudioManager.AUDIOFOCUS_LOSS:
	            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
	                mFocusable.onLostAudioFocus(false);
	                break;
	            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
	                mFocusable.onLostAudioFocus(true);
	                break;
	             default:
	        }
	    }
	}	
}
