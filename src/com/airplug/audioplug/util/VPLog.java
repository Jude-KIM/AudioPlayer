package com.airplug.audioplug.util;

import android.util.Log;

public class VPLog {
	
	private static boolean useAPLog = false;
	public static final String TAG = "VIDEOPLUG";

	public static final void e(String CLASS, String msg) {
		if(useAPLog) {
//			APLog.e(TAG, CLASS, msg);
		} else {
			Log.e(TAG, CLASS + " " + msg);
		}
	}
	
	public static final void e(String CLASS, Throwable e) {
		if(useAPLog) {
//			APLog.e(TAG, CLASS, e);
		} else {
			Log.e(TAG, CLASS + " " + Log.getStackTraceString(e));
		}
	}

	public static final void e(String CLASS, String msg, Throwable e) {
		if(useAPLog) {
//			APLog.e(TAG, CLASS, msg + "\n" + Log.getStackTraceString(e));
		} else {
			Log.e(TAG, CLASS + " " + msg + "\n" + Log.getStackTraceString(e));
		}
	}

	public static final void w(String CLASS, String msg) {
		if(useAPLog) {
//			APLog.w(TAG, CLASS, msg);
		} else {
			Log.w(TAG, CLASS + " " + msg);
		}
	}

	public static final void i(String CLASS, String msg) {
		if(useAPLog) {
//			APLog.i(TAG, CLASS, msg);
		} else {
			Log.i(TAG, CLASS + " " + msg);
		}
	}

	public static final void d(String CLASS, String msg) {
		if(useAPLog) {
//			APLog.d(TAG, CLASS, msg);
		} else {
			Log.d(TAG, CLASS + " " + msg);
		}
	}

	public static final void v(String CLASS, String msg) {
		if(useAPLog) {
//			APLog.v(TAG, CLASS, msg);
		} else {
			Log.v(TAG, CLASS + " " + msg);
		}
	}
}
