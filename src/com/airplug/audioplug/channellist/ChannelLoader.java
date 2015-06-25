package com.airplug.audioplug.channellist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.airplug.audioplug.channellist.RSSFile.FeedElements;
import com.airplug.audioplug.dev.R;
import com.airplug.audioplug.util.ImageCache;

public class ChannelLoader {

	private static final String NAME = "ChannelLoader";
	private final String CLASS = NAME + "@" + Integer.toHexString(hashCode());
	
	private final List<Channel> listChannel;
	private final BaseAdapter adapter;
	private final ParserBase parser;

	private ExecutorService pool = Executors.newFixedThreadPool(10);
	private Handler handler= new Handler();
	
	public ChannelLoader(List<Channel> data, BaseAdapter adapter, ParserBase parser) {
		this.listChannel = data;
		this.adapter = adapter;
		this.parser = parser;
	}
	
	public Channel load(int position, Context ctx, ChannelListImageAdapter.ViewHolder convertView) {		
		Channel channel = listChannel.get(position);
		ImageView imageView = (ImageView) convertView.imageView;
		TextView ChannelTitle = (TextView)convertView.ChannelTitle;		

		Log.d("CL", "ch:"+channel.getTitle()+" position:"+position);
		if(channel.isLoading() == false) {	
			channel.setLoading(true);
			channel.setLoadedText(false);
			channel.setLoadedImage(false);
			ChannelTitle.setText("loading...");
			imageView.destroyDrawingCache();

			imageView.setImageResource(R.drawable.ic_channel_default);
			pool.submit(new ItemLoader(channel, ctx, convertView));
		}
		return channel;
	}
	
	public void shutdown() {
		pool.shutdown();
	}

	class ItemLoader implements Runnable {
		
		Channel channel;
		private ImageView imageView;
		private TextView ChannelTitle;
		private Context ctx;
		ArrayList<FeedElements> feedList;
		
		ItemLoader(Channel channel, Context ctx, ChannelListImageAdapter.ViewHolder convertView) {
			this.channel = channel;
			this.ctx = ctx;
			imageView = (ImageView) convertView.imageView;
			ChannelTitle = (TextView)convertView.ChannelTitle;		


		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {								
				feedList = parser.parse(channel);	

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
						if( feedList != null){
//							channel.setLoading(true);
//							channel.setLoadedText(false);
//							channel.setLoadedImage(false);

						ChannelTitle.setText(channel.getTitle());
						if (channel.getBackGroundColor() != 0) {
							ChannelTitle.setBackgroundColor(channel.getBackGroundColor());
						}
							ImageCache.getInstance(ctx).getImageLoader().displayImage(channel.getThumbnailUrl(), imageView, ImageCache.getInstance(ctx).getOption());
						}
						
					}
				});
			}
		}

		
		void onException(Throwable e) {			
			channel.setTitle(e.toString().split(":")[0]);
			channel.setLoadedText(true);
		}
		
	}

}
