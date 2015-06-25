package com.airplug.audioplug.player;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.airplug.audioplug.Preferences;
import com.airplug.audioplug.data.ConstData;
import com.airplug.audioplug.dev.R;
import com.airplug.audioplug.player.AudioService.IServiceListener;
import com.airplug.audioplug.util.Util;
import com.airplug.audioplug.widget.FourLongWidget;

public abstract class AudioPlayer {

	private static final String NAME = "AudioPlayer";
	private final String CLASS = NAME + "@" + Integer.toHexString(hashCode());
	
	public enum State {
		IDLE,
		PREPARING,
		PLAYING,
		PAUSED,
		BUFFERING,
		COMPLETED,
		ERROR,
		STOPPED,
	}
	
	public interface Callback {
		public IServiceListener getRadioCallback();
	}
	
	protected interface DestroyCallback {
		public void onDestroy();
	}

	protected final Service context;
	protected final ArrayList<AudioFile> entries;
	protected volatile AudioFile entry;
	private boolean running;
	private IServiceListener listener;
	private int index;
	
	protected abstract void create();
	protected abstract void destroy();
	protected abstract void loadList();
	
	public abstract boolean isPlaying();
	public abstract int getDuration();
	public abstract int getCurrentPosition();
	public abstract int getBufferPercent();
	
	public abstract void play();
	public abstract void pause();
	public abstract void stop();
	public abstract void seekTo(int msec);

	
	public AudioPlayer(Service context, ArrayList<AudioFile> entries) {
		this.context = context;
		this.entries = entries;
	}
	
	public void open(int position) {
		index = position;
		Log.i("jude","index_audioplayer.open : " + index);
		if(entries != null){
		if(index == -1){
			return;			
		}
		else{
			if(entries.size() -1 < index){
				index = 0;
			}
			entry = entries.get(index);		
			loadList();
			// save 
			SharedPreferences prefs = context.getSharedPreferences(ConstData.CurrentPlayPrefs, context.MODE_PRIVATE);
			SharedPreferences.Editor ed = prefs.edit();
			ed.putInt("index", entry.index);
			ed.commit();
		}
		}
	}
	
	private void updateWidgets()
	{
		//To avoid thumbnail issue of notification.
		getService().showNotification();
		
		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		AudioFile entry = getAudioEntry();
	
//		OneCellWidget.updateWidget(this, manager, song, state);
		FourLongWidget.updateWidget(context, manager, entry, ((AudioService)context).getState());
		//FourLongWidgetLock.updateWidget(context, manager, entry, ((AudioService)context).getState());

//		FourSquareWidget.updateWidget(this, manager, song, state);
//		WidgetD.updateWidget(this, manager, song, state);
//		WidgetE.updateWidget(this, manager, song, state);
	}
	public void close() {
		Log.d("jude","AudioPlayer : close()");
		getService().giveUpAudioFocus();
		context.stopForeground(true);
		destroy();
	}
		
	public void playNext() {
		if(Util.is3GConnected(context) && Preferences.isDataEnabled(context) == false){
			//show pop up
			//show3G_Dialog(index-1);
		}
		else
		{
		
			if(entries != null){
				if(0 <= entries.size()) {
					setState(State.STOPPED);
					close();			
					index--;			
					if(index < 0)
						open(entries.size()-1);
					else open(index);
				} 
				else {
					Log.i("jude","stopservice");
					stopService();
				}	
			}			
		}		
	}
	
	public void show3G_Dialog(final int i) {
		// TODO Auto-generated method stub
		AlertDialog.Builder alert = new AlertDialog.Builder(context);
		alert.setTitle(context.getResources().getString(R.string.app_name));
		alert.setMessage(R.string.dialog_allow_data);
		//alert.setIcon(R.drawable.ico_mdpi_videoplug);
		//alert.setOnKeyListener(backKeyListener);
		alert.setCancelable(false);
		alert.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Preferences.setDataEnabled(context, true);
				open(i);
			}

		});
		
		alert.setNegativeButton("No",
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) { 
				//do nothing
			}
		});
		alert.show();
	}
	
	
	public void playPrevious() {
		//entries.remove(entry);
		if(Util.is3GConnected(context) && Preferences.isDataEnabled(context) == false){
			//show pop up
			//show3G_Dialog(index+1);
		}
		else
		{
			if(entries != null){
				if(0 < entries.size()) {
					close();
					index++;
					open(index);
					//setState(State.PLAYING);
				} else {			
					stopService();
				}
			}
		}
	}
	
	void stopService() {
		Log.i("jude","=======stopService========");
		setState(State.STOPPED);
		context.stopForeground(true);		
		getService().giveUpAudioFocus();
		context.stopSelf();		
	}
	
	public void setServiceListener(IServiceListener listener) {
		this.listener = listener;
		notifyEntryStateChanged();
	}
	
	public AudioFile getAudioEntry() {				
		if(index <0){
			return null;
		}
		return entry;
	}
	
	public ArrayList<AudioFile> getEntries() {
		return entries;
	}
	
	private void notifyEntryStateChanged() {
		if(listener != null) {
			listener.onAudioEntryInfo(entry);
		}
	}
	
	protected AudioService getService() {
		return (AudioService)context;
	}
	
	protected void setState(final State state) {
		getService().workHandler().post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				Log.d("jude",  entry.state + " ---> " + state);
				entry.state = state;
				notifyEntryStateChanged();

				switch(state) {
				case PREPARING:
					//getService().startForeground();
					if(running == false) {
						running = true;
					}					
					break;
					
				case BUFFERING:
					getService().setState(ConstData.FLAG_BUFFERING);
					getService().showNotification();					
					break;
					
				case PLAYING:
					getService().setState(ConstData.FLAG_PLAYING);					
					getService().showNotification();					
					break;
					
				case PAUSED:					
					getService().setState(ConstData.FLAG_PAUSE);
					getService().showNotification();					
					break;

				case STOPPED:					
					getService().setState(ConstData.FLAG_STOPPED);					
					break;
					
				case ERROR:
					getService().playSound(R.raw.buffering);
					getService().setState(ConstData.FLAG_ERROR);
					//PlayerView.showErrorPopup();					
					if(ConstData.DEBUG) {
						getService().workHandler().postDelayed(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								stopService();								
							}
						}, 1000);												
					}					
					
				case COMPLETED:
					//jude_test
					playNext();
					break;
					
				default:					
					break;				
				}	
				
				updateWidgets();
			}

		});
	}
	
	
	private void showErrorPopup() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("No connection. Please retry after connect to network");
        builder.setCancelable(true);                
        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        
        AlertDialog alert = builder.create();
        alert.show();
	}	
	
	
	private Boolean isConnected() {
		// TODO Auto-generated method stub
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		
		if(mobile.isConnected() || wifi.isConnected())
			return true;
		else
			return false;		
		
	}
	public int getIndex() {
		return index;
	}
	
}
