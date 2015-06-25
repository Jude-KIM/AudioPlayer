/*
 *Copyright linuxs by Airplug.,
 *All rights reserved.
 *Airplug.com
*/
package com.airplug.audioplug.player;

import java.util.ArrayList;

import com.airplug.audioplug.channellist.Channel;

public class PlayInfo {
	
	public ArrayList<AudioFile> playlist;	
	public boolean playing;
	public Channel channel;
	public int index;
	
	public PlayInfo(ArrayList<AudioFile> playlist, boolean notList,
			Channel channel, int index) {
		super();
		this.playlist = playlist;		
		this.channel = channel;
		this.index = index;		
	}

	public PlayInfo() {
		
	}
	
		public PlayInfo getPlayInfo(){
		return this;
	}

}
