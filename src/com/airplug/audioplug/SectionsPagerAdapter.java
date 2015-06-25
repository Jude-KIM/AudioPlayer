package com.airplug.audioplug;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;

import com.airplug.audioplug.channellist.ChannelListFragment;
import com.airplug.audioplug.dev.R;
import com.airplug.audioplug.settings.SettingsFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
	Context mContext;
	FragmentManager fm;
	private SettingsFragment frag0;
	private ChannelListFragment frag1;
	//private MyChannelFragment frag2;
	
	public SectionsPagerAdapter(Context context, FragmentManager fm) {		
		super(fm);
		mContext = context;
		this.fm = fm;
		frag0 = new SettingsFragment().setContext(context);
		frag1 = new ChannelListFragment(context);
		//frag2 = new MyChannelFragment(context);
		
		// TODO Auto-generated constructor stub
	}

	
	public SettingsFragment getSettingsFragment(){		
		return frag0;
	}
	
	public ChannelListFragment getChannelListFragment(){		
		return frag1;
	}
	
//	
//	public MyChannelFragment getMyChannelFragment(){		
//		return frag2;
//	}
	

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub		
		switch(position) {
        case 0:
        	return frag0;
        case 1:
            return frag1;
//        case 2:
//            return frag2;
        }
        return null;
        		
	}

	
	//전체 View 개수를 결정
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 2;
	}
	
	//각 Page의 Title정의
	public CharSequence getPageTitle(int position) {
        // TODO Auto-generated method stub
        switch (position) {
        case 0:
            return mContext.getString(R.string.title_fragment1).toUpperCase();
        case 1:
            return mContext.getString(R.string.title_fragment2).toUpperCase();
//        case 2:
//            return mContext.getString(R.string.title_fragment3).toUpperCase();
        }
        return null;
    }


}
