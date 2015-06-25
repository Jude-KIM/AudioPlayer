/*
 *Copyright linuxs by Airplug.,
 *All rights reserved.
 *Airplug.com
*/
package com.airplug.audioplug.player;

import java.util.ArrayList;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.airplug.audioplug.channellist.Channel;
import com.airplug.audioplug.player.AudioService.AudioServiceBinder;
import com.airplug.audioplug.player.AudioService.IServiceListener;

public class ServiceManager  implements IServiceListener{
	
	private static final String NAME = "ServiceManager";
	private final String CLASS = NAME + "@" + Integer.toHexString(hashCode());

	private boolean bound;
	private Context ctx;
	private ArrayList<AudioFile> playlist;
	private boolean notList;
	private Channel channel;
	private static IAudioService service ;
	private boolean playing;
	private int index;
	
	static ServiceManager instance = null;
	

		
	
//	 public static ServiceManager getInstance(Context context){
//		 if(instance == null){
//			 instance= new ServiceManager(context);
//		 }
//
//		 return instance;
//	 }
	
//	public ServiceManager(Context ctx, ArrayList<AudioFile> playlist, boolean notList, Channel channel){
//		this.ctx = ctx;
//		this.notList = notList;
//		this.channel = channel;
//		if(this.playlist.equals(playlist)){
//			// equal playlist
//		}else{
//			this.playlist = playlist;
//		}
//		bind();
//	}

	public ServiceManager(Context context) {
		this.ctx = context;
	//	bind();
	}
	
	public void start(PlayInfo pInfo){
		if (pInfo != null) {
			if (pInfo.index == -1) {
				return;
			}
			this.index = pInfo.index;			
			this.channel = pInfo.channel;
			this.playing = pInfo.playing;
			AudioFile afile = pInfo.playlist.get(pInfo.index);
			Log.d(CLASS, "APService 1 ");
			
			service = this.getService();
			Log.d("jude","service ?? " + service);
			//if (service != null && service.isPlaying()) {
			if (service != null && service.getAudioEntry() != null) {
				if (service.getAudioEntry().title.equals(afile.title)) {
					return;
				}
			}
			this.playlist = pInfo.playlist;
			stopAudioService();

		}else{
			
		}

		Log.i("jude", "start ");		
		//bind();
		startAudioService();		
	}
	

	public IAudioService getService(){
		return service;
	}
	private void startAudioService( ) {
		Log.e("jude","startAudioService : " + service + " || bound : " + bound);		
		if(service != null){
			if(bound)
				return;
		}
		
		if (bound == false) {
			bound = ctx.bindService(getServiceIntent(), serviceConnection, 0);			
		}
		Log.d(CLASS, "bound " + bound);		
		//bind();
	}
	
	
	public void stopAudioService() {
		Log.d("jude","stopAudioService!");
		boolean success = ctx.stopService(getServiceIntent());
		Log.d(CLASS, "stopService " + success);		

		if (bound ) {
			ctx.unbindService(serviceConnection);
			
		}
		bound = false;
		service = null;
	}
	
	public void unbind(Context c){
		Log.d("jude","bound? " + bound);
		if (bound ) {
			c.unbindService(serviceConnection);
			bound = false;
		}		
		Log.i("jude","bound? " + bound);
	}
		
//	private void bind() {
//		if (bound == false) {
//			bound = ctx.bindService(getServiceIntent(), serviceConnection, 0);
//			Log.d(CLASS, "bound " + bound);
//		}
//	}

	private Intent getServiceIntent() {
		if(playlist == null){
//			ComponentName componentName = new ComponentName(ctx, AudioService.class);
			return new Intent(AudioService.ACTION_TOGGLE);
		}else{
			return new Intent(AudioService.getAction(ctx))
				.putExtra("entries", playlist)
				.putExtra("notList", notList)
				.putExtra("channel", channel)
				.putExtra("index", index);
		}
	}
	
	private ServiceConnection serviceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder binder) {
			Log.d(CLASS, "onServiceConnected: " + name);			

			if(service == null){
				service = ((AudioServiceBinder) binder).getAudioService();
				service.setServiceListener(ServiceManager.this);
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.d(CLASS, "onServiceDisconnected: " + name);			
//			oldEntry = null;
			service = null;
		}
	};

	@Override
	public void onAudioEntryInfo(AudioFile entry) {
		//	if(ctx instanceof IServiceListener){
		Log.d(CLASS, "onStateChanged: " + entry);
		if(ctx instanceof IServiceListener){
			((IServiceListener)ctx).onAudioEntryInfo(entry);
		}
	}
	
	public boolean isPlaying(){
		if(service == null){
			return false;
		}else{
			return service.isPlaying();
		}
	}
	
	public int getState(){
		if(service == null){
			return 0;
		}
		return service.getState();
	}
	
	public int getDuration(){
		if(service == null){
			return 0;
		}
		return service.getDuration();
	}
	public int getCurrentPosition(){
		if(service == null){
			return 0;
		}
		return service.getCurrentPosition();
	}
	public int getBufferPercent(){
		if(service == null){
			return 0;
		}
		return service.getBufferPercent();
	}
	
	public void play(){
		if(service == null){
			return ;
		}
		service.play();
	}
	public void pause(){
		if(service == null){
			return;
		}
		service.pause();
	}
	public void stop(){
		if(service == null){
			return ;
		}
		service.stop();
	}
	public void seekTo(int msec){
		if(service == null){
			return ;
		}
		service.seekTo(msec);
	}
	
	public void playNext(){
		if(service == null){
			return ;
		}
		service.playNext();
	}

	public void sendCommand(PlayInfo pInfo) {
		if (pInfo != null) {
			if (pInfo.index == -1) {
				return;
			}
			this.index = pInfo.index;			
			this.channel = pInfo.channel;
			AudioFile afile = pInfo.playlist.get(pInfo.index);
			Log.d(CLASS, "APService 1 ");
						
			if (service != null) {
				if (service.getAudioEntry().title.equals(afile.title)) {
					return;
				}
			}
			this.playlist = pInfo.playlist;
//			stopAudioService();
		}
		
		ComponentName name = ctx.startService(getServiceIntent());
		Log.d(CLASS, "startService " + name);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
