package com.airplug.audioplug.channelhome;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.airplug.audioplug.channellist.Channel;
import com.airplug.audioplug.channellist.ChannelList;
import com.airplug.audioplug.channellist.RSSFile;
import com.airplug.audioplug.channellist.RSSFile.FeedElements;
import com.airplug.audioplug.data.ConstData;
import com.airplug.audioplug.dev.R;
import com.airplug.audioplug.player.AudioFile;
import com.airplug.audioplug.player.AudioPlayer.State;
import com.airplug.audioplug.player.IAudioService;
import com.airplug.audioplug.player.PlayerActivity;
import com.airplug.audioplug.player.ServiceManager;
import com.airplug.audioplug.share.ShareActivity;
import com.airplug.audioplug.util.ImageCache;
import com.airplug.audioplug.util.ObjectPreference;

public class ChannelHomeActivity extends Activity implements OnClickListener, OnItemClickListener{
	
	private static final String NAME = "ChannelHomeActivity";
	private final String CLASS = NAME + "@" + Integer.toHexString(hashCode());
	
	// Back to AudioPlug right after airSocket install -  Jude
	public static boolean active = false;
	
	private ListView feedList;
	private FeedListAdapter adapter;
	private Channel channel;
	private ArrayList<FeedElements> feeds;
	private ActionBar mActionbar;	
	private TextView actionTitle;
	private ImageButton btnShare;
//	private ImageButton btnPlayAll;
	private FeedItemLoader feedItemLoader;
	private FrameLayout refreshLayout;
	private IAudioService service;
	private int mCurrentItem; 
		
	private ServiceManager srvManager;
	private MenuItem playMenu;	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_channelhome);
				
		//set actionbar
		mActionbar = getActionBar();
		View actionView = getLayoutInflater().inflate(R.layout.actionbar_layout, null);		 
		mActionbar = getActionBar();		
		mActionbar.setCustomView(actionView, new ActionBar.LayoutParams(
				ActionBar.LayoutParams.MATCH_PARENT,
				ActionBar.LayoutParams.MATCH_PARENT));
		mActionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);		
		actionTitle = (TextView) findViewById(R.id.actionTitle);
		actionTitle.setText(R.string.title_channel_home);
		
		btnShare = (ImageButton) findViewById(R.id.btnShare);
		btnShare.setOnClickListener(this);

		channel = (Channel) getIntent().getSerializableExtra("channel");
		refreshLayout = (FrameLayout) findViewById(R.id.feeds_refresh_layout);
		/*
		btnPlayList = (ImageButton) findViewById(R.id.btnPlayList);
		btnPlayList.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//block playlist
                //startActivity(-1, ConstData.MODE_PLAYLIST);
			}		
		});
		*/
		
		//btnPlayAll = (ImageButton) findViewById(R.id.btnPlayAll);
//		btnPlayAll.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				startActivity(-1, ConstData.MODE_PLAYER);
//			}
//			
//		});
		srvManager = new ServiceManager(this);
		srvManager.start(null);

		init();
	
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
						
		getMenuInflater().inflate(R.menu.actionbar_menu_channelhome, menu);		
		//MenuItem menuItem = menu.findItem(R.id.action_title);
		//menuItem.setTitle("Channel Home");
		
		playMenu = 	menu.findItem(R.id.action_play);
		
		if (srvManager.getService() == null) {
			playMenu.setIcon(R.drawable.ic_action_play_dim);
		}else{
			playMenu.setIcon(R.drawable.ic_action_play);
		}
		return true;		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.action_refresh:						
			if(channel != null){
				refreshLayout.setVisibility(View.VISIBLE);
				adapter.clearFeedList();
				adapter.notifyDataSetChanged();								
				channel.setLoading(true);
				channel.setLoadedText(false);
				channel.setLoadedImage(false);
				feedItemLoader = new FeedItemLoader(channel, ChannelList.getParser(this, channel.getType()), adapter, feeds, refreshLayout);
				Thread refresh = new Thread(feedItemLoader);
				refresh.start();
	
				
			}
			break;
		case R.id.action_play:
			openPlayer(-1);
			break;
