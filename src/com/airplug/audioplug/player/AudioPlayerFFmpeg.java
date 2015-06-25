package com.airplug.audioplug.player;

import java.util.ArrayList;

import android.app.Service;

public class AudioPlayerFFmpeg extends AudioPlayer {

	public AudioPlayerFFmpeg(Service ctx, ArrayList<AudioFile> list) {
		super(ctx, list);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void create() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void loadList() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isPlaying() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getDuration() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getCurrentPosition() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getBufferPercent() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void play() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void seekTo(int msec) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
}
