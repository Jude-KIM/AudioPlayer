package com.airplug.audioplug;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;

public class Preferences {
	
	private static final String KEY_MAIN = "setting";
	
	public static final String KEY_ENABLE_AGENT			= "B001";
	public static final String KEY_ENABLE_DATA			= "B002";
	public static final String KEY_PLAYER				= "B003";
	
	public static final String KEY_AMSW_ENABLED			= "A002";
	public static final String KEY_TEST_MODE			= "A003";
	public static final String KEY_SERVICE_BINDING		= "A004";
	

	public static void setAgentEnabled(Context context, boolean value) {
		store(context, KEY_ENABLE_AGENT, value);
	}

	public static boolean isAgentEnabled(Context context) {
		return restoreBoolean(context, KEY_ENABLE_AGENT, true);
	}
	
	public static boolean isDataEnabled(Context context) {
		return restoreBoolean(context, KEY_ENABLE_DATA, true);
	}
	
	public static void setDataEnabled(Context context, boolean value) {
		store(context, KEY_ENABLE_DATA, value);
	}
	
	public static void setAMSWEnabled(Context context, boolean value) {
		store(context, KEY_AMSW_ENABLED, value);
	}

	public static boolean isAMSWEnabled(Context context) {
		return restoreBoolean(context, KEY_AMSW_ENABLED, false);
	}
	
	public static void setTestMode(Context context, boolean value) {
		store(context, KEY_TEST_MODE, value);
	}

	public static boolean isTestMode(Context context) {
		return restoreBoolean(context, KEY_TEST_MODE, false);
	}
	
	public static void setServiceBinding(Context context, boolean value) {
		store(context, KEY_SERVICE_BINDING, value);
	}

	public static boolean isServiceBinding(Context context) {
		return restoreBoolean(context, KEY_SERVICE_BINDING, true);
	}
	

