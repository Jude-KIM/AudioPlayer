package com.airplug.audioplug.share;


import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.airplug.audioplug.data.ConstData;
import com.airplug.audioplug.data.ConstData.Extra;

public class TwitterBase {

	private static TwitterBase inst_;
	public static TwitterBase getInst(Context context) {
		synchronized (TwitterBase.class) {
			if (inst_ == null) {
				inst_ = new TwitterBase(context);
				if (inst_.isLogined() == true) {
					inst_.login();
				}
			}
		}
		return inst_;
	}

	private Twitter mTwitter;
	private Context mContext;
	private AccessToken mAccessToken;
	private RequestToken mRequestToken;
	private TwitterEventObserver twitterEventObserver;
	private String TAG = "Twitter";
	
	public void setTwitterEventObserver(TwitterEventObserver twitterEventObserver) 
	{
		this.twitterEventObserver = twitterEventObserver;
	}

	public TwitterBase(final Context context) {
		mContext = context;
	}

	public void login() {
		class loginTask extends AsyncTask<Void, Void, RequestToken> 
		{

			@Override
			protected RequestToken doInBackground(Void... params) 
			{
				try 
				{
			//String accessToken = TwitterUtil.getAppPreferences(mContext, TwitterConst.TWITTER_ACCESS_TOKEN);
			//String accessTokenSecret = TwitterUtil.getAppPreferences(mContext, TwitterConst.TWITTER_ACCESS_TOKEN_SECRET);
					String accessToken = TwitterUtil.getAppPreferences(mContext, ConstData.TWITTER_ACCESS_TOKEN);
					String accessTokenSecret = TwitterUtil.getAppPreferences(mContext, ConstData.TWITTER_ACCESS_TOKEN_SECRET);
									    
				    Log.i("Twitter","jude //  accessToken : " + accessToken);
				    Log.i("Twitter","jude //  accessTokenSecret : " + accessTokenSecret);
					if (accessToken != null && !"".equals(accessToken)&& accessTokenSecret != null&& !"".equals(accessTokenSecret)) 
					{	
											
					    //mTwitter = tf.getInstance();
					
						mTwitter = TwitterFactory.getSingleton();
						mTwitter.setOAuthConsumer(ConstData.TWITTER_CONSUMER_KEY, ConstData.TWITTER_CONSUMER_SECRET);						
						mAccessToken = mTwitter.getOAuthAccessToken();
						
						/*
				mAccessToken = new AccessToken(accessToken, accessTokenSecret);
				TwitterFactory factory = new TwitterFactory();
				mTwitter = factory.getOAuthAuthorizedInstance(TwitterConst.TWITTER_CONSUMER_KEY, TwitterConst.TWITTER_CONSUMER_SECRET);
				mTwitter.setOAuthAccessToken(accessToken, accessTokenSecret);
						*/
//				mTwitter = new twitter4j.Twitter(screenName, password) 
						return null;
					} else 
					{
						/*
						ConfigurationBuilder cb = new ConfigurationBuilder();
					    cb.setDebugEnabled(true)
					            .setOAuthConsumerKey(ConstData.TWITTER_CONSUMER_KEY)
					            .setOAuthConsumerSecret(ConstData.TWITTER_CONSUMER_SECRET)
					            .setOAuthAccessToken(ConstData.TWITTER_ACCESS_TOKEN)
					            .setOAuthAccessTokenSecret(ConstData.TWITTER_ACCESS_TOKEN_SECRET);
					    TwitterFactory tf = new TwitterFactory(cb.build());
					    */						
						//mTwitter = tf.getInstance();
						
						mTwitter = new TwitterFactory().getInstance();
						mTwitter.setOAuthConsumer(ConstData.TWITTER_CONSUMER_KEY, ConstData.TWITTER_CONSUMER_SECRET);
						
						try
						{
							mRequestToken = mTwitter.getOAuthRequestToken();
							Log.i("Twitter","jude //  mRequesttoken : " + mRequestToken);
							
						}catch(TwitterException e)
						{
							e.printStackTrace();
						}
						/*
				TwitterFactory factory = new TwitterFactory();
				mTwitter = factory.getOAuthAuthorizedInstance(TwitterConst.TWITTER_CONSUMER_KEY, TwitterConst.TWITTER_CONSUMER_SECRET);
				mRequestToken = mTwitter.getOAuthRequestToken(TwitterConst.TWITTER_CALLBACK_URL);
						*/	
						return mRequestToken;
					}
				} catch (Exception e) 
				{
					e.printStackTrace();
				}
				
				return null;
			}
			
			@Override
			public void onPostExecute(RequestToken result) {
				if (result != null) {
				Intent intent = new Intent(mContext, TwitterLogin.class);
				intent.putExtra("auth_url", mRequestToken.getAuthorizationURL());
				((Activity)mContext).startActivityForResult(intent, ConstData.LOGIN_CODE);
			}
		}
	}
		loginTask task = new loginTask();
		task.execute();
	}
	
