package com.airplug.audioplug.share;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.airplug.audioplug.data.ConstData;
import com.airplug.audioplug.data.ConstData.Extra;
import com.airplug.audioplug.dev.R;
import com.airplug.audioplug.util.ImageCache;
import com.nostra13.socialsharing.common.AuthListener;
import com.nostra13.socialsharing.common.PostListener;
import com.nostra13.socialsharing.facebook.FacebookEvents;
import com.nostra13.socialsharing.facebook.FacebookFacade;
/**
 * Activity for sharing information with Facebook
 * 
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
*/

public class FacebookActivity extends Activity {

	private FacebookFacade facebook;
	private FacebookEventObserver facebookEventObserver;
	
	private static final String NAME = "FacebookActivity";
	private final String CLASS = NAME + "@" + Integer.toHexString(hashCode());

	private TextView messageView;
	private ImageView thumbnailView;

	private String link;
	private String contentTitle;
	private String linkDescription;
	private String picture;	
	
	private Map<String, String> actionsMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_facebook);

		facebook = new FacebookFacade(this, getString(R.string.app_id));
		facebookEventObserver = FacebookEventObserver.newInstance();

		messageView = (TextView) findViewById(R.id.message);
		thumbnailView = (ImageView) findViewById(R.id.fb_thumb);
						
		TextView linkNameView = (TextView) findViewById(R.id.link_name);
		TextView linkDescriptionView = (TextView) findViewById(R.id.link_description);
		Button postButton = (Button) findViewById(R.id.btnFacebookPost);		
		//Button postImageButton = (Button) findViewById(R.id.btnFacebookPostImage);
		Button cancleButton = (Button) findViewById(R.id.btnFacebookCancle);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			//String message = bundle.getString(Extra.POST_MESSAGE);			
			link = bundle.getString(Extra.POST_LINK);
			contentTitle = bundle.getString(Extra.POST_CONTENT_TITLE);
			linkDescription = bundle.getString(Extra.POST_LINK_DESCRIPTION);
			picture = bundle.getString(Extra.POST_PICTURE);			
			
			//picture = getImageFromURL(bundle.getString(Extra.POST_PICTURE));
			
			actionsMap = new HashMap<String, String>();
			actionsMap.put(ConstData.FACEBOOK_SHARE_ACTION_NAME, ConstData.FACEBOOK_SHARE_ACTION_LINK);			
			messageView.setHint("Say something about this...");
			linkNameView.setText(contentTitle);
			linkDescriptionView.setText(linkDescription);
			ImageCache.getInstance(this).getImageLoader().displayImage(picture, thumbnailView, ImageCache.getInstance(this).getOption());
		}

		postButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (facebook.isAuthorized()) {
					publishMessage();
					finish();
				} else {
					// Start authentication dialog and publish message after successful authentication
					facebook.authorize(new AuthListener() {
						@Override
						public void onAuthSucceed() {
							publishMessage();
							finish();
						}

						@Override
						public void onAuthFail(String error) { // Do noting
						}
					});
				}
			}
		});
		
		cancleButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

		/*
		postImageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (facebook.isAuthorized()) {
					publishImage();
					finish();
				} else {
					// Start authentication dialog and publish image after successful authentication
					facebook.authorize(new AuthListener() {
						@Override
						public void onAuthSucceed() {
							publishImage();
							finish();
						}

						@Override
						public void onAuthFail(String error) { // Do noting
						}
					});
				}
			}
		});
	}
	*/

	
	
	public static Bitmap getImageFromURL(String imageURL){
        Bitmap imgBitmap = null;
        HttpURLConnection conn = null;
        BufferedInputStream bis = null;
        
        try
        {
            URL url = new URL(imageURL);
            conn = (HttpURLConnection)url.openConnection();
            conn.connect();
            
            int nSize = conn.getContentLength();
            bis = new BufferedInputStream(conn.getInputStream(), nSize);
            imgBitmap = BitmapFactory.decodeStream(bis);
        }
        catch (Exception e){
            e.printStackTrace();
        } finally{
            if(bis != null) {
                try {bis.close();} catch (IOException e) {}
            }
            if(conn != null ) {
                conn.disconnect();
            }
        }
        
        return imgBitmap;
    }

	private void publishMessage() {
		facebook.publishMessage(messageView.getText().toString(), link, contentTitle, linkDescription, picture, actionsMap);
	}
	
	private void publishImage() {
		Bitmap bmp = ((BitmapDrawable) getResources().getDrawable(R.drawable.test_thumbnail)).getBitmap();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		byte[] bitmapdata = stream.toByteArray();
		facebook.publishImage(bitmapdata, ConstData.FACEBOOK_SHARE_IMAGE_CAPTION);
	}

	@Override
	public void onStart() {
		super.onStart();
		facebookEventObserver.registerListeners(this);
		//FacebookEvents.addPostListener(listener);
		if (!facebook.isAuthorized()) {
			facebook.authorize();
		}
	}

	@Override
	public void onStop() {
		facebookEventObserver.unregisterListeners();
		//FacebookEvents.removePostListener(listener);
		super.onStop();
	}

}

