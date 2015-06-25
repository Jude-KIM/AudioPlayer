package com.airplug.audioplug.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airplug.audioplug.Preferences;
import com.airplug.audioplug.dev.R;

public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
	
	private Context mContext;	
	private Context appContext;	
	private SwitchPreference preference_agent, preference_data;	
	private String KEY_AGENT = Preferences.KEY_ENABLE_AGENT;
	private String KEY_DATA = Preferences.KEY_ENABLE_DATA;
	Handler mHandler = new Handler();	
	
	public SettingsFragment() {
		super();
	}
	
	public SettingsFragment setContext(Context context) {
		mContext = context;
		return this;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);		
		addPreferencesFromResource(R.xml.setting);	
		appContext = getActivity();
		preference_agent = (SwitchPreference) findPreference(KEY_AGENT);
		preference_data = (SwitchPreference) findPreference(KEY_DATA);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View mainView = inflater.inflate(R.layout.fragment_settings, null);		
//		agentSwitch = (Switch)mainView.findViewById(R.id.switchWidget);		
//		Log.d("jude","agentswitch : " + agentSwitch);		
				
		//setButtonView();
		
		return mainView;
		
	}	

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		//restorePreferences();		
		getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);		
	}
	
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
				
		getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub
		
		if(key.equals(Preferences.KEY_ENABLE_AGENT)){
			Log.i("jude","KEY_ENABLE_AGENT : "  + preference_agent.isChecked());
			Preferences.storePreference(appContext, key, preference_agent);
			preference_agent.setSummary("" + getString(R.string.agent_summary));
			//if(preference_agent.isChecked()){
			
		}
		else if(key.equals(Preferences.KEY_ENABLE_DATA)){
			Log.e("jude","KEY_ENABLE_DATA : "  + preference_data.isChecked());
			Preferences.storePreference(appContext, key, preference_data);
			preference_data.setSummary("" + getString(R.string.data_summary));
			if(Preferences.isDataEnabled(appContext)){
				//use mobile network
				preference_data.setChecked(true);
				Preferences.setDataEnabled(appContext, true);
				disableAgentSetting(false);
			}
			else{
				//disable mobile network
				Preferences.setDataEnabled(appContext, false);
				preference_data.setChecked(false);				
				//disableAgentSetting(true);
				
			}
		}
		
		//setButtonView();

	}
	
	public void disableAgentSetting(boolean value) {
		Log.d("jude","disableAgentSetting : " + value);		
		if(value){
			preference_agent.setEnabled(false);
			preference_agent.setSummary(""+getString(R.string.agent_summary2));			
		}
		else
		{
			preference_agent.setEnabled(true);
			preference_agent.setSummary(""+getString(R.string.agent_summary));
		}
			
	}
		
	
	private void restorePreferences() {
		
		preference_agent.setChecked(Preferences.restoreBoolean(appContext,Preferences.KEY_ENABLE_AGENT , Preferences.isAgentEnabled(appContext)));
		preference_data.setChecked(Preferences.restoreBoolean(appContext,Preferences.KEY_ENABLE_DATA , Preferences.isDataEnabled(appContext)));
	}	
}

