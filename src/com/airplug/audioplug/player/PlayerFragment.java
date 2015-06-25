package com.airplug.audioplug.player;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.airplug.audioplug.MainActivity;
import com.airplug.audioplug.Preferences;
import com.airplug.audioplug.channellist.Channel;
import com.airplug.audioplug.dev.R;
import com.airplug.audioplug.share.ShareActivity;
import com.airplug.audioplug.util.DisplayUtil;
import com.airplug.audioplug.util.ImageCache;
import com.airplug.audioplug.util.Util;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class PlayerFragment extends Fragment implements OnClickListener, OnSeekBarChangeListener{

	private static final String NAME = "PlayerFragment";
	private static final String CLASS = "PlayerFragment";
	private PlayInfo pInfo;
	private ImageView imageView;	
	private ImageButton buttonBack;	
	private ImageButton buttonPlaylist;
	private ImageButton btnPlayerShare;
	private ImageView btnPlayerBookmark;
	private TextView channelName;
	private Context context;
	private Activity activity;
	
	private SeekBar seekBar;
	private ImageButton buttonPlay;
	private TextView playDurationTextView;
	private TextView playTimeTextView;
	private TextView feedTitle;
	private DisplayHandler h;
	private Handler mainHandler;
	ProgressDialog loadingScreen;
	private ImageView playerThumbBack;
	
	private LinearLayout _container;
	private WindowManager wm;
	
	int button01pos = 0;
	
	private IAudioService service;
	private ServiceManager srvManager;	
	private AudioFile _playFile;	
	
	//- brown
//	public PlayerFragment(Context context) {
//		this.context = context;
//		Bundle args = new Bundle();
//		this.setArguments(args);
//	}
	
	public static final PlayerFragment newInstance(Context context)
	{
		PlayerFragment fragment = new PlayerFragment();
		fragment.context = context;
	    return fragment ;
	}
	
	public PlayerFragment(){
		//Log.e("jude","emptry ChannelList constructor");
	}
	
    @Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		Log.d(CLASS,"jude " + "getview() : " + getView());
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(CLASS,"jude " + "onCreate");        
        activity = getActivity();        
				
	}
	
	 @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		 
		Log.d(CLASS,"jude " + "onCreateView");
		
		srvManager =new ServiceManager(activity);
		View view = inflater.inflate(R.layout.fragment_player, container, false);
				
		wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		
		imageView = (ImageView) view.findViewById(R.id.playerThumbnail);
		playerThumbBack = (ImageView) view.findViewById(R.id.playerThumbnailBack);
		
		channelName = (TextView) view.findViewById(R.id.channelTitle);
		btnPlayerBookmark = (ImageView) view.findViewById(R.id.btnPlayerBookmark);
		btnPlayerBookmark.setOnClickListener(this);
		buttonBack = (ImageButton) view.findViewById(R.id.btnBack);
		buttonBack.setOnClickListener(this);
		buttonPlaylist = (ImageButton) view.findViewById(R.id.btnPlayList);
		buttonPlaylist.setOnClickListener(this);
		btnPlayerShare = (ImageButton) view.findViewById(R.id.btnPlayerShare);
		btnPlayerShare.setOnClickListener(this);
		
		h = new DisplayHandler();
		mainHandler = new Handler();
		
