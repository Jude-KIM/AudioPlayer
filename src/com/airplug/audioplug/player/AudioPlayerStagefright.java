package com.airplug.audioplug.player;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Service;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

public class AudioPlayerStagefright extends AudioPlayer {

	private static final String NAME = "AudioPlayerStagefright";
	private final String CLASS = NAME + "@" + Integer.toHexString(hashCode());
		
	private MediaPlayer mediaPlayer;
	private int bufferPercent;
	private Context mContext;
	private AudioManager am;
	
	public AudioPlayerStagefright(Service ctx, ArrayList<AudioFile> list) {
		super(ctx, list);
		mContext = ctx;
	}

	@Override
	protected void create() {
		Log.d(CLASS, "create: " + entry);
		Log.i("jude", "AduioPlayerStatefright Create()");		
				
			
		try {
//			if(maoStream.isRedirected()) {
			mediaPlayer = new MediaPlayer();			
//			} else {
//				//mediaPlayer = new MediaPlayer();
//				createMediaPlayerIfNeeded();
//			}
			
			Log.d(CLASS, "url: " + entry.url);
			
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setDataSource(entry.url);
			mediaPlayer.setOnPreparedListener(onPreparedListener);
			mediaPlayer.setOnBufferingUpdateListener(onBufferingUpdateListener);
			mediaPlayer.setOnInfoListener(onInfoListener);
			mediaPlayer.setOnCompletionListener(onCompletionListener);
			mediaPlayer.setOnErrorListener(onErrorListener);
			mediaPlayer.prepareAsync();

			setState(State.PREPARING);

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			setState(State.ERROR);
		} catch (SecurityException e) {
			e.printStackTrace();
			setState(State.ERROR);
		} catch (IllegalStateException e) {
			e.printStackTrace();
			setState(State.ERROR);
		} catch (IOException e) {
			e.printStackTrace();
			setState(State.ERROR);
		}
		
		
	}

	private void createMediaPlayerIfNeeded() {
		// TODO Auto-generated method stub
		if (mediaPlayer == null) {
			mediaPlayer = new MediaPlayer();

            // Make sure the media player will acquire a wake-lock while playing. If we don't do
            // that, the CPU might go to sleep while the song is playing, causing playback to stop.
            //
            // Remember that to use this, we have to declare the android.permission.WAKE_LOCK
            // permission in AndroidManifest.xml.
			mediaPlayer.setWakeMode(context.getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);

            // we want the media player to notify us when it's ready preparing, and when it's done
            // playing:            
        }
        else
        	mediaPlayer.reset();
		
	}

