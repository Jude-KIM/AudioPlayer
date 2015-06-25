package com.airplug.audioplug.share;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airplug.audioplug.channellist.Channel;
import com.airplug.audioplug.data.ConstData.Extra;
import com.airplug.audioplug.dev.R;
import com.airplug.audioplug.util.Util;

public class ShareActivity extends Activity implements OnClickListener {

	private static final String NAME = "ShareActivity";
	private final String CLASS = NAME + "@" + Integer.toHexString(hashCode());
	
	Context mContext = null;
	ImageView btnShareFacebook = null;
	ImageView btnShareKakao = null;
	ImageView btnShareTwitter = null;
	Button btnShareClose = null;	
	
	private String link;
	private String contentTitle;
	private String linkDescription;
	private String picture;
	private Channel channel;
	
	static TextView tv = null;	
	SharedPreferences pref;	
	TwitterBase twitter = null;
	
	ArrayList<Map<String, String>> metaInfoArray;
	Map<String, String> metaInfo;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        
        mContext = this;
        channel = (Channel) getIntent().getSerializableExtra("channel");
        if(channel != null){
        	link = channel.getUrl();        	
        	linkDescription = channel.getDescription();
        	picture = channel.getThumbnailUrl();
        }
        contentTitle = getIntent().getStringExtra("content");
        
        
        initialize();		
	}
	
	public void initialize()
	{
		btnShareFacebook = (ImageView)findViewById(R.id.btnShareFacebook);
		btnShareKakao = (ImageView)findViewById(R.id.btnShareKakao);
		btnShareTwitter = (ImageView)findViewById(R.id.btnShareTwitter);
		btnShareClose = (Button)findViewById(R.id.btnShareClose);
		
		btnShareFacebook.setOnClickListener(this);
		btnShareKakao.setOnClickListener(this);
		btnShareTwitter.setOnClickListener(this);
		btnShareClose.setOnClickListener(this);
	
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId()){
		
		case R.id.btnShareClose:
			finish();
			break;
			
		case R.id.btnShareFacebook:
			startFacebookActivity();
			break;
			
		case R.id.btnShareKakao:
			try {
				startKakao();
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case R.id.btnShareTwitter:
			startTwitterActivity();
			break;
			
		}
	}
	
	
	private void startKakao() throws NameNotFoundException {
		// TODO Auto-generated method stub
	
		String message = contentTitle;
		
		metaInfoArray = new ArrayList<Map<String, String>>();
		metaInfo = new Hashtable<String, String>(1);
		metaInfo.put("os", "android");
		metaInfo.put("devicetype", "phone");
		metaInfo.put("installurl", "market://details?id=com.kakao.talk");
		metaInfo.put("executeurl", "kakaoLinkTest://startActivity");

		// add to array
		metaInfoArray.add(metaInfo);	

		KakaoLink kakaoLink = KakaoLink.getLink(getApplicationContext());
		
		if(!kakaoLink.isAvailableIntent()){
			Util.showDialog(ShareActivity.this, getString(R.string.no_install));
			return;
		}
		
		kakaoLink.openKakaoAppLink(this, link, message + "  " + link, 
				getPackageName(), 
				getPackageManager().getPackageInfo(getPackageName(), 0).versionName,
				"AudioPlug",
				"UTF-8",
				metaInfoArray);	
			
		

//		popupWindow.dismiss();
//		EntryItem item = Data.getSelectEntryItem();
//		String message = String.format("%s", item.getTitle());
	}


	private void startTwitterActivity() {
		// TODO Auto-generated method stub		
		
		twitter = TwitterBase.getInst(this);
		twitter.setTwitterEventObserver(onTwitterEventObserver_);
		
		if(TwitterBase.isLogined(ShareActivity.this)){
			
			Intent intent = new Intent(this, TwitterActivity.class);
			String message = contentTitle + " " + link + "\n" + "from AudioPlug";
			intent.putExtra(Extra.POST_MESSAGE, message);		
			intent.putExtra(Extra.POST_LINK, link);
			intent.putExtra(Extra.POST_CONTENT_TITLE, contentTitle);
			intent.putExtra(Extra.POST_LINK_DESCRIPTION, linkDescription);
			startActivity(intent);
		}
		else{			
			Util.showProgressDialog(this, "wait");
			new Handler().postDelayed(new com.airplug.audioplug.util.VPRunnable() {

				@Override
				public void runs() {
					twitter.login();
					Util.hideProgressDialog();				
				}
			}, 100);
			
		}
		
		
	}
	

	private void startFacebookActivity() {
		Intent intent = new Intent(this, FacebookActivity.class);
		intent.putExtra(Extra.POST_MESSAGE, "jude test");
		intent.putExtra(Extra.POST_LINK, link);
		intent.putExtra(Extra.POST_CONTENT_TITLE, contentTitle);
		intent.putExtra(Extra.POST_LINK_DESCRIPTION, linkDescription);
		intent.putExtra(Extra.POST_PICTURE, picture);
		startActivity(intent);		
		
	}
	
	private TwitterEventObserver onTwitterEventObserver_ = new TwitterEventObserver() {		
		@Override
		public void onLoginComplete() {
//			isTryLogin_ = false;
//			twitterSwitch_.setChecked(true);
			Log.i("Twitter", "twitter_ onLoginComplete////////////  go to twitterActivity");
			
			if(twitter.isLogined()){
				Intent intent = new Intent(mContext, TwitterActivity.class);
				String message = contentTitle + " " + link + "\n" + "from AudioPlug";
				intent.putExtra(Extra.POST_MESSAGE, message);		
				startActivity(intent);
			}
			
		}

		@Override
		public void onWriteComplete() {
			//isTryLogin_ = false;			
			Toast t = new Toast(mContext); 
			t = Toast.makeText(mContext, getString(R.string.twitter_post_published), Toast.LENGTH_SHORT);
			t.show();
		}

		@Override
		public void onWriteFail() {
			//isTryLogin_ = false;
			Toast t = new Toast(mContext); 
			t = Toast.makeText(mContext, getString(R.string.twitter_post_publishing_failed), Toast.LENGTH_SHORT);
			t.show();
		}
	};


}
