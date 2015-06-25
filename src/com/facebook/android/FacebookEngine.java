package com.facebook.android;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.BaseKeyListener;
import android.util.Log;
import android.app.AlertDialog;

import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.Facebook.DialogListener;



public class FacebookEngine implements DialogListener 
{
	 static FacebookEngine instance = null;
	 
	 public static final String LOG_TAG = "FacebookCon";
	 public static boolean D = true;
	 public static final String FACEBOOK_APP_ID = "137105036432267";
	 public static final int FACEBOOK_AUTH_CODE = 32665;
	 private Facebook _mFacebook =  null;
	 public Activity _activity = null;
	 
//	 public class ImageUploadListener implements RequestListener {
//
//		    public void onComplete(final String response, final Object state) {
//		        try {
//		            // process the response here: (executed in background thread)
//		            Log.d("Facebook-Example", "Response: " + response.toString());
//		            JSONObject json = Util.parseJson(response);
//		            final String src = json.getString("src");
//
//		            // then post the processed result back to the UI thread
//		            // if we do not do this, an runtime exception will be generated
//		            // e.g. "CalledFromWrongThreadException: Only the original
//		            // thread that created a view hierarchy can touch its views."
//
//		        } catch (JSONException e) {
//		            Log.w("Facebook-Example", "JSON Error in response");
//		        } catch (FacebookError e) {
//		            Log.w("Facebook-Example", "Facebook Error: " + e.getMessage());
//		        }
//		    }
//
//		    public void onFacebookError(FacebookError e, Object state) {
//		        // TODO Auto-generated method stub
//
//		    }
//
//		    public Bitmap getInputType(Bitmap img) {
//		        // TODO Auto-generated method stub
//		        return img;
//		    }
//
//			@Override
//			public void onIOException(IOException e, Object state) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void onFileNotFoundException(FileNotFoundException e,
//					Object state) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void onMalformedURLException(MalformedURLException e,
//					Object state) {
//				// TODO Auto-generated method stub
//				
//			}
//
//	
//		}
//	 public class AuthorizeListener implements DialogListener
//	  {
//	    @Override
//	    public void onCancel()
//	    {
//	      // TODO Auto-generated method stub
//	      
//	    }
//
//	    @Override
//	    public void onComplete(Bundle values)
//	    {
//	      // TODO Auto-generated method stub
//	      //if (C.D) Log.v(C.LOG_TAG, "::: onComplete :::");
//	    }
//
//	    @Override
//	    public void onError(DialogError e)
//	    {
//	      // TODO Auto-generated method stub
//	      
//	    }
//
//	    @Override
//	    public void onFacebookError(FacebookError e)
//	    {
//	      // TODO Auto-generated method stub
//	      
//	    }
//	  }
	 
	 static public FacebookEngine getInstance()
	 {
		 if(instance == null)
		 {
			 instance = new FacebookEngine();
		 }
		 return instance;
		 
	 }
	 public FacebookEngine()
	 {
		 _mFacebook = new Facebook(FACEBOOK_APP_ID);
		 
		 //jude_test
		 //_activity = DataManager._resultActivity;
	 }
	 
	 public boolean isLogin()
	 {
		 return _mFacebook.isSessionValid(); 
		 
	 }
	 public void login(Activity a_Activity, DialogListener a_Listener)
	 {
		 
		 _mFacebook.authorize2(a_Activity, new String[] {"publish_stream, user_photos"}, a_Listener);
	 }
	 public void logout()
	  {
	    try
	    {
	      _mFacebook.logout(_activity);      
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	    }
	  }
	 public void feed(String a_Text)
	 {
		  try
		    {

		      
		      Bundle params = new Bundle();
		      params.putString("message", a_Text);
		      params.putString("name", "����ڸ�");
		      params.putString("link", "");
		      params.putString("description", a_Text);
		      params.putString("picture", "");

		      _mFacebook.request("me/feed", params, "POST");
		    }
		    catch (Exception e)
		    {
		      e.printStackTrace();
		    }
	 }
	 public void feedWithImage(Bitmap a_Bitmap, String a_Description, RequestListener a_Listener)
	 {
		 
		 try
		    {
			 	byte[] data = null;               
			 	ByteArrayOutputStream baos = new ByteArrayOutputStream();              
			 	a_Bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);              
		        data = baos.toByteArray();                
		        Bundle params = new Bundle();              
		        params.putString(Facebook.TOKEN, _mFacebook.getAccessToken());              
		        params.putString("method", "photos.upload");              
		        params.putByteArray("picture", data);               
		        AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(_mFacebook);              
		        mAsyncRunner.request(null, params, "POST", a_Listener, null);   
		    }
		    catch (Exception e)
		    {
		      e.printStackTrace();
		    }
	 }
	 public void authorizeCallback(int requestCode, int resultCode, Intent data)
	 {
		 _mFacebook.authorizeCallback(requestCode, resultCode, data);
	 }
	@Override
	public void onComplete(Bundle values) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onFacebookError(FacebookError e) {
		// TODO Auto-generated method stub
//		AlertDialog.Builder builder = new AlertDialog.Builder(DataManager._resultActivity);
//		builder.setTitle("FaceBook");
//		builder.setMessage(e.toString());
//		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
//		{
//			public void onClick(DialogInterface dialog, int which) 
//			{
//				dialog.dismiss();
//			}
//		});
//		builder.show();
	}
	@Override
	public void onError(DialogError e) {
		// TODO Auto-generated method stub
//		AlertDialog.Builder builder = new AlertDialog.Builder(DataManager._resultActivity);
//		builder.setTitle("FaceBook");
//		builder.setMessage(e.toString());
//		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
//		{
//			public void onClick(DialogInterface dialog, int which) 
//			{
//				dialog.dismiss();
//			}
//		});
//		builder.show();

		
	}
	@Override
	public void onCancel() {
		// TODO Auto-generated method stub
		
	}

}

