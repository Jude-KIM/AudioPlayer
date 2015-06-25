package com.airplug.audioplug.channelhome;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.airplug.audioplug.channellist.RSSFile.FeedElements;
import com.airplug.audioplug.dev.R;

public class FeedListAdapter extends BaseAdapter {

	private final LayoutInflater inflater;
	private ArrayList<FeedElements> feeds;	
	private volatile boolean paused;
	
	//public FeedListAdapter(Context context, ArrayList<FeedElements> data, String thumb){
	public FeedListAdapter(Context context, ArrayList<FeedElements> feeds){
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.feeds = feeds;	
	}
	
	public void clearFeedList(){
		feeds.clear();
	}
	
	public void setFeedList(ArrayList<FeedElements> feeds){
		this.feeds = feeds;
	}
	
	public void pause() {
		paused = true;
	}
	
	public void resume() {
		paused = false;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return feeds.size();
	}

	@Override
	public Object getItem(int position) {
		return feeds.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(paused) {
			return convertView;
		}
		
		View view = convertView;
		if(view == null) {
			view = inflater.inflate(R.layout.feed_item, null);
		}
		
		FeedElements feed_item = feeds.get(position);		
		// set title and date
		setText(view, R.id.FeedItemTitle, feed_item.title);
		setText(view, R.id.FeedItemDate, feed_item.pubDate);
		
		ProgressCircleView status = (ProgressCircleView) view.findViewById(R.id.FeedItemStatus);
		//TODO set play status - brown
		status.setValue((float) (Math.random() * 100));
		//if channel
		//status.setChannelMark(true);
		
		return view;
	}
	
	private void setText(View view, int id, String text) {
		if(text != null) {
			TextView textView = (TextView)view.findViewById(id);
			textView.setText(text);
		}
	}

}
