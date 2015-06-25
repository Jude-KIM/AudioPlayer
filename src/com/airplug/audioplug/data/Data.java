package com.airplug.audioplug.data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Data {
	/*
	
//	@SuppressWarnings("unused")
//	private final static String TAG = "Data";
//	private final static String CLASS = "Data";
//	private static Data data_;
//	public static WebCache webCache = new WebCache();	
//	private static ArrayList<BookmarkData> g_arBookmarkDataList;
//	private static ArrayList<BookmarkWeekData> g_arBookmarkWeekDataList;
//	//private static ArrayList<EntryItem> g_arIhaveSeenMovieList;
//	private static ArrayList<SlideTabData>	g_arBookmarkSlideTabData;
//	private static ArrayList<SlideTabData>	g_arSearchSlideTabData;
//	private static DBCtrl g_dbCtrl;	
////	private static HashMap<String, ArrayList<ChannelItem> > channelItemListMap_;
////	private static ArrayList<ChannelItem> allChannelList_;
////	private static ArrayList<CategoryItem> categoryItemList_;
////	private static HashMap<Integer, ChannelItem> addChannelItemMap_;
////	private static ChannelItem selectChannelItem;
////	private static EntryItem selectEntryItem;
////	private static ArrayList<EntryItem> selectEntryItemList;
////	
//	// detailpage => bookmarkpage(Searchpage) => detailpage - Jude
//	private static String bookmarkChannelName;
//	private static String searchCPName;
//	
//	
//	public static Data getInstance() {
//		if (data_ == null) {
//			data_ = new Data();
//		}
//		return data_;
//	}
//	
//	public Data() {	}
//	
//	
//	/**
//	 * showbookmarkdetailpage ??channel name
//	 * @return
//	 */
//	// detailpage => bookmarkpage(Searchpage) => detailpage - Jude
//	public static String getbookmarkChannelName() {
//		return bookmarkChannelName;
//	}
//	
//	/**
//	 * ?�택 ?�세 ?�이�?
//	 * @param item
//	 */
//	// detailpage => bookmarkpage(Searchpage) => detailpage - Jude
//	public static void setbookmarkChannelName(final String name) {
//		bookmarkChannelName = name;
//	}
//	
//	
//	// detailpage => bookmarkpage(Searchpage) => detailpage - Jude
//		public static String getsearchCPName() {
//			return searchCPName;
//		}		
//		
//	/**
//	 * ?�택 ?�세 ?�이�?
//	 * @param item
//	 */
//	// detailpage => bookmarkpage(Searchpage) => detailpage - Jude
//	public static void setsearchCPName(final String name) {
//		searchCPName = name;
//	}
//			
//
	
	public static final String vp_main_3g_access 			= "C001";
	public static final String vp_main_network_switch 		= "C002";
	public static final String vp_player_screen_lock 		= "C003";
	public static final String vp_player_waitsee_check 		= "C004";
	public static final String vp_player_waitsee_option 	= "C005";
	public static final String vp_player_orientation 		= "C006";
	public static final String vp_data_init_use_data_day 	= "C007";
	public static final String vp_data_user_want_data_size 	= "C008";
	public static final String vp_setting_logview		 	= "C009";
	public static final String vp_locale				 	= "C010";
	public static final String vp_blended_boost				= "C011";
	public static final String vp_agent_enabled				= "C012";
	public static final String vp_blended_boost_support		= "C013";
	
	public static String getAppLocale(Context context) {
		SharedPreferences pref = null;
		pref = context.getSharedPreferences("BE", Activity.MODE_PRIVATE);
		String locale = pref.getString(vp_locale, "");
		
		/*
		if (locale.equals("") == true) {
			locale = Util.getLocale(context);
		}
		*/
		return locale;
	}
	
	/**
	 * 
	 * @param context
	 * @return
	 */
	public static boolean is3GAccess(Context context) {
		try {
			SharedPreferences mSharedPrefs = context.getSharedPreferences("BE", Activity.MODE_PRIVATE);
			boolean ret = false;
			
			//Dev, Eva - true, Com - false
			if(Config.B2C_MODE)
			{
				ret = mSharedPrefs.getBoolean(vp_main_3g_access, false);
			}else
			{
				ret = mSharedPrefs.getBoolean(vp_main_3g_access, true);
			}		

			return ret;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 3g?�용�??�팅
	 * @param context
	 * @param b
	 */
	public static void setIs3GAccess(Context context, boolean b) {
		try {
			SharedPreferences mSharedPrefs = context.getSharedPreferences("BE", Activity.MODE_PRIVATE);
			Editor editor = mSharedPrefs.edit();
			editor.putBoolean(vp_main_3g_access, b);
			editor.commit();
			
//			if(MainActivity.agent != null) {
//				MainActivity.agent.set(Extra.AGENT_ALLOW_CELL.name(), b);
//			}
		} catch (Exception e) {
		}
	}
//
//	/**
//	 * ?�면 ?�금
//	 * @param context
//	 * @return
//	 */
//	public static int getPlayerScreenLock(Context context) {
//		SharedPreferences pref = null;
//		pref = context.getSharedPreferences("BE", Activity.MODE_PRIVATE);
//		return pref.getInt(MaoApi.vp_player_screen_lock, 0);
//	}
//	
//	/**
//	 * ?�면 ?�금
//	 * @param context
//	 * @param i
//	 */
//	public static void setPlayerScreenLock(Context context, int i) {
//		SharedPreferences pref = null;
//		pref = context.getSharedPreferences("BE", Activity.MODE_PRIVATE);
//		SharedPreferences.Editor prefEditor = pref.edit();
//		prefEditor.putInt(MaoApi.vp_player_screen_lock, i);
//		prefEditor.commit();
//	}
//
//	/**
//	 * ?�레?�어 �?��보기
//	 * @param context
//	 * @return
//	 */
//	public static int getPlayerScreenOrientation(Context context) {
//		SharedPreferences pref = null;
//		pref = context.getSharedPreferences("BE", Activity.MODE_PRIVATE);
//		return pref.getInt(MaoApi.vp_player_orientation, 0);
//	}
//	
//	/**
//	 * ?�레?�어 �?��보기 ?�팅
//	 * @param context
//	 * @param i
//	 */
//	public static void setPlayerScreenOrientation(Context context, int i) {
//		SharedPreferences pref = null;
//		pref = context.getSharedPreferences("BE", Activity.MODE_PRIVATE);
//		SharedPreferences.Editor prefEditor = pref.edit();
//		prefEditor.putInt(MaoApi.vp_player_orientation, i);
//		prefEditor.commit();
//	}
//
//	/**
//	 * 초기 ?�짜
//	 * @param context
//	 * @return
//	 */
//	public static int getInitUseDataDay(Context context) {
//		SharedPreferences mSharedPrefs = context.getSharedPreferences("BE", Activity.MODE_PRIVATE);
//		return mSharedPrefs.getInt(MaoApi.vp_data_init_use_data_day, 1);
//	}
//	
//	/**
//	 * 초기 ?�짜 ?�팅
//	 * @param context
//	 * @param v
//	 */
//	public static void setInitUseDataDay(Context context, int v) {
//		try {
//			SharedPreferences mSharedPrefs = context.getSharedPreferences("BE", Activity.MODE_PRIVATE);
//			Editor editor = mSharedPrefs.edit();
//			editor.putInt(MaoApi.vp_data_init_use_data_day, v);
//			editor.commit();
//		} catch (Exception e) {
//		}
//	}
//
//	
//	/*
//	 * Network Lelvel set
//	 */
//	
//	public static void setNetworkStatus(Context context, int i){
//		try{
//			SharedPreferences mSharedPrefs = context.getSharedPreferences("BE", Activity.MODE_PRIVATE);
//			Editor editor = mSharedPrefs.edit();
//			editor.putInt("NETWORK_STATUS", i);
//			editor.commit();
//		}catch(Exception e){
//			
//		}
//	}
//	
//	public static int getNetworkStatus(Context context){
//		SharedPreferences mSharedPrefs = context.getSharedPreferences("BE", Activity.MODE_PRIVATE);
//		return mSharedPrefs.getInt("NETWORK_STATUS", 0);
//	}
//
//	
//	/**
//	 * ?�이???�용??
//	 * @param context
//	 * @param v
//	 */
//	public static void setDataUseSize(Context context, float v) {
//		try {
//			SharedPreferences mSharedPrefs = context.getSharedPreferences("BE", Activity.MODE_PRIVATE);
//			Editor editor = mSharedPrefs.edit();
//			editor.putFloat("DATA_USE_SIZE", v);
//			editor.commit();
//		} catch (Exception e) {
//		}
//	}
//
//	public static void setDataUseSizes(Context context, float[] v){
//		
//		
//		for(int i = 0; i < 6; i++){
//			
//			if(v[i] <= 0){
//				v[i] = 0;
//			}
//			
//			//v[i] = v[i] / 1000;
//		}
//		
//		setDataUseSize(context, v[3]);
//		
//		try{
//			SharedPreferences mSharedPrefs = context.getSharedPreferences("BE", Activity.MODE_PRIVATE);
//			Editor editor = mSharedPrefs.edit();
//			
//			editor.putFloat("DATA_USE_THIS_MONTH_WIFI", v[0]);
//			editor.putFloat("DATA_USE_PREVIOUS_MONTH_WIFI", v[1]);
//			editor.putFloat("DATA_USE_THREE_MONTH_AGO_WIFI", v[2]);
//			
//			editor.putFloat("DATA_USE_THIS_MONTH_MOBILE", v[3]);
//			editor.putFloat("DATA_USE_PREVIOUS_MONTH_MOBILE", v[4]);
//			editor.putFloat("DATA_USE_THREE_MONTH_AGO_MOBILE", v[5]);
//					
//			editor.commit();
//			
//		}catch(Exception e){
//			
//		}
//	}
//	
//	public static float[] getDataUseSizes(Context context) {
//		SharedPreferences mSharedPrefs = context.getSharedPreferences("BE", Activity.MODE_PRIVATE);
//		float[] v = new float[6];
//		
//		v[0] = mSharedPrefs.getFloat("DATA_USE_THIS_MONTH_WIFI", 0.0f);
//		v[1] = mSharedPrefs.getFloat("DATA_USE_PREVIOUS_MONTH_WIFI", 0.0f);
//		v[2] = mSharedPrefs.getFloat("DATA_USE_THREE_MONTH_AGO_WIFI", 0.0f);
//		
//		
//		v[3] = mSharedPrefs.getFloat("DATA_USE_THIS_MONTH_MOBILE", 0.0f);
//		v[4] = mSharedPrefs.getFloat("DATA_USE_PREVIOUS_MONTH_MOBILE", 0.0f);
//		v[5] = mSharedPrefs.getFloat("DATA_USE_THREE_MONTH_AGO_MOBILE", 0.0f);
//		
//		
//		return v;
//	}
//
//	public static int getAlarmCycle(Context context) {
//		
//		SharedPreferences pref = null;
//		pref = context.getSharedPreferences("BE", Activity.MODE_PRIVATE);
//		return pref.getInt("ALARM_CYCLE", 0);
//	}
//
//	public static void setAlarmCycle(Context context, int i) {
//		SharedPreferences pref = null;
//		pref = context.getSharedPreferences("BE", Activity.MODE_PRIVATE);
//		SharedPreferences.Editor prefEditor = pref.edit();
//		prefEditor.putInt("ALARM_CYCLE", i);
//		prefEditor.commit();
//	}
//	
//	public static int getRetryCount(Context context) {
//		SharedPreferences pref = null;
//		pref = context.getSharedPreferences("BE", Activity.MODE_PRIVATE);
//		return pref.getInt("RETRY_COUNT", 0);
//	}
//	
//	public static void setRetryCount(Context context, int i){
//		SharedPreferences pref = null;
//		pref = context.getSharedPreferences("BE", Activity.MODE_PRIVATE);
//		SharedPreferences.Editor prefEditor = pref.edit();
//		prefEditor.putInt("RETRY_COUNT", i);
//		prefEditor.commit();
//	}
//	
//	public static boolean getSettingLogView(Context context) {
//		SharedPreferences pref = null;
//		pref = context.getSharedPreferences("BE", Activity.MODE_PRIVATE);
//		//HongKoon
//		Log.i("HONGKOON", "getSettingLogView - " + pref.getBoolean(MaoApi.vp_setting_logview, false));
//		return pref.getBoolean(MaoApi.vp_setting_logview, false);
//	}
//	
//	public static void setSettingLogView(Context context, boolean b) {
//		SharedPreferences pref = null;
//		pref = context.getSharedPreferences("BE", Activity.MODE_PRIVATE);
//		SharedPreferences.Editor prefEditor = pref.edit();
//		prefEditor.putBoolean(MaoApi.vp_setting_logview, b);
//		prefEditor.commit();
//	}
//
//        public static String getAppLocale(Context context) {
//		SharedPreferences pref = null;
//		pref = context.getSharedPreferences("BE", Activity.MODE_PRIVATE);
//		String locale = pref.getString(MaoApi.vp_locale, "");
//		
//		/*
//		if (locale.equals("") == true) {
//			locale = Util.getLocale(context);
//		}
//		*/
//		return locale;
//		
//	}
//	
//	public static void setAppLocale(Context context, String locale) {
//		SharedPreferences pref = null;
//		pref = context.getSharedPreferences("BE", Activity.MODE_PRIVATE);
//		SharedPreferences.Editor prefEditor = pref.edit();
//		prefEditor.putString(MaoApi.vp_locale, locale);
//		prefEditor.commit();
//	}
//
//
//	/*
//	 * Set Install Date 
//	 */
//	
//	public static void setInstallDate(Context context) {
//		
//		Calendar calendar = Calendar.getInstance();
//		
//		int month = calendar.get(Calendar.MONTH) + 1;
//		int year = calendar.get(Calendar.YEAR);
//		
//		SharedPreferences pref = null;
//		pref = context.getSharedPreferences("BE", Activity.MODE_PRIVATE);
//		SharedPreferences.Editor prefEditor = pref.edit();
//		
//		prefEditor.putInt("INSTALL_MONTH", month);
//		prefEditor.putInt("INSTALL_YEAR", year);
//		
//		prefEditor.commit();
//	}
//	
//	public static int isGoneThreeMonth(Context context){
//		Calendar calendar = Calendar.getInstance();
//		
//		int month = calendar.get(Calendar.MONTH) + 1;
//		int year = calendar.get(Calendar.YEAR);
//		
//		SharedPreferences pref = null;
//		pref = context.getSharedPreferences("BE", Activity.MODE_PRIVATE);
//		int installMonth = pref.getInt("INSTALL_MONTH", 1);
//		int installYear = pref.getInt("INSTALL_YEAR", 2013);
//		
//		int gapYear = (year - installYear) * 12;
//		int gapMonth = month - installMonth;
//		
//		return (gapYear + gapMonth);		
//	}
//
//	public static void setBBSupport(Context context, boolean isChecked) {
//		SharedPreferences pref = null;
//		pref = context.getSharedPreferences("BE", Activity.MODE_PRIVATE);
//		SharedPreferences.Editor prefEditor = pref.edit();
//		prefEditor.putBoolean(MaoApi.vp_blended_boost_support, isChecked);
//		prefEditor.commit();	
//	}
//	
//	public static boolean isBBSupport(Context context){
//		SharedPreferences pref = null;
//		pref = context.getSharedPreferences("BE", Activity.MODE_PRIVATE);
//		return pref.getBoolean(MaoApi.vp_blended_boost_support, false);
//	}
//
//	/*
//	 * 	BlendedBoost Setting
//	 */
//	public static void setBlendedBoost(Context context, boolean isChecked) {
//		SharedPreferences pref = null;
//		pref = context.getSharedPreferences("BE", Activity.MODE_PRIVATE);
//		SharedPreferences.Editor prefEditor = pref.edit();
//		prefEditor.putBoolean(MaoApi.vp_blended_boost, isChecked);
//		prefEditor.commit();
//		MaoApi.setPreference(Extra.AGENT_ALLOW_BOOST.name(), isChecked);
//	}
//
//	public static boolean getBlendedBoost(Context context) {
//		SharedPreferences pref = null;
//		pref = context.getSharedPreferences("BE", Activity.MODE_PRIVATE);
//		return pref.getBoolean(MaoApi.vp_blended_boost, true);
//		
//	}
//
//	public static void setAgentActivation(Context context, boolean agentActivation)
//	{
//		SharedPreferences pref = null;
//		pref = context.getSharedPreferences("BE", Activity.MODE_PRIVATE);
//		SharedPreferences.Editor prefEditor = pref.edit();
//		prefEditor.putBoolean("VIDEOPLUG_AGENT_ACTIVATION", agentActivation);
//		prefEditor.commit();
//		
//	}
//	
//	public static boolean getAgentActivation(Context context)
//	{
//		SharedPreferences pref = null;
//		pref = context.getSharedPreferences("BE", Activity.MODE_PRIVATE);
//		return pref.getBoolean("VIDEOPLUG_AGENT_ACTIVATION", true);
//	}
//	
//	public static void setAgentEnabled(Context context, boolean agentEnabled) {
//		SharedPreferences pref = null;
//		pref = context.getSharedPreferences("BE", Activity.MODE_PRIVATE);
//		SharedPreferences.Editor prefEditor = pref.edit();
//		prefEditor.putBoolean(MaoApi.vp_agent_enabled, agentEnabled);
//		prefEditor.commit();
//		MaoApi.setPreference(MaoApi.vp_agent_enabled, agentEnabled);
//	}
//	
//	public static boolean getAgentEnabled(Context context){
//		SharedPreferences pref = null;
//		pref = context.getSharedPreferences("BE", Activity.MODE_PRIVATE);
//		return pref.getBoolean(MaoApi.vp_agent_enabled, false);
//	}
//
//	public static void setChannelUpdateUrl(Context context,
//			String channelUpdateUrl) {
//		SharedPreferences pref = null;
//		pref = context.getSharedPreferences("BE", Activity.MODE_PRIVATE);
//		SharedPreferences.Editor prefEditor = pref.edit();
//		prefEditor.putString("CHANNEL_UPDATE_URL", channelUpdateUrl);
//		prefEditor.commit();
//	}
//
//	public static void setChannelUpdateFlag(Context context,
//			String channelUpdateFlag) {
//		SharedPreferences pref = null;
//		pref = context.getSharedPreferences("BE", Activity.MODE_PRIVATE);
//		SharedPreferences.Editor prefEditor = pref.edit();
//		prefEditor.putString("CHANNEL_UPDATE_FLAG", channelUpdateFlag);
//		prefEditor.commit();
//	}
//
//	public static String getChannelUpdateUrl(Context context) {
//		SharedPreferences pref = null;
//		pref = context.getSharedPreferences("BE", Activity.MODE_PRIVATE);
//		return pref.getString("CHANNEL_UPDATE_URL", null);
//	}
//	
//	public static String getChannelUpdateFlag(Context context) {
//		SharedPreferences pref = null;
//		pref = context.getSharedPreferences("BE", Activity.MODE_PRIVATE);
//		return pref.getString("CHANNEL_UPDATE_FLAG", null);
//	}
//
//	public static void setContainAdsClip(Context context, boolean b) {
//		SharedPreferences pref = null;
//		pref = context.getSharedPreferences("BE", Activity.MODE_PRIVATE);
//		SharedPreferences.Editor prefEditor = pref.edit();
//		prefEditor.putBoolean("CONTAIN_ADVERTISEMENT_CLIP", b);
//		prefEditor.commit();
//	}
//	
//	public static boolean getContainAdsClip(Context context) {
//		SharedPreferences pref = null;
//		pref = context.getSharedPreferences("BE", Activity.MODE_PRIVATE);
//		return pref.getBoolean("CONTAIN_ADVERTISEMENT_CLIP", false);		
//	}
//
//	public static long getVideoPlugCheckDate(Context context) {
//		SharedPreferences pref = null;
//		pref = context.getSharedPreferences("BE", Activity.MODE_PRIVATE);
//		return pref.getLong("VIDEOPLUG_CHECKDATE", -1);
//	}
//	
//	public static void setVideoPlugCheckDate(Context context, Long date){
//		SharedPreferences pref = null;
//		pref = context.getSharedPreferences("BE", Activity.MODE_PRIVATE);
//		SharedPreferences.Editor prefEditor = pref.edit();
//		prefEditor.putLong("VIDEOPLUG_CHECKDATE", date);
//		prefEditor.commit();
//	}
//	
//	public static String getVideoPlugNewestVersion(Context context){
//		
//		SharedPreferences pref = null;
//		PackageInfo packageInfo = null;
//		
//		try 
//		{
//			PackageManager packageManager = context.getPackageManager();
//			packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
//			
//			pref = context.getSharedPreferences("BE", Activity.MODE_PRIVATE);
//			
//		} catch (NameNotFoundException e) {
//		
//		}
//		
//		return pref.getString("VIDEOPLUG_NEWEST_VERSION", packageInfo.versionName);
//	}
//
//	public static void setVideoPlugNewestVersion(Context context, String newestVersion) {
//		SharedPreferences pref = null;
//		pref = context.getSharedPreferences("BE", Activity.MODE_PRIVATE);
//		SharedPreferences.Editor prefEditor = pref.edit();
//		prefEditor.putString("VIDEOPLUG_NEWEST_VERSION", newestVersion);
//		prefEditor.commit();
//	}
//	
//	public static void setRestartFlag(Context context, boolean restart)
//	{
//		SharedPreferences pref = null;
//		pref = context.getSharedPreferences("BE", Activity.MODE_PRIVATE);
//		SharedPreferences.Editor prefEditor = pref.edit();
//		prefEditor.putBoolean("MENUTAB_ACTIVITY_RESTART", restart);
//		prefEditor.commit();
//		
//	}
//	
//	public static boolean getRestartFlag(Context context)
//	{
//		SharedPreferences pref = null;
//		pref = context.getSharedPreferences("BE", Activity.MODE_PRIVATE);
//		return pref.getBoolean("MENUTAB_ACTIVITY_RESTART", false);
//	}
//
//	public static void setNetworkSwitchingErrorFlag(Context context, boolean error)
//	{
//		SharedPreferences pref = null;
//		pref = context.getSharedPreferences("BE", Activity.MODE_PRIVATE);
//		SharedPreferences.Editor prefEditor = pref.edit();
//		prefEditor.putBoolean("NETWORK_SWITCHING_ERROR", error);
//		prefEditor.commit();
//		
//	}
//	
//	
//	public static boolean getNetworkSwitchingErrorFlag(Context context) 
//	{
//		SharedPreferences pref = null;
//		pref = context.getSharedPreferences("BE", Activity.MODE_PRIVATE);
//		return pref.getBoolean("NETWORK_SWITCHING_ERROR", false);
//	}
//	
}
