package com.airplug.audioplug;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.airplug.audioplug.channelhome.ChannelHomeActivity;
import com.airplug.audioplug.dev.R;
import com.airplug.audioplug.player.IAudioService;
import com.airplug.audioplug.player.PlayerActivity;
import com.airplug.audioplug.player.ServiceManager;
import com.airplug.audioplug.settings.SettingsFragment;
import com.airplug.audioplug.util.DisplayUtil;
import com.airplug.audioplug.util.Util;


public class MainActivity extends FragmentActivity {

	private static final String NAME = "ActivityMain";
	private final String CLASS = NAME + "@" + Integer.toHexString(hashCode());
	
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	private SectionsPagerAdapter mSectionsPagerAdapter;	
	private ActionBar mActionBar;
	private TextView actionTitle;	
	private IAudioService service;
	private boolean backPressed = false;	
	
	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	public static Context appContext;
	
	public static int pixel;
	private ServiceManager srvManager;	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
				
		setContentView(R.layout.activity_main);
		appContext = this;

		pixel = DisplayUtil.PixelFromDP(this, 160);
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(this, getFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setCurrentItem(1);
		
		//set actionbar custom view
		View actionView = getLayoutInflater().inflate(R.layout.actionbar_layout, null);		 
		mActionBar = getActionBar();		
		mActionBar.setCustomView(actionView, new ActionBar.LayoutParams(
				ActionBar.LayoutParams.MATCH_PARENT,
				ActionBar.LayoutParams.MATCH_PARENT));		
		mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		
		actionTitle = (TextView) findViewById(R.id.actionTitle);
		actionTitle.setText(mSectionsPagerAdapter.getPageTitle(1));
		
				
		
		//Util.startActivity(LoadingActivity.class, this, true);
		
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {			
				@Override
				public void onPageSelected(int position) {
				Log.d("jude","onpageselected");				
				actionTitle.setText(mSectionsPagerAdapter.getPageTitle(position));				
				}

				@Override
				public void onPageScrollStateChanged(int arg0) {
					// TODO Auto-generated method stub
				
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					// TODO Auto-generated method stub					
				}
		});
		
		srvManager = new ServiceManager(this);
		srvManager.start(null);

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
						//set switch on
						Preferences.setAgentEnabled(context, true);
						//SettingsFragment frag = (SettingsFragment) getFragmentManager().findFragmentById(R.id.settingAirSocketPermSwitch);
						SettingsFragment sf = mSectionsPagerAdapter.getSettingsFragment();
//						CheckBox cb = (CheckBox) sf.getView().findViewById(R.id.settingAirSocketPermSwitch);
//						cb.setChecked(true);
						if(ChannelHomeActivity.active){
							// do nothing
							return;
						}
						else
						{
							Log.d("jude","Airsocket installed! Start MainActivity");
							Intent i = new Intent(MainActivity.this, MainActivity.class );				
							i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);					
							startActivity(i);
						}
						
					}
				}
			}
			
		};
		private int currentPosition;
		private Object menuInflater;
		private MenuItem playMenu;
	

	protected void onPause() {
		super.onPause();
		srvManager.unbind(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		Log.d("jude","agent setting : " + Preferences.isAgentEnabled(this));
		
		// Back to AudioPlug right after airSocket install -  Jude
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
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
				
		Log.d("jude","srvManager.isPlaying() : " + srvManager.isPlaying());
		if(srvManager.getService() != null){
			if(srvManager.isPlaying() == false){
				//srvManager.stopAudioService();
			}
		}		
		
		// Back to AudioPlug right after airSocket install -  Jude
		try{
			unregisterReceiver(airSocketInstallReceiver);			
		} catch(IllegalArgumentException e){
			
		}
		try {
			Util.recursiveRecycle(getWindow().getDecorView());
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.gc();
	}

	@Override
	protected void onStart() {
		super.onStart();
	

	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		
		getMenuInflater().inflate(R.menu.actionbar_menu_main, menu);		
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
		case R.id.action_play:
			openPlayer();
			break;
		//case R.id.action_overflow:
			//break;
		}
		
		return super.onOptionsItemSelected(item);
		
	}

	private void openPlayer() {		

		Log.d("jude","MainActivity  service : " + service);
		Log.d("jude","MainActivity  srvManager : " + srvManager);
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
	
//	@Override
//	public void onUserInteraction() {
//		APLog.d(CLASS, "onUserInteraction " + backPressed);
//		super.onUserInteraction();		
//		backPressed = false;		
//	}
//
//	@Override
//	protected void onUserLeaveHint() {
//		APLog.d(CLASS, "onUserLeaveHint " + backPressed);
//		super.onUserLeaveHint();		
//		backPressed = false;		
//	}
	
	
	@Override
	public void onBackPressed(){
		
		if(backPressed){
			super.onBackPressed();
		}
		else{
			backPressed = true;
			Toast.makeText(getApplicationContext(),
					"Press BACK key again to quit", Toast.LENGTH_SHORT).show();
		}
	}
}