	public static void store(Context context, String key, 
			boolean value) {
		SharedPreferences settings = 
			context.getSharedPreferences(
				KEY_MAIN, 
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	
	public static boolean restoreBoolean(Context context, 
			String key, boolean defValue) {
		SharedPreferences settings = 
			context.getSharedPreferences(
				KEY_MAIN, 
				Context.MODE_PRIVATE);
		boolean value = settings.getBoolean(key, defValue);
		return value;
	}

	public static void store(Context context, String key, 
			int value) {
		SharedPreferences settings = 
			context.getSharedPreferences(
				KEY_MAIN, 
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static int restoreInt(Context context, String key, 
			int defValue) {
		SharedPreferences settings = 
			context.getSharedPreferences(
				KEY_MAIN, 
				Context.MODE_PRIVATE);
		int value = settings.getInt(key, defValue);
		return value;
	}

	public static void store(Context context, String key, 
			long value) {
		SharedPreferences settings = 
			context.getSharedPreferences(
				KEY_MAIN, 
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	public static long restoreLong(Context context, String key, 
			long defValue) {
		SharedPreferences settings = 
			context.getSharedPreferences(
				KEY_MAIN, 
				Context.MODE_PRIVATE);
		long value = settings.getLong(key, defValue);
		return value;
	}

	public static void store(Context context, String key, 
			float value) {
		SharedPreferences settings = 
			context.getSharedPreferences(
				KEY_MAIN, 
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putFloat(key, value);
		editor.commit();
	}

	public static float restoreFloat(Context context, String key, 
			float defValue) {
		SharedPreferences settings = 
			context.getSharedPreferences(
				KEY_MAIN, 
				Context.MODE_PRIVATE);
		float value = settings.getFloat(key, defValue);
		return value;
	}
	
	public static void store(Context context, String key, 
			String value) {
		SharedPreferences settings = 
			context.getSharedPreferences(
				KEY_MAIN,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static String restoreString(Context context, String key,
			String defValue) {
		SharedPreferences settings = 
			context.getSharedPreferences(
				KEY_MAIN, 
				Context.MODE_PRIVATE);
		String value = settings.getString(key, defValue);
		return value;
	}
	
	public static void storePreference(Context context, String key,
			CheckBoxPreference preference) {
		boolean value = preference.isChecked();
		preference.setSummary("" + value);
		store(context, key, value);
	}
	
	public static void storePreference(Context context, String key,
			SwitchPreference preference) {
		boolean value = preference.isChecked();
		preference.setSummary("" + value);
		store(context, key, value);
	}
	
	public static void restoreSwitchBox(Context context, int resId,
			boolean defValue) {
		restoreSwitchBox(context, context.getString(resId), defValue);
	}
	
	public static void restoreSwitchBox(Context context, String key,
			boolean defValue) {		
		
		SwitchPreference preference =
			(SwitchPreference)((PreferenceActivity)context).findPreference(key);		
		if(preference == null) return;
		boolean value = restoreBoolean(context, key, defValue);
		preference.setSummary("" + value);
		preference.setChecked(value);
	}

	public static void restoreCheckBox(Context context, int resId,
			boolean defValue) {
		restoreCheckBox(context, context.getString(resId), defValue);
	}

	@SuppressWarnings("deprecation")
	public static void restoreCheckBox(Context context, String key,
			boolean defValue) {
		CheckBoxPreference preference =
			(CheckBoxPreference)((PreferenceActivity)context).findPreference(key);
		if(preference == null) return;
		boolean value = restoreBoolean(context, key, defValue);
		preference.setSummary("" + value);
		preference.setChecked(value);
	}

	public static String storePreferenceString(Context context, String key,
			ListPreference preference) {
		CharSequence entry = preference.getEntry();
		preference.setSummary(entry);
		store(context, key, preference.getValue());
		return entry.toString();
	}

	public static void storePreferenceInt(Context context, String key,
			ListPreference preference) {
		preference.setSummary(preference.getEntry());
		store(context, key, Integer.parseInt(preference.getValue()));
	}

	@SuppressWarnings("deprecation")
	public static void restoreList(Context context, int resId,
			String defValue) {
		String key = context.getString(resId);
		ListPreference preference =
			(ListPreference)((PreferenceActivity)context).findPreference(key);
		if(preference == null) return;
		String value = restoreString(context, key, defValue);
		CharSequence entry = preference.getEntry();

		if(entry == null && value != null) {
			CharSequence[] entries = preference.getEntries();
			CharSequence[] entryValues = preference.getEntryValues();

			for(int n = 0; n < entryValues.length; n++) {
				if(value.equals(entryValues[n])) {
					entry = entries[n];
					break;
				}
			}
		}
		
		preference.setSummary(entry);
		preference.setValue(value);
	}

	@SuppressWarnings("deprecation")
	public static void restoreList(Context context, int resId, int defValue) {
		String key = context.getString(resId);
		ListPreference preference =
			(ListPreference)((PreferenceActivity)context).findPreference(key);
		if(preference == null) return;
		int value = restoreInt(context, key, defValue);
		CharSequence entry = preference.getEntry();

		if(entry == null) {
			CharSequence[] entries = preference.getEntries();
			CharSequence[] entryValues = preference.getEntryValues();

			for(int n = 0; n < entryValues.length; n++) {
				int eValue = Integer.parseInt(entryValues[n].toString());
				if(eValue == value) {
					entry = entries[n];
					break;
				}
			}
		}
		
		preference.setSummary(entry);
		preference.setValue("" + value);
	}

	public static void storePreference(Context context, String key,
			EditTextPreference preference) {
		storePreference(context, key, preference, true);
	}
	
	public static void restoreEditText(Context context, int resId) {
		restoreEditText(context, resId, true);
	}

	public static void storePreference(Context context, String key,
			EditTextPreference preference, boolean showSummary) {
		String value = preference.getText();
		if(showSummary) preference.setSummary(value);
		store(context, key, value);
	}

	@SuppressWarnings("deprecation")
	public static void restoreEditText(Context context, int resId,
			boolean showSummary) {
		String key = context.getString(resId);
		EditTextPreference preference = (EditTextPreference)((PreferenceActivity)context).findPreference(key);
		if(preference == null) return;
		String value = restoreString(context, key, "");
		if(showSummary) preference.setSummary(value);
		preference.setText(value);
	}
}