	@Override
	protected void destroy() {
		Log.i("jude","AudioPlayerStagefright : destroy()");		
		setState(State.STOPPED);
		
		if(mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

	@Override
	protected void loadList() {
		if(AudioList.isM3U(entry.url)) {
			requestList(new AudioListM3U());
		} else if(AudioList.isSHOUTcast(entry.url)) {
			requestList(new AudioListSHOUTcast());
		} else {
			create();
		}
	}

	@Override
	public boolean isPlaying() {
		if(mediaPlayer != null) {
			return mediaPlayer.isPlaying();
		}
		return false;
	}

	@Override
	public int getDuration() {
		if(mediaPlayer != null) {
			return mediaPlayer.getDuration();
		}
		return 0;
	}
	
	@Override
	public int getCurrentPosition() {
		if(mediaPlayer != null) {
			return mediaPlayer.getCurrentPosition();
		}
		return 0;
	}

	@Override
	public int getBufferPercent() {
		return bufferPercent;
	}

	@Override
	public void play() {
		if(mediaPlayer != null) {			
			mediaPlayer.start();						
			setState(State.PLAYING);
		}
	}

	@Override
	public void pause() {
		if(mediaPlayer != null) {
			mediaPlayer.pause();
			setState(State.PAUSED);			
		}
	}
	
	@Override
	public void stop() {
		Log.e("jude","AudioPlayerStagefright : stop()");
		setState(State.STOPPED);		
	
		if(mediaPlayer != null) {
			mediaPlayer.reset();
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

	@Override
	public void seekTo(int msec) {
		if(mediaPlayer != null) {
			mediaPlayer.seekTo(msec);
		}
	}

	private MediaPlayer.OnPreparedListener onPreparedListener =
			new MediaPlayer.OnPreparedListener() {

		@Override
		public void onPrepared(final MediaPlayer mp) {
			Log.i("listener", "onPreparedListener");
			Log.d(CLASS, "onPrepared: " + mp.getDuration());
			setState(State.PLAYING);
			mp.start();
		}
	};
	
	private MediaPlayer.OnBufferingUpdateListener onBufferingUpdateListener =
			new MediaPlayer.OnBufferingUpdateListener() {

		@Override
		public void onBufferingUpdate(MediaPlayer mp, int percent) {
			Log.i("listener", "onBufferingUpdateListener");
			Log.v(CLASS, "onBufferingUpdate: " + percent + " " + mp.getCurrentPosition());
			bufferPercent = percent;
		}
	};

	private MediaPlayer.OnInfoListener onInfoListener =
			new MediaPlayer.OnInfoListener() {

		@Override
		public boolean onInfo(MediaPlayer mp, int what, int extra) {
			Log.w(CLASS, "onInfo: " + what + " " + extra);
			Log.i("listener", "onInfoListener: " + what + " " + extra);
			
			switch(what) {
			case MediaPlayer.MEDIA_INFO_BUFFERING_START:
				setState(State.BUFFERING);
				break;
				
			case MediaPlayer.MEDIA_INFO_BUFFERING_END:
				setState(State.PLAYING);
				break;
			}
			
			return false;
		}
	};

	private MediaPlayer.OnCompletionListener onCompletionListener =
			new MediaPlayer.OnCompletionListener() {

		@Override
		public void onCompletion(MediaPlayer mp) {
			Log.i(CLASS, "onCompletion");
			Log.i("listener", "onCompleteListener");
			setState(State.COMPLETED);
		}
	};

	private MediaPlayer.OnErrorListener onErrorListener =
			new MediaPlayer.OnErrorListener() {

		@Override
		public boolean onError(MediaPlayer mp, int what, int extra) {
			Log.e(CLASS, "onError: " + what + " " + extra);
			Log.i("listener", "onErrorListener " + what + " " + extra);
			String m1 = null;
			String m2 = null;			
			switch (what){
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                 m1 = "MEDIA_ERROR_UNKNOWN";
                 break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
            	m1 = "MEDIA_ERROR_SERVER_DIED";
            	break;
            default:            	
            	break;
			}
			
			if(m1 != null)
				Toast.makeText(context.getApplicationContext(), m1, Toast.LENGTH_SHORT).show();
			
			if(what != -38)
				setState(State.ERROR);

//			switch (extra){
//            case MediaPlayer.MEDIA_ERROR_IO:
//            	m2 = "IO media error";                 
//                 break;
//            case MediaPlayer.MEDIA_ERROR_MALFORMED:
//            	m2 = "media error, malformed";                 
//                 break;
//            case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
//            	m2 = "unSupported media content";                 
//                 break;
//            case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
//            	m2 = "media timeout error";                 
//                 break;
//            default:
//            	m2 = "unknown playback error";                 
//                 break;
//			}
	
			return true;
		}
	};
			
	private void requestList(final AudioList audioList) {
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					audioList.open(entry);
					Log.d(CLASS, "list " + audioList);
					
					if(audioList.list.isEmpty()) {
						Log.e(CLASS, "empty list");
						setState(State.ERROR);
						return;
					}
					
					entry.url = audioList.list.get(0).url;
					
					create();
					
				} catch (IOException e) {
					e.printStackTrace();
					setState(State.ERROR);
				} finally {
					audioList.close();
				}
				
			}
		};
		new Thread(runnable).start();
	}
}