	public void write(String body) {
		
		//body = body.replace("#shortenurl#", string2 != null ? string2 : "");
		try {			
			//Log.i("Twitter","jude" + " mTwitter Auth1 : " + mTwitter.getOAuthAccessToken() );			
			mTwitter.updateStatus(body);
			if(twitterEventObserver != null){
				twitterEventObserver.onWriteComplete();
			}
		} catch (Exception e) {
			Log.e("Twitter","jude" + " exception wirte : e :" + e.getMessage());
			if(twitterEventObserver != null){
				twitterEventObserver.onWriteFail();
			}
			e.printStackTrace();
		}
	}

	public void logout() {
		if(isLogined()){
			TwitterUtil.setAppPreferences(mContext, ConstData.TWITTER_ACCESS_TOKEN, "");
			TwitterUtil.setAppPreferences(mContext, ConstData.TWITTER_ACCESS_TOKEN_SECRET, "");
		}
	}
	
	public static boolean isLogined(Context context) {
		String token = TwitterUtil.getAppPreferences(context, ConstData.TWITTER_ACCESS_TOKEN);
		String tokenSecret = TwitterUtil.getAppPreferences(context, ConstData.TWITTER_ACCESS_TOKEN_SECRET);
		if(token.equals("") || tokenSecret.equals("")){
			return false;
		}
		return true;
	}
	
	public boolean isLogined(){
		String token = TwitterUtil.getAppPreferences(mContext, ConstData.TWITTER_ACCESS_TOKEN);
		String tokenSecret = TwitterUtil.getAppPreferences(mContext, ConstData.TWITTER_ACCESS_TOKEN_SECRET);
		if(token.equals("") || tokenSecret.equals("")){
			return false;
		}else{
			return true;
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i("Twitter","jude" + " onActivityResult code : " + resultCode);
		Log.i("Twitter","jude" + " onActivityResutl request code : " + requestCode);
				
		if (resultCode == Activity.RESULT_OK) {
			 if( requestCode == ConstData.LOGIN_CODE){
				 if(twitterEventObserver != null){
					 twitterEventObserver.onLoginComplete();
				 }
				try {
					mAccessToken = mTwitter.getOAuthAccessToken(mRequestToken, data.getStringExtra("oauth_verifier"));
					TwitterUtil.setAppPreferences(mContext, ConstData.TWITTER_ACCESS_TOKEN, mAccessToken.getToken());
					TwitterUtil.setAppPreferences(mContext, ConstData.TWITTER_ACCESS_TOKEN_SECRET, mAccessToken.getTokenSecret());
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
	}
	
	
	
	public void completeLogin(Intent data) {
		/*
		try {
			mAccessToken = mTwitter.getOAuthAccessToken(mRequestToken, data.getStringExtra("oauth_verifier"));
			TwitterUtil.setAppPreferences(mContext, TwitterConst.TWITTER_ACCESS_TOKEN, mAccessToken.getToken());
			TwitterUtil.setAppPreferences(mContext, TwitterConst.TWITTER_ACCESS_TOKEN_SECRET, mAccessToken.getTokenSecret());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(OnLoginCompleteListener != null){
			 OnLoginCompleteListener.onLoginComplete();
		}
		*/
		
		Log.i("Twitter","completeLogin()");
		CompleteLoginTask task = new CompleteLoginTask(data);
		task.execute();
	}
	
	class CompleteLoginTask extends AsyncTask<Void, Void, Void> {
		private Intent data;
		public CompleteLoginTask(Intent data) {
			this.data = data;
		}
		
		@Override
		protected Void doInBackground(Void... params) {
		try {
			Log.i("Twitter","completeLoginTask doInBackground");
			mAccessToken = mTwitter.getOAuthAccessToken(mRequestToken, data.getStringExtra("oauth_verifier"));
			TwitterUtil.setAppPreferences(mContext, ConstData.TWITTER_ACCESS_TOKEN, mAccessToken.getToken());
			TwitterUtil.setAppPreferences(mContext, ConstData.TWITTER_ACCESS_TOKEN_SECRET, mAccessToken.getTokenSecret());
		} catch (Exception e) {
			e.printStackTrace();
		}
			return null;
		}
		
		@Override
		public void onPostExecute(Void result) {
			super.onPostExecute(result);
			Log.i("Twitter","completeLoginTask onpostExecute");
		if(twitterEventObserver != null){
			Log.i("Twitter","completeLoginTask onpostExecute onLoginComplete");
			twitterEventObserver.onLoginComplete();
		}
	}
		
		
	}
	
}
