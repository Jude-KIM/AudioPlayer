package com.airplug.audioplug.channellist;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.airplug.audioplug.channelhome.ChannelHomeActivity;
import com.airplug.audioplug.dev.R;

public class ChannelListFragment extends Fragment implements UpdateChannelListener{
	
	public enum Type {
		RSS,
		ICECAST
	}
	
	public static final List<Channel> list = new ArrayList<Channel>();
	private ChannelListImageAdapter adapter;
	private Context mContext;
	private List<Channel> init(String[] URLS) {
		list.clear();
		for(int n = 0; n < URLS.length; n++) {
			list.add(new Channel(URLS[n], "", "", null, n));
		}
		return list;
	}
	
	public ChannelListFragment(Context context) {
		mContext = context;
	}
	
	public ChannelListFragment(){
		Log.e("jude","emptry ChannelList constructor");
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View view = inflater.inflate(R.layout.fragment_channel_list, container, false);
		GridView gridview = (GridView) view.findViewById(R.id.ChannelListGridView);		
		ChannelList channelList = new ChannelList(Type.RSS);
		ParserBase parser = ChannelList.getParser(mContext, channelList.type);
		ChannelListImageAdapter adapter = new ChannelListImageAdapter(mContext, init(channelList.urls), parser );		
		gridview.setAdapter(adapter);
		gridview.setOnItemClickListener(new OnItemClickListener() {
		
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub

				//Don't show channel home if network error
				if(list.get(position) == null ||
				   list.get(position).getTitle() == "" ||
				   list.get(position).getException() == true)
				{
					return;
				}				
				
				Intent intent = new Intent(mContext.getApplicationContext(), ChannelHomeActivity.class);
				intent.putExtra("channel", list.get(position));
				intent.putExtra("index", position);				
				startActivity(intent);				
			}
			
		});		
		
		return view;		
	}	
	
	@Override
	public void onComplete() {
		adapter.notifyDataSetChanged();
		
	}

}
