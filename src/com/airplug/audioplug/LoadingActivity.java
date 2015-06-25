package com.airplug.audioplug;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.airplug.audioplug.data.Data;
import com.airplug.audioplug.dev.R;
import com.airplug.audioplug.util.Util;

public class LoadingActivity extends Activity{
	
	String loc;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
		
		loc = new String(Data.getAppLocale(this));
		SharedPreferences mSharedPrefs = getSharedPreferences("BE", Activity.MODE_PRIVATE);
				
		if (mSharedPrefs.getBoolean("IS_FIRST_BOOT5", true) == true) 
		{	
			//showEncryptPerm_Dialog();
			//showAgreementDialog();		
			
			float[] initSizes = {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
			
			//Data.setDataUseSizes(this, initSizes);
		}
		
		
		Handler handler = new Handler(){
			public void handleMessage(Message msg){
				finish();
			}
		};

		
		handler.sendEmptyMessageDelayed(0, 30000);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		Util.recursiveRecycle(getWindow().getDecorView()); /// 사용자원 초기화
		System.gc();
	}
	
}
