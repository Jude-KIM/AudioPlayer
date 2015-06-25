package com.airplug.audioplug.share;

import com.airplug.audioplug.data.ConstData;

import android.content.Context;
import android.content.SharedPreferences;

public class TwitterUtil {
	public static void setAppPreferences(Context context, String key, String value) {
		SharedPreferences pref = null;
		pref = context.getSharedPreferences(ConstData.LOG_TAG, 0);
		SharedPreferences.Editor prefEditor = pref.edit();
		prefEditor.putString(key, value);

		prefEditor.commit();
	}

	public static String getAppPreferences(Context context, String key) {
		String returnValue = null;

		SharedPreferences pref = null;
		pref = context.getSharedPreferences(ConstData.LOG_TAG, 0);

		returnValue = pref.getString(key, "");

		return returnValue;
	}
}
