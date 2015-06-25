package com.airplug.audioplug.mychannel;

import com.airplug.audioplug.dev.R;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyChannelFragment extends Fragment{
	
	public MyChannelFragment(Context mContext) {
		// TODO Auto-generated constructor stub
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_mychannel,
				container, false);		
		return rootView;
	}

}
