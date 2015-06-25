package com.airplug.audioplug.share;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.airplug.audioplug.data.ConstData;
import com.airplug.audioplug.dev.R;
import com.airplug.audioplug.util.VPLog;

public class TwitterLogin extends Activity {
	private Intent mIntent;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.twitter_login);
		WebView webView = (WebView) findViewById(R.id.webView);

		webView.setWebViewClient(new WebViewClient() {
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);

				if (url != null && url.equals("http://mobile.twitter.com/")) {
					Log.i("Twitter", "Login OK1");
					finish();
				}
				else if (url != null && url.startsWith(ConstData.TWITTER_CALLBACK_URL)) {
					String[] urlParameters = url.split("\\?")[1].split("&");
					String oauthToken = "";
					String oauthVerifier = "";

					try {
						if (urlParameters[0].startsWith("oauth_token")) {
							oauthToken = urlParameters[0].split("=")[1];
						} else if (urlParameters[1].startsWith("oauth_token")) {
							oauthToken = urlParameters[1].split("=")[1];
						}

						if (urlParameters[0].startsWith("oauth_verifier")) {
							oauthVerifier = urlParameters[0].split("=")[1];
						} else if (urlParameters[1].startsWith("oauth_verifier")) {
							oauthVerifier = urlParameters[1].split("=")[1];
						}

						mIntent.putExtra("oauth_token", oauthToken);
						mIntent.putExtra("oauth_verifier", oauthVerifier);

						setResult(RESULT_OK, mIntent);
						TwitterBase.getInst(getBaseContext()).completeLogin(mIntent);
						Log.i("Twitter", "Login OK2");
						finish();
					} catch (Exception e) {
						e.printStackTrace();
						setResult(RESULT_CANCELED, mIntent);
						finish();
					}
				}/*else if(url != null &&  url.equals("https://twitter.com/oauth/authorize")){
					String[] urlParameters = url.split("\\?")[1].split("&");
					String oauthToken = "";
					String oauthVerifier = "";

					try {
						if (urlParameters[0].startsWith("oauth_token")) {
							oauthToken = urlParameters[0].split("=")[1];
						} else if (urlParameters[1].startsWith("oauth_token")) {
							oauthToken = urlParameters[1].split("=")[1];
						}

						if (urlParameters[0].startsWith("oauth_verifier")) {
							oauthVerifier = urlParameters[0].split("=")[1];
						} else if (urlParameters[1]
								.startsWith("oauth_verifier")) {
							oauthVerifier = urlParameters[1].split("=")[1];
						}

						mIntent.putExtra("oauth_token", oauthToken);
						mIntent.putExtra("oauth_verifier", oauthVerifier);

						setResult(RESULT_OK, mIntent);
						finish();
					} catch (Exception e) {
						e.printStackTrace();
					}					
				}*/
			}
		});

		mIntent = getIntent();
		String url1 = mIntent.getStringExtra("auth_url");
		webView.loadUrl(url1);
	}
}