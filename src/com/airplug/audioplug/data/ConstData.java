package com.airplug.audioplug.data;



/**
 * 공통 ?�용 ?�정�?
 * @author 1Ticket
 *
 */
public class ConstData {
	
	//For AudioPlug
	public static final String AGENT_PACKAGE_NAME = "com.airplug.mao.agent";
	public static final int MODE_PLAYER = 1;
	public static final int MODE_PLAYLIST = 2;
	public static final boolean DEBUG = true;
	
	//For player and widget
	public static final int FLAG_NO_MEDIA = 0;
	public static final int FLAG_PLAYING = 1;
	public static final int FLAG_BUFFERING = 2;
	public static final int FLAG_ERROR = -1;	
	public static final int FLAG_EMPTY_QUEUE = 3;
	public static final int FLAG_PAUSE = 6;
	public static final int FLAG_STOPPED = 9;
	public static final int MASK_FINISH = 4;
	public static final int MASK_SHUFFLE = 5;
	
	public static final String FACEBOOK_APP_ID = "1390089447914111";
	public static final String FACEBOOK_SHARE_MESSAGE = "Say something about this...";
	public static final String FACEBOOK_SHARE_ACTION_NAME = "AudioPlug";
	public static final String FACEBOOK_SHARE_ACTION_LINK = "http://blog.airplug.com/";
	public static final String FACEBOOK_SHARE_IMAGE_CAPTION = "Great image";	
	public static final String FACEBOOK_SHARE_LINK = "https://github.com/nostra13/Android-Simple-Social-Sharing";
	public static final String FACEBOOK_SHARE_LINK_NAME = "Use Android Simple Social Sharing in your project!";
	public static final String FACEBOOK_SHARE_LINK_DESCRIPTION = "Also see other projects of nostra13 on GitHub!";
	public static final String FACEBOOK_SHARE_PICTURE = "http://cdn.androidcommunity.com/wp-content/uploads/2011/01/facebook-android-logo-1.jpg";
	
	public static final String TWITTER_SHARE_MESSAGE = "Say something about this...";
	public static final String TWITTER_CONSUMER_KEY = "ntJEEHLiSrFPAHRwY6qgSQ";
	public static final String TWITTER_CONSUMER_SECRET = "fWBvq4aWJxoSjav19RG2Ig2W4tnHarfYZEjtQST81E";
	//public static final String TWITTER_API_KEY = "526f6c8ff14924d4b5f875f974bd24ae";
	public static final String TWITTER_API_KEY = "ntJEEHLiSrFPAHRwY6qgSQ";
	public static final String LOG_TAG = "twitter";
	
	public static final String TWITTER_CALLBACK_URL = "http://vp.airplug.com/callback/";
	public static final String MOVE_TWITTER_LOGIN = "com.android.twittercon.TWITTER_LOGIN"; 
	public static String TWITTER_LOGOUT_URL = "http://api.twitter.com/logout";
	public static final int LOGIN_CODE = 123456789;
	public static boolean D = true;
	public static final String SLIDE_TWITTER = "SLIDE_TWITTER";
	
	public static final String TWITTER_ACCESS_TOKEN = "twitter_access_token";
	public static final String TWITTER_ACCESS_TOKEN_SECRET = "twitter_access_token_secret";
	
	//怨듯넻 ?占쎌슜 ?占쎌젙
	public static  int MAX_PAGE_CHANNEL_COUNT = 8;
	public static  int MAX_PAGE_COUNT = 12;
	public static int	MAX_STRAIGHT_PLAY_COUNT = 5;
	
	public static final String HOME_YOUTUBE_URL = "http://m.youtube.com";
	public static final String HOME_TVING_URL = "http://air.tving.com/html/tvingAir/index.html";
	public static final String HOME_TVPOT_URL = "http://m.tvpot.daum.net/best/BestClipList.tv";
	public static final String HOME_PANN_URL = "http://m.pann.nate.com/";
	
	public static final String SEARCH_URL_YOUTUBE	= "http://gdata.youtube.com/feeds/api/videos?";
	public static final String SEARCH_URL_DAUMTVPOT	= "http://apis.daum.net/search/vclip?";
	public static final String SEARCH_URL_TVING		= "http://air.tving.com/TvingAir/api/search/cliplist";
	public static final String SEARCH_URL_VIMEO		= "http://vimeo.com/api/rest/v2?format=json&method=vimeo.videos.search&";
	public static final String VIMEO_RELATED_URL	= "http://vimeo.com/api/v2/%s/videos.json?page=1";
	
	public static final String VIMEO_API_GET_THUMBNAIL_URL = "http://vimeo.com/api/rest/v2?format=json&method=vimeo.videos.getThumbnailUrls&video_id=";
	
	public static final String VPCS_URL = "http://vp.airplug.com/vp/index.php?mid=vpcs";
	public static final String VPHELP_URL = "http://vp.airplug.com/vp/vphelp/index.php";
	public static final String AGENT_MARKET_URL = "market://details?id=com.airplug.mao.agent";
	public static final String AGENT_APK_NAME = "AirPlugAgentDev.apk";
	public static final String VIDEOPLUG_MARKET_URL = "market://details?id=com.airplug.videoplug";
	
	//public static final String VPCS_URL = "http://www.airplug.com/videoplug/index.php?mid=cs2";
	public static final String VP_PREFERENCE = "VP Preference";
	public static final String VP_FACEBOOK_PREFERENCE = "facebook";
	public static final String CHANNEL_FILE_NAME = "vp_channels.json";
	
	public static final String CHANNEL_UPDATE_FLAG_VP = "http://vp.airplug.com/flag/";
	public static final String CHANNEL_UPDATE_FLAG_VPG = "http://vpg.airplug.com/flag/";
	
	public static final int CHANNEL_VERSION = 2;
	
	public static final String CHANNEL_UPDATE_URL_VP = "http://vp.airplug.com/channel/?v=2";
	public static final String CHANNEL_UPDATE_URL_VPG = "http://vpg.airplug.com/channel/?v=2";
	
	public static final String CHANNEL_UPDATE_URL_DEV_VP = CHANNEL_UPDATE_URL_VP + "&mode=9";
	public static final String CHANNEL_UPDATE_URL_DEV_VPG = CHANNEL_UPDATE_URL_VPG + "&mode=9";
	public static final String CHANNEL_UPDATE_URL_EVA_VP = CHANNEL_UPDATE_URL_VP + "&mode=1";
	public static final String CHANNEL_UPDATE_URL_EVA_VPG = CHANNEL_UPDATE_URL_VPG + "&mode=1";
	public static final String CHANNEL_UPDATE_URL_COM_VP = CHANNEL_UPDATE_URL_VP + "&mode=0";
	public static final String CHANNEL_UPDATE_URL_COM_VPG = CHANNEL_UPDATE_URL_VPG + "&mode=0";
	
	public static final String VP_NOTIFICATION_URL_KO = "http://vp.airplug.com/vp/notice/?language=ko";
	public static final String VP_NOTIFICATION_URL_EN = "http://vp.airplug.com/vp/notice/?language=en";
	
	
	// Workaround for network switching error - jude
	public static final int NET_TYPE_CELL = 0;
	public static final int NET_TYPE_WIFI = 1;
	public static final int NET_TYPE_NONE = 2;
	public static final String CurrentPlayPrefs = "CurrentPlayPrefs";
	
	public static final class Extra {
		public static final String POST_MESSAGE = "com.airplug.audioplug.share.extra.POST_MESSAGE";
		public static final String POST_LINK = "com.airplug.audioplug.share.extra.POST_LINK";
		public static final String POST_PHOTO = "com.airplug.audioplug.share.extra.POST_PHOTO";
		public static final String POST_PHOTO_DATE = "com.airplug.audioplug.share.extra.POST_PHOTO_DATE";
		public static final String POST_LINK_NAME = "com.airplug.audioplug.share.extra.POST_LINK_NAME";		
		public static final String POST_CONTENT_TITLE = "com.airplug.audioplug.share.extra.POST_CONTENT_TITLE";		
		public static final String POST_LINK_DESCRIPTION = "com.airplug.audioplug.share.extra.POST_LINK_DESCRIPTION";
		public static final String POST_PICTURE = "com.airplug.audioplug.share.extra.POST_PICTURE";
		
	}
	
}
