package com.airplug.audioplug.channellist;

import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.airplug.audioplug.dev.R;
import com.airplug.audioplug.util.DownloadTask;
import com.airplug.audioplug.util.ImageCache;

public class ChannelListImageAdapter extends BaseAdapter   {

	private Context mContext;
	private final LayoutInflater inflater;
	private final ChannelLoader loader;
	private final int count;
	
	private volatile boolean paused;
	private List<Channel> channelList;
	private Channel channelItem ;
	private ParserBase parser;
	Activity activity;
	ProgressDialog loadingScreen;
	
    public ChannelListImageAdapter(Context c, List<Channel> channel, ParserBase parser) {
        mContext = c;
        inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.parser = parser;
		loader = new ChannelLoader(channel, this, parser);
		count = channel.size();		
		this.channelList = channel;
    }
    
	public void shutdown() {
		loader.shutdown();
	}
	
	public void pause() {
		paused = true;
	}
	
	public void resume() {
		paused = false;
	}    
    

    public int getCount() {
        return count;
    }

    @Override
	public Object getItem(int position) {
		return channelList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public class ViewHolder {
		public TextView ChannelTitle;
		public ImageView imageView;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		activity = (Activity) mContext;
		//loadingScreen = ProgressDialog.show(activity, null, "Receiving Data...");
		View view = convertView;

		final ViewHolder holder;
		if (convertView == null) {
			view =  inflater.inflate(R.layout.channellist_item, null);
			holder = new ViewHolder();
			holder.ChannelTitle = (TextView) view.findViewById(R.id.ChannelItemTitle);
			holder.imageView = (ImageView) view.findViewById(R.id.ChannelItemThumbnail);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
//			holder.ChannelTitle.setText("loading...");
		}
		
		
//		holder.imageView.setImageResource(R.drawable.ic_action_play);

		Channel channel = (Channel)getItem(position);
		if(channel != null){
			if(channel.isLoading() == false){
				holder.ChannelTitle.setText("loading...");
				holder.imageView.setImageResource(R.drawable.ic_channel_default);
//				loader.load(position, mContext, holder);
				
				DownloadTask dt = new DownloadTask(mContext, parser, channel, holder, this);
				dt.execute("");
//				try {
////					dt.get();
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (ExecutionException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				
			}else{
				holder.ChannelTitle.setText(channel.getTitle());
				if (channel.getBackGroundColor() != 0) {
					holder.imageView.setBackgroundColor(channel.getBackGroundColor());
				}
				ImageCache.getInstance(mContext).getImageLoader().displayImage(channel.getThumbnailUrl(), holder.imageView, ImageCache.getInstance(mContext).getOption());
			}
			//loadingScreen.cancel();
		}
		
		return view;
	}

}