//		li = (LayoutInflater) activity.getSystemService(infService);
//		li.inflate(R.layout.contoller_layout,  this, true);
		
		buttonPlay = (ImageButton) view.findViewById(R.id.buttonPlay);
		buttonPlay.setOnClickListener(this);
		buttonPlay.setImageResource(R.drawable.ic_playpage_pause);		
		ImageButton buttonFF = (ImageButton) view.findViewById(R.id.buttonForward);
		buttonFF.setOnClickListener(this);
		ImageButton buttonRW = (ImageButton) view.findViewById(R.id.buttonRew);
		buttonRW.setOnClickListener(this);		
		seekBar = (SeekBar) view.findViewById(R.id.seekBar);
		seekBar.setOnSeekBarChangeListener((OnSeekBarChangeListener) this);		
		playTimeTextView = (TextView) view.findViewById(R.id.textViewPlayTime);
		playDurationTextView = (TextView) view.findViewById(R.id.textViewPlayDuration);
		feedTitle = (TextView) view.findViewById(R.id.feedTitle);

		
		try{			
			pInfo = new PlayInfo();
			pInfo.playlist = (ArrayList<AudioFile>) activity.getIntent().getSerializableExtra("entries");			
			pInfo.channel = (Channel) activity.getIntent().getSerializableExtra("channel");		 
			pInfo.index = activity.getIntent().getIntExtra("index", -1);
			
			Log.i("jude", "index : " + pInfo.index);
			Log.i("jude", "channel : " + pInfo.channel);
			Log.i("jude", "channeltitle : " + pInfo.channel.getTitle());
			if(pInfo != null){
				if(Util.is3GConnected(activity) && Preferences.isDataEnabled(activity) ==false)
					show3G_Dialog();
				else{				
				//if(Preferences.isDataEnabled(this)){			
					showLoadingScreen("onCreate");
					setPlayInfo(pInfo);
					channelName.setText(pInfo.channel.getTitle());				
					channelName.setGravity(Gravity.CENTER);
				}					
			}
		}catch(Throwable e){
			e.printStackTrace();			
		}
				
				
		//jude_test
		//AudioFile entry = pInfo.playlist.get(0);
		Log.d("jude","pInfo.playlist.get(position) : " + pInfo.playlist.get(pInfo.index));
		AudioFile entry = pInfo.playlist.get(pInfo.index);
		if(entry != null){
			ImageCache.getInstance(activity).getImageLoader()
			.displayImage(entry.thumbnail, imageView, ImageCache.getInstance(activity).getOption(), imageLoadingListener);
		}
		
	 
		return view;
	 }
	 
	 private ImageLoadingListener imageLoadingListener = new ImageLoadingListener() {
			
		@Override
		public void onLoadingStarted(String imageUri, View view) {}
		
		@Override
		public void onLoadingFailed(String imageUri, View view, FailReason failReason) {}
		
		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			
			Bitmap blured = DisplayUtil.applyGaussianBlur(loadedImage, 50);
			
			if(blured != null) {
				playerThumbBack.setImageBitmap(blured);
			}
		}
		
		@Override
		public void onLoadingCancelled(String imageUri, View view) {}
	};
	

	 @Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent;
			try{
				service = srvManager.getService();
				switch(v.getId()){			
				case R.id.btnPlayerBookmark:								
			        if (button01pos == 0) {
			        	btnPlayerBookmark.setImageResource(R.drawable.btn_player_bookmark_pressed);
			            button01pos = 1;
			        } else if (button01pos == 1) {
			        	btnPlayerBookmark.setImageResource(R.drawable.btn_player_bookmark);
			            button01pos = 0;
			        }
			        break;	
				
				case R.id.btnBack:
					intent = new Intent(context, MainActivity.class);			
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.setAction(Intent.ACTION_MAIN);
					startActivity(intent);
					((Activity) context).finish();
					break;
				
				case R.id.btnPlayList:
					//Call play list				
					if (service!=null)					
						((PlayerActivity) activity).setCurrentFragment(1);
					break;
					
				case R.id.btnPlayerShare:
					intent = new Intent(context, ShareActivity.class);
					intent.putExtra("channel", pInfo.channel);
					intent.putExtra("content", feedTitle.getText());
					startActivity(intent);
					break;
					
				case R.id.buttonPlay:
					if (service == null) {
						//	service.startAudioService();
						} else if (service.isPlaying()) {
							service.pause();
							buttonPlay.setImageResource(R.drawable.ic_playpage_play);
						} else {
							service.play();
							buttonPlay.setImageResource(R.drawable.ic_playpage_pause);
						}
					break;
					
				case R.id.buttonForward:
					if (service != null	&& 0 < service.getDuration()) {
		//				int position = service.getCurrentPosition();
		//				service.seekTo(position + 5000);
						if(Util.is3GConnected(activity) && Preferences.isDataEnabled(activity) ==false){
							show3G_Dialog(1);
						}
						else
						{
							showLoadingScreen("buttonForward");
							service.playNext();
						}
					}
					break;
					
				case R.id.buttonRew:
					if (service != null && 0 < service.getDuration()) {
		//				int position = service.getCurrentPosition();
		//				service.seekTo(position - 5000);
						if(Util.is3GConnected(activity) && Preferences.isDataEnabled(activity) ==false){
							show3G_Dialog(-1);
						}
						else
						{
							showLoadingScreen("buttonRew");
							service.playPrevious();
						}					
					}
					break;		
				
			default:
			}
		}catch(Throwable e){
			e.printStackTrace();
		}
			
	 }

	public void show3G_Dialog(final int mode) {
			// TODO Auto-generated method stub
			if((Preferences.isDataEnabled(activity) == false) && Util.is3GConnected(activity))
			{
				AlertDialog.Builder alert = new AlertDialog.Builder(activity);
				alert.setTitle(activity.getResources().getString(R.string.app_name));
				alert.setMessage(R.string.dialog_allow_data);
				//alert.setIcon(R.drawable.ico_mdpi_videoplug);
				//alert.setOnKeyListener(backKeyListener);
				alert.setCancelable(false);
				alert.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Preferences.setDataEnabled(activity, true);
						if(mode>0){
							//play next
							showLoadingScreen("buttonForward");
							service.playNext();
						}
						else if(mode<0){
							//play previous
							showLoadingScreen("buttonRew");
							service.playPrevious();
						}
					}
				});
				
				alert.setNegativeButton("No",
						new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) { 
						//do nothing();
					}
				});
				alert.show();
			}
	}
	 
	public void updateProgress(int position, int duration, int buffer) {
		playDurationTextView.setText(getTimeDiffFormat(position));
		if(0 < duration) {
			playDurationTextView.setText(getTimeDiffFormat(duration));
			playTimeTextView.setText(getTimeDiffFormat(position));			
			seekBar.setMax(duration);
			seekBar.setProgress(position);
			seekBar.setSecondaryProgress(duration * buffer / 100);
		}		
	}
	 
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {		
		try {
			service = srvManager.getService();					
			if (fromUser && service != null) {
					if(!isLoadingScreen())
						showLoadingScreen("onProgressChanged");
					int duration = service.getDuration();
					if (0 < duration) {
						service.seekTo(progress);
						Log.d(CLASS, "seekTo: " + progress + " of "
								+ duration);
					}
				} 
			}
		catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	public void setPlayInfo(PlayInfo pInfo ){
		Log.e(CLASS,"jude" + " setPlayInfo");		
		this.pInfo = pInfo;
		this._playFile = pInfo.playlist.get(pInfo.index);

		srvManager.start(pInfo);
		srvManager.sendCommand(pInfo);
		updatePlayInfo(_playFile);		
	}
	
	public boolean isLoadingScreen(){
		if(loadingScreen != null){
			boolean show = loadingScreen.isShowing();
			return show;
		}
		else return false;
	}
	
	public void showLoadingScreen(String txt){
		
		Log.i(CLASS,"jude" + " ||||||||||||||showLoadingScreen|||||||||||| from " + txt);
		//show loading dialog
				loadingScreen = new ProgressDialog(activity);				
				loadingScreen.show();
				loadingScreen.setContentView(R.layout.custom_progressdialog);
				loadingScreen.setMessage("Preparing");
				loadingScreen.setCancelable(true);
				loadingScreen.setCanceledOnTouchOutside(false);
				loadingScreen.setOnCancelListener(new ProgressDialog.OnCancelListener(){
			         @Override
			          public void onCancel(DialogInterface dialog)
			          {
		                      loadingScreen.cancel();                      
			           }                                            
				});
	}
	
	public void showErrorPopup() {
		AlertDialog.Builder alert = new AlertDialog.Builder(activity);
		alert.setTitle("AudioPlug");
		alert.setMessage("Connection Fails. Forced to close");		
		alert.setCancelable(false);
		alert.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						((PlayerActivity) activity).finish();						
					}
				});		
		alert.show();
	}
 
	 
	 public void show3G_Dialog() {
			// TODO Auto-generated method stub
			if((Preferences.isDataEnabled(activity) == false) && Util.is3GConnected(activity))
			{
				AlertDialog.Builder alert = new AlertDialog.Builder(activity);
				alert.setTitle(activity.getApplicationContext().getResources().getString(R.string.app_name));
				alert.setMessage(R.string.dialog_allow_data);
				//alert.setIcon(R.drawable.ico_mdpi_videoplug);
				//alert.setOnKeyListener(backKeyListener);
				alert.setCancelable(false);
				alert.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Preferences.setDataEnabled(activity.getApplicationContext(), true);
						setEntryFromPlaylist(pInfo.index);
					}
				});
				
				alert.setNegativeButton("No",
						new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) { 
						//do nothing();
					}
				});
				alert.show();
			}
		}
	
	public void setEntryFromPlaylist(int index){
		Log.i("jude","allowMobileData : " + Preferences.isDataEnabled(context));
		try{					 
			pInfo.index = index;			
			if(pInfo != null){
				if(Util.is3GConnected(context) && Preferences.isDataEnabled(context) ==false){
					show3G_Dialog();
				}
				else{
					setPlayInfo(pInfo);
					channelName.setText(pInfo.channel.getTitle());			
					channelName.setGravity(Gravity.CENTER);
				}								
			}
		}catch(Throwable e){
			e.printStackTrace()			;
		}
	}
	
	public void onAudioEntryInfo(AudioFile entry) {
		
		Log.i(CLASS,"jude  " + "PlayerFragment_onAudioEntryInfo : " + entry.state);
		
		switch(entry.state) {
		case PREPARING:
			activity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					updateProgress(0, 0, 0);
					
				}
			});
			break;
			
		case BUFFERING:
			showLoadingScreen("onAudioEntryInfo-BUFFERING");
						
			break;
			
		case PLAYING:
		case PAUSED:
		case COMPLETED:
			Log.d(CLASS,"jude" + "isLoadingScreen : " + isLoadingScreen());
			if(isLoadingScreen())
				{
					removeLoadingScreen();					
				}
				else
				{
					//do nothing
				}						
			break;
			
		default:	
		}
	}
	
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		Log.d(CLASS,"jude " + "onStartTrackingTouch");
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		Log.d(CLASS,"jude " + "onStopTrackingTouch");
		if(service!= null)
			service.play();
		else
			removeLoadingScreen();
	}
		
	public void stopAudioService(){
		srvManager.stop();
	}

	public void updatePlayInfo(AudioFile playFile) {
		feedTitle.setText(playFile.title);
	}

	public void setPlayButton() {
		// TODO Auto-generated method stub		
		try{
			service = srvManager.getService();
			if(service != null){				
				if(service.isPlaying())
					buttonPlay.setImageResource(R.drawable.ic_playpage_pause);
				else
					buttonPlay.setImageResource(R.drawable.ic_playpage_play);
							
				updateProgress(
				service.getCurrentPosition(),
				service.getDuration(),
				service.getBufferPercent()
				);			
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public void removeLoadingScreen() {
		// TODO Auto-generated method stub		
		h.sendEmptyMessage(0);
	}
	
	public ServiceManager getSrvManager(){
		return srvManager;
	}
	
	
	public void onResult(int resultCode, Intent data){	     
        if(resultCode == 100){
        	setEntryFromPlaylist(data.getExtras().getInt("index"));
        }
	}
	
	@Override
	public void onPause() {
		super.onPause();
		Log.e("jude","PlayerFragment onPause");				
	}
	
	@Override
	public void onStop() {
		super.onPause();
		mainHandler.removeCallbacks(runnable);				
	}
	
	@Override
	public void onStart() {		
		super.onStart();	
		Log.i(CLASS,"jude " + "PlayerFragment onStart");
		//getView().getHandler().postDelayed(runnable, 1000);
		//activity.runOnUiThread(runnable);
		mainHandler.postDelayed(runnable, 1000);
		
	}

	@Override
	public void onResume() {
		Log.i("jude","PlayerFragment onResume");
		super.onResume();	
		setPlayButton();		
		service = srvManager.getService();		
	}
	
	private Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			mainHandler.postDelayed(runnable, 1000);
			if(srvManager!= null)
				service = srvManager.getService();
			else
				service = null;
			setPlayButton();
			if(service != null && service.isPlaying()) {			
				int position = service.getCurrentPosition();
				int duration = service.getDuration();
				int buffer = service.getBufferPercent();
				Log.i("position", "position send" + position + "/" + duration
						+ "/" + buffer);
				updateProgress(position, duration, buffer);
				updatePlayInfo(service.getAudioEntry());
				removeLoadingScreen();
			}
			
		}

	};	
		
	class DisplayHandler extends Handler{		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what == 0){				
				if(loadingScreen != null)
					loadingScreen.cancel();
			}
		}
		
	}
	
	public static String getTimeDiffFormat(long timeMilli) {
		long time = timeMilli / 1000;
		String format = String.format("%%0%dd", 2);
		String seconds = String.format(format, time % 60);
		String minutes = String.format(format, (time % 3600) / 60);
		String hours = String.format(format, time / 3600);
		String text = hours + ":" + minutes + ":" + seconds;
		return text;
	}
	
}