//		case R.id.action_overflow:
//			break;	
		}		
		return super.onOptionsItemSelected(item);		
	}
	
	void init(){
	
		//set thumbnail
			ImageView imageView = (ImageView)findViewById(R.id.ChannelInfoThumbnail);
			ImageCache.getInstance(this).getImageLoader().displayImage(channel.getThumbnailUrl(), imageView, ImageCache.getInstance(this).getOption());
		
		//set title, description, link
		RSSFile rss = channel.getRSS();		
		if(channel.isLoadedText()) {	
			setText(R.id.ChannelInfoTitle, channel.getTitle());
			setText(R.id.ChannelInfoDesc, channel.getDescription());
			if(rss != null){
				setText(R.id.ChannelInfoLink, rss.channel.link);
			}
		}

		
		//set feed list
		if(rss !=null){
			feeds = rss.feeds;
			adapter = new FeedListAdapter(this, feeds);
			feedList = (ListView) findViewById(R.id.feedListView);
			feedList.setOnItemClickListener(this);
			feedList.setAdapter(adapter);
		}
		
		
				
	}
	
	// Back to audioplug right after airSocket install -  Jude
		private BroadcastReceiver airSocketInstallReceiver = new BroadcastReceiver(){
			@Override
			public void onReceive(Context context, Intent intent) 
			{
				String action = intent.getAction();
				if(Intent.ACTION_PACKAGE_ADDED.equals(action))
				{
					String packageName = intent.getData().getSchemeSpecificPart();
					if(packageName.equals("com.airplug.mao.agent"))
					{					
						if(active)
						{					
							Intent i = new Intent(ChannelHomeActivity.this, ChannelHomeActivity.class );
						//i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);					
							i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);					
						//i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
							startActivity(i);
						}
						else
						{ //do nothing					
							return;
						}					
					}
				}
			}			
		};
	
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.d("jude","ChannelHomeActivity onResume");
		
		// Back to audioplug right after airSocket install -  Jude
		active = true;	
		IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
		filter.addDataScheme("package");		
		registerReceiver(airSocketInstallReceiver,filter);
		
		if(playMenu != null){
			if (srvManager.getService() == null) {
				playMenu.setIcon(R.drawable.ic_action_play_dim);
			}else{
				playMenu.setIcon(R.drawable.ic_action_play);
			}
		}
		mCurrentItem = srvManager.getCurrentPosition();
	}

	protected void onPause() {
		super.onPause();
		srvManager.unbind(this);
	}
	
	@Override
	protected void onDestroy() {
		
		super.onDestroy();
						
		// Back to audioplug right after airSocket install -  Jude
		active = false;
		try{
			unregisterReceiver(airSocketInstallReceiver);			
		} catch(IllegalArgumentException e){
			e.printStackTrace();
		}
		
		if(feedList != null) feedList.setAdapter(null);
	}

	@Override
	protected void onStart() {
		
		super.onStart();
				
		if(adapter != null) {
			adapter.resume();
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					
		//if(adapter != null) adapter.pause();
		if(adapter != null){
			adapter.notifyDataSetChanged();			
		}
		//startActivity(position);
		openPlayer(position);
	}
	
//	private void startActivity(int itemId, int mode) {
//		if(feedItemLoader != null){
//			//in case of refresh, get feeds from FeedItemLoader
//			feeds = feedItemLoader.getFeeds();
//		}
//		
//		ArrayList<AudioFile> entries = new ArrayList<AudioFile>();		
//		
//		//In case of Play List
//		/*
//		if(itemId == -1 && mode == ConstData.MODE_PLAYLIST){
//			for(FeedElements item: feeds) {
//				if(item.enclosure_type == null ||
//					item.enclosure_type.contains("video") == false) {
//					entries.add(new AudioFile(item, channel.getThumbnailUrl(), channel.getTitle(), itemId, State.IDLE));
//				}
//			}
//
//		}
//		else{	
//			if(itemId == -1)
//			{				
//				itemId = 0;				
//			}
//		}
//		*/
//		
//		for(FeedElements item: feeds) {
//			if(item.enclosure_type == null ||
//				item.enclosure_type.contains("video") == false) {
//				entries.add(new AudioFile(item, channel.getThumbnailUrl(), channel.getTitle(), itemId, State.IDLE));
//			}
//		}		
//		savePlaylist(entries);
//		Intent intent = new Intent(getApplicationContext(), PlayerActivity.class);
//		intent.putExtra("entries", entries);		
//		intent.putExtra("channel", channel);
//		intent.putExtra("mode", mode);
//		intent.putExtra("index", itemId);
//		Log.d("jude","channelhome start PlayerMain with index : " + itemId);
//		startActivity(intent);
//		
//	}

	private void savePlaylist(ArrayList<AudioFile> feeds){
		
		SharedPreferences prefs = getSharedPreferences(ConstData.CurrentPlayPrefs, MODE_PRIVATE);
		SharedPreferences.Editor ed = prefs.edit();
		ObjectPreference.witeObjectToFile(this, feeds, "playlist");
	}
	private void setText(int id, String text) {
		TextView textView = (TextView)findViewById(id);
		if(text != null) {
			textView.setText(text);
		} else {
			textView.setVisibility(View.GONE);
		}
	}
	
	private void openPlayer(int position) {
		if(position <0){  			//First play
			service = srvManager.getService();
			if (service!=null) {					
				Intent intent = new Intent(this, PlayerActivity.class);
				intent.putExtra("entries", service.getEntries());				
				intent.putExtra("index", service.getIndex());				
				intent.putExtra("channel", service.getChannel());			
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);						
			}	
			else {		
				Toast.makeText(this, "There is no playing list", Toast.LENGTH_SHORT).show();
			}
		}
		
		else //Not first play
		{				
			if(feedItemLoader != null){
				//in case of refresh, get feeds from FeedItemLoader
				feeds = feedItemLoader.getFeeds();
			}
			
			ArrayList<AudioFile> entries = new ArrayList<AudioFile>();
			
			for(FeedElements item: feeds) {
				if(item.enclosure_type == null ||
					item.enclosure_type.contains("video") == false) {
					entries.add(new AudioFile(item, channel.getThumbnailUrl(), channel.getTitle(), position, State.IDLE));
				}
			}
						
			savePlaylist(entries);
			Intent intent = new Intent(getApplicationContext(), PlayerActivity.class);
			intent.putExtra("entries", entries);		
			intent.putExtra("channel", channel);			
			intent.putExtra("index", position);
			Log.d("jude","channelhome start PlayerMain with index : " + position);			
			startActivity(intent);			
		}
			
	}
	
//	private void playOrStop() {
//		
//		Log.i("jude","play or stop___service : " + srvManager.getService());		
//		//pause 상태이면 isPlaying() 값은 false로 옴
//		if (srvManager.isPlaying()) {
//			srvManager.pause();
//			if(srvManager.getService() != null){
//				currentPosition = srvManager.getService().getCurrentPosition();
//			}
//			playMenu.setIcon(R.drawable.ic_action_play);
//
//		} 
//		else {
//			if (srvManager.getService() == null) {
//				Toast.makeText(this, "There is no playing list", Toast.LENGTH_SHORT).show();
////				srvManager.start(null);
////				srvManager.sendCommand(null);
//				
//			} 
//			else {
//				currentPosition = srvManager.getService().getCurrentPosition();
//				playMenu.setIcon(R.drawable.ic_action_pause);
//				srvManager.seekTo(currentPosition);
//				srvManager.play();
//			}
//		}		
//	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.btnShare:
			Intent intent = new Intent(this, ShareActivity.class);
			intent.putExtra("channel", channel);
			intent.putExtra("content", channel.getTitle());
			startActivity(intent);
			break;
		}			
	}
	
}
