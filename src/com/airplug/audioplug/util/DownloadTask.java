/*
 *Copyright linuxs by Airplug.,
 *All rights reserved.
 *Airplug.com
*/
package com.airplug.audioplug.util;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.airplug.audioplug.channellist.Channel;
import com.airplug.audioplug.channellist.ChannelListImageAdapter;
import com.airplug.audioplug.channellist.ParserBase;
import com.airplug.audioplug.channellist.RSSFile.FeedElements;
import com.airplug.audioplug.dev.R;

public class DownloadTask extends AsyncTask<String, Integer, Long> {
	ParserBase parser;
	ImageView imageView;
	Channel channel;
	private ArrayList<FeedElements> feedList;
	private TextView ChannelTitle;
	private Context ctx;
	private ChannelListImageAdapter channelListImageAdapter;
	private ProgressDialog mDlg;
	
	
    public DownloadTask(Context ctx, ParserBase parser, Channel channel, ChannelListImageAdapter.ViewHolder convertView, ChannelListImageAdapter channelListImageAdapter) {
		this.parser = parser;
		this.channel = channel;
		this.ctx = ctx;
		this.imageView = (ImageView) convertView.imageView;
		this.ChannelTitle = (TextView)convertView.ChannelTitle;		
		this.channelListImageAdapter = channelListImageAdapter;
	}

	protected Long doInBackground(String... urls) {
          long totalSize = 0;
			try {
				feedList = parser.parse(this.channel);
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}	

          return totalSize;
    }

    protected void onProgressUpdate(Integer... progress) {
//          setProgressPercent(progress[0]);
		
    }

    @Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
    	
    	
    	mDlg = new ProgressDialog(ctx);
    	mDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    	mDlg.setMessage("loading...");
    	mDlg.show();

    	ChannelTitle.setText("load");
		imageView.setImageResource(R.drawable.ic_channel_default);
		
		super.onPreExecute();
		
	}

	protected void onPostExecute(Long result) {
    	//complete
//          showDialog("Downloaded " + result + " bytes");
		channel.setLoading(true);

    	if( feedList != null){
//			channel.setLoading(true);
//			channel.setLoadedText(fatlse);
//			channel.setLoadedImage(false);

			ChannelTitle.setText(channel.getTitle());
			if (channel.getBackGroundColor() != 0) {
				ChannelTitle.setBackgroundColor(channel.getBackGroundColor());
			}
			imageView.setScaleType(ScaleType.FIT_XY);
				ImageCache.getInstance(ctx).getImageLoader().displayImage(channel.getThumbnailUrl(), imageView, ImageCache.getInstance(ctx).getOption());
			}
    	
//    	this.channelListImageAdapter.notifyDataSetChanged();
//		((UpdateChannelListener)ctx).onComplete();

    	mDlg.dismiss();
    }
}