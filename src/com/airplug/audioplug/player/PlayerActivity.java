/*
 *Copyright linuxs by Airplug.,
 *All rights reserved.
 *Airplug.com
*/
package com.airplug.audioplug.player;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;

import com.airplug.audioplug.dev.R;
import com.airplug.audioplug.player.AudioService.IServiceListener;

public class PlayerActivity extends FragmentActivity implements IServiceListener, OnClickListener, PlaylistListener {

	private static final String NAME = "PlayerActivity";
	private final String CLASS = NAME + "@" + Integer.toHexString(hashCode());
	
			
	private ServiceManager srvManager;	
	private PlayerPagerAdapter mPlayerPagerAdapter;	
	private ViewPager mViewPager;
	private PlayerFragment fPlayer;
	private PlaylistFragment fPlaylist;
	private RadioGroup pageIndicator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.d(CLASS, "onCreate");
		
		setContentView(R.layout.activity_player);
		
		
		srvManager = new ServiceManager(this);		
		pageIndicator = (RadioGroup) findViewById(R.id.player_paging_indicator);		
		mPlayerPagerAdapter = new PlayerPagerAdapter(this, getFragmentManager());
		fPlayer = mPlayerPagerAdapter.getPlayerFragment();		
		fPlaylist = mPlayerPagerAdapter.getPlaylistFragment();
		fPlaylist.setPlaylistListener(this);
		
		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.playerPager);
		mViewPager.setAdapter(mPlayerPagerAdapter);
		mViewPager.setCurrentItem(0);
		pageIndicator.check(R.id.page1);
		
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
		
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				if(arg0 == 0)
					pageIndicator.check(R.id.page1);				
				else
					pageIndicator.check(R.id.page2);
				
				
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
				
	}	
	
	public void setCurrentFragment(int id){
		mViewPager.setCurrentItem(id);
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.e("jude","PlayerActivity onPause");
		srvManager.unbind(this);		
	}

	@Override
	protected void onResume() {
		Log.i("jude","PlayerActivity onResume");
		super.onResume();		
		//service = srvManager.getService();		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.e("position","PlayerActivity onDestroy");
			
		if(srvManager!=null)
			srvManager.unbind(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.i("jude","PlayerActivity onStart");	
				

		//getMainHandler().postDelayed(runnable, 1000);
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.i("jude","PlayerActivity onStop");
		//getMainHandler().removeCallbacks(runnable);
	}
	
	

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return NAME;
	}


	@Override
	public void onAudioEntryInfo(AudioFile entry) {
		// TODO Auto-generated method stub
		fPlayer.onAudioEntryInfo(entry);
	}
	


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onPlaylistSelected(Intent i){
		Log.i(CLASS,"jude " + "onPlaylistSelected intent : " + i);
		if(i!=null && fPlayer != null){
			fPlayer.setEntryFromPlaylist(i.getExtras().getInt("index"));
			mViewPager.setCurrentItem(0);
		}
		
	}
	
	public class PlayerPagerAdapter extends FragmentPagerAdapter {
		Context mContext;
		FragmentManager fm;
		
		public PlayerPagerAdapter(Context context, FragmentManager fm) {			
			super(fm);
			Log.i("jude","PlayerPagerAdapter");
			mContext = context;
			this.fm = fm;
			fPlayer = PlayerFragment.newInstance(context);//new PlayerFragment(context); -- brown
			fPlaylist = new PlaylistFragment(context);			
		}

		
		public PlayerFragment getPlayerFragment(){		
			return fPlayer;
		}
		
		public PlaylistFragment getPlaylistFragment(){		
			return fPlaylist;
		}
	

		@Override
		public Fragment getItem(int position) {
			// TODO Auto-generated method stub		
			switch(position) {
	        case 0:
	        	return fPlayer;
	        case 1:
	            return fPlaylist;
//	        case 2:
//	            return frag2;
	        }
	        return null;
	        		
		}
		
		//��ü View ������ ����
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 2;
		}
		
		//�� Page�� Title����
		public CharSequence getPageTitle(int position) {
	        // TODO Auto-generated method stub
	        switch (position) {
	        case 0:
	            return mContext.getString(R.string.title_fragment1).toUpperCase();
	        case 1:
	            return "Play List";
//	        case 2:
//	            return mContext.getString(R.string.title_fragment3).toUpperCase();
	        }
	        return null;
	    }

	}
	

}
