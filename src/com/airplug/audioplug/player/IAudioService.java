package com.airplug.audioplug.player;

import java.io.Serializable;
import java.util.ArrayList;

import com.airplug.audioplug.channellist.Channel;
import com.airplug.audioplug.player.AudioService.IServiceListener;

public interface IAudioService {
	public void setServiceListener(IServiceListener listener);
	public AudioFile getAudioEntry();
	
	public boolean isPlaying();
	public int getDuration();
	public int getCurrentPosition();
	public int getBufferPercent();	
	
	public void play();
	public void pause();
	public void stop();
	public void seekTo(int msec);
	
	public void playNext();	
	public void playPrevious();	
	
	public void setContents( ArrayList<AudioFile> playlist, boolean notList, Channel channel ) ;
	public int getState();
	
	public Serializable getEntries();
	public int getIndex();
	Channel getChannel();	
}
