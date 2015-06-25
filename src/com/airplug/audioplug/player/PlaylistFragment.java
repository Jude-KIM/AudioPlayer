package com.airplug.audioplug.player;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.airplug.audioplug.channellist.Channel;
import com.airplug.audioplug.channellist.RSSFile;
import com.airplug.audioplug.channellist.RSSFile.FeedElements;
import com.airplug.audioplug.dev.R;

//public class PlaylistFragment extends ListFragment implements OnItemClickListener{
public class PlaylistFragment extends ListFragment {

	private static final String NAME = "PlayListFragment";
	private static final String CLASS = "PlaylistFragment";
    public ArrayList<FeedElements> feedsList = new ArrayList<FeedElements>();
    private static ArrayList<AudioFile> entries;
    private ListView listView;
    private static Channel channel;
    private PlaylistAdapter adapter;    
    private TextView playingFeedTitle;
    private TextView playingChannelTitle;
    private ImageButton btnNowPlaying;    
    private Activity activity;
    private Context context;
    private static int currentPosition;
    private PlaylistListener listener = null;
    private int mSelectedItem;
    static private final String STATE_CHECKED="CHECKED";
	
    public PlaylistFragment(Context context){
    	this.context = context;    	
    }
    
    public PlaylistFragment(){
    	//do nothing;
    }
        
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        activity = getActivity();
    }
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
    	 
    	Log.d(CLASS,"jude " + "onCreateView");
    	View view = inflater.inflate(R.layout.fragment_playlist, container, false);
    	channel = (Channel) activity.getIntent().getSerializableExtra("channel");
        currentPosition = activity.getIntent().getIntExtra("index", -1);
        entries = (ArrayList<AudioFile>) activity.getIntent().getSerializableExtra("entries");     	
                     
        playingFeedTitle = (TextView) view.findViewById(R.id.playingFeedTitle);
        playingChannelTitle = (TextView) view.findViewById(R.id.playingChannelTitle);
        btnNowPlaying = (ImageButton) view.findViewById(R.id.btnNowPlaying);
        RSSFile rss = channel.getRSS();
        feedsList = rss.feeds;
           
         //set current channel title and feed title
        try
        {
         	playingChannelTitle.setText(channel.getTitle());
         	playingFeedTitle.setText(entries.get(currentPosition).title);        
     	}catch(Throwable e){
     		e.printStackTrace();			
     	}
        
        Log.d(CLASS,"jude " + "end of onCreateView");
         
        btnNowPlaying.setOnClickListener(new View.OnClickListener(){
        	
        	@Override
 			public void onClick(View v) {
 				// TODO Auto-generated method stub
 							
 			}        	
         });
    	
		return view;
        	
        }    

	@Override
	public void onActivityCreated(Bundle state) {
		// TODO Auto-generated method stub
		super.onActivityCreated(state);		
		
        setListAdapter(new PlaylistAdapter(context, feedsList));
        
        if(state!=null){
        	int position = state.getInt(STATE_CHECKED, - 1);
        	if(position>-1){
        		getListView().setItemChecked(position, true);
        	    //if(currentPosition > -1){
        			getListView().setItemsCanFocus(false);	        
        			getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        			getListView().setItemChecked(currentPosition, true);
        	    //}
        	}
        }	    
	}
	
	public void enablePersistentSelection() {
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	}
	

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		l.setItemChecked(position,true);
		
		mSelectedItem = position;
		Log.e(CLASS,"jude1 position/mSeletedItem" + position + "/" + mSelectedItem);
		
		//if(adapter != null) adapter.pause();		
		playingFeedTitle.setText(feedsList.get(position).title);
		
		Intent i = new Intent(activity, PlayerActivity.class);
		i.putExtra("index", position);
		
		if(listener != null){
			listener.onPlaylistSelected(i);
		}	

	}
	
	  @Override
	  public void onSaveInstanceState(Bundle state) {
	    super.onSaveInstanceState(state);
	    
	    state.putInt(STATE_CHECKED,
	                  getListView().getCheckedItemPosition());
	  }
	
    
    public void setPlaylistListener(PlaylistListener listener) {
        this.listener = listener;
      }
    
    
    public class PlaylistAdapter extends BaseAdapter {

    	private static final String CLASS = "PlaylistAdapter";	
    	private final LayoutInflater inflater;
    	private ArrayList<FeedElements> feeds;	
    	private volatile boolean paused;
    	private LinearLayout controller;
    	
    	public PlaylistAdapter(Context context, ArrayList<FeedElements> feeds){
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
    		// TODO Auto-generated method stub
    		return position;
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
    			view = inflater.inflate(R.layout.playlist_item, null);
    		}
    		
    		FeedElements feed_item = feeds.get(position);		
    		//controller = (LinearLayout) view.findViewById(R.id.playListController);
    		Log.d(CLASS,"test " + "getView(" + position + ")");
    		
    		// set title and date
    		setText(view, R.id.listItemTitle, feed_item.title);
    		if(position == currentPosition){
    			Log.d(CLASS,"jude2 position/currentPosition" + position + "/" + currentPosition);
    		}
//    		if(position == mSelectedItem){
//    			Log.d(CLASS,"jude2 position/mSeletedItem" + position + "/" + mSelectedItem);
//    			showController(true);
//    		}
//    		else{
//    			showController(false);
//    		}
    		
    		
//    		Log.d(CLASS,"jude " + view.isSelected());
//    		if(view.isSelected())
//    		{
//    			view.setBackgroundColor(R.color.mint);
//    		}

    		// set play status image
//    		if(thumbnail != null) {
//    			ImageView imageView = (ImageView)view.findViewById(R.id.FeedItemStatus);
//    			imageView.setImageBitmap(
//    					DisplayUtil.decodeSampledBitmapFromFile(thumbnail,
//    							MainActivity.pixel, MainActivity.pixel));
//    		}
    		
    		return view;
    	}
    	
    	private void showController(boolean b) {
    		// TODO Auto-generated method stub
    		
    		if(b)
    			controller.setVisibility(View.VISIBLE);
    		else
    			controller.setVisibility(View.GONE);
    			
    	}

    	private void setText(View view, int id, String text) {
    		if(text != null) {
    			TextView textView = (TextView)view.findViewById(id);
    			textView.setText(text);
    		}
    	}

    }


}
