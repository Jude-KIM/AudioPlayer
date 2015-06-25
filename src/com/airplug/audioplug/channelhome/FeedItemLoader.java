package com.airplug.audioplug.channelhome;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;

import com.airplug.audioplug.channellist.Channel;
import com.airplug.audioplug.channellist.ParserBase;
import com.airplug.audioplug.channellist.RSSFile.FeedElements;

public class FeedItemLoader implements Runnable {
		
		private Channel channel;
		private ParserBase parser;
		private Handler handler= new Handler();
		private FeedListAdapter adapter;
		private ArrayList<FeedElements> feeds;		
		private FrameLayout frame;
		
		FeedItemLoader(Channel channel, ParserBase parser, FeedListAdapter adapter, ArrayList<FeedElements> feeds, FrameLayout refreshlayout) {
			this.channel = channel;
			this.parser = parser;
			this.adapter = adapter;			
			this.feeds = feeds;
			frame = refreshlayout;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				frame.setVisibility(View.VISIBLE);
				this.feeds = parser.parse(channel);				
				adapter.setFeedList(feeds);				
			} catch (ParserConfigurationException e) {
				onException(e);
			} catch (SAXException e) {
				onException(e);
			} catch (IOException e) {
				onException(e);
			} finally {
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						adapter.notifyDataSetChanged();
						frame.setVisibility(View.GONE);
						
					}
				});
			}
		}

		
		void onException(Throwable e) {			
			channel.setTitle(e.toString().split(":")[0]);
			channel.setLoadedText(true);
		}
		
		public ArrayList<FeedElements> getFeeds(){			
			return feeds;
		}
		
}

