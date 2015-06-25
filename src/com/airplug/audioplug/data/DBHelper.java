package com.airplug.audioplug.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper
{
	private static final String	DBN		=	"videoplug.db";
	private static final int	VERSION	=	3;
	public DBHelper(Context context)
	{
		super(context, DBN, null, VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		createBookmarkInfo(db);			/// 북마?�정�??�이�?create
		createCategoryList(db);			/// 카테고리 리스???�보 ?�이�?create
		createChannelList(db);			/// 채널 리스???�이�?create
		createSelectChannelList(db);	/// ?�택??채널 목록 ?�이�?create
		createIhaveSeenMovieInfo(db);	/// ?��? �??�상 ?�상 목록 ?�이�?create
		createSharedMovieInfo(db);		/// 공유???�상목록 ?�이�?create
		createStraightPlayList(db);		/// ?�속?�생 목록 ?�이�?create
		createTotalSearch(db);			/// ?�합 �?�� 결과 목록 ?�이�?create
		createSearchResultChannel(db);  /// �?��결과 채널 목록 ?�이�?create
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		
	}
	
	
	/**
	 * 북마???�이�??�성
	 * @param db
	 */
	public void createBookmarkInfo(SQLiteDatabase db)
	{
		String szSql;
		szSql	=	"CREATE TABLE IF NOT EXISTS bookmark_info "
				+	"("
				+	"	bi_idx INTEGER PRIMARY KEY AUTOINCREMENT "
				+	",	week_no INTEGER DEFAULT 0 "
				+	",	movie_url VARCHAR(256) "
				+	",	cp_no INTEGER DEFAULT 0 "
				+	",	thumb_img VARCHAR(256) "
				+	",	title VARCHAR(256) "
				+	",	playtime VARCHAR(256) "
				+	",	viewcount INTEGER DEFAULT 0"
				+	",	postuser VARCHAR(256) "
				+	",	date VARCHAR(40) "
				+ 	",  description VARCHAR(256)"
				+	",	published VARCHAR(256)"
				+	",  like_count INTEGER DEFAULT 0"
				+   ",  score FLOAT DEFAULT 0"
				+ 	",  video_id VARCHAR(256)"
				+	");"
				+	"CREATE INDEX IF NOT EXISTS ix_week_no ON bookmark_info (week_no);"
				+	"CREATE INDEX IF NOT EXISTS ix_cp_no ON bookmark_info (cp_no);";
		db.execSQL(szSql);
	}
	
	/**
	 * �?�� 결과 채널 목록 ?�성
	 * @param db
	 */
	public void createSearchResultChannel(SQLiteDatabase db)
	{
		/*
		 	private int		bi_idx			=	0;	/// 북마??고유번호
		 	private int 	ch_idx				;	/// ?�속 채널
			private int		m_nCpNo			=	0;	/// CP 고유번호(ConstData ?�록�?참조)
			private String	m_szThumbImg;			/// ?�네???��?�?
			private String	m_szTitle;				/// ?�상 ?�목
			private int		m_nPlayTime;			/// ?�상 길이
			private int		m_nViewCount	=	0;	/// 조회??
			private String	m_szPostUser;			/// ?�상 ?�록??
			private String	m_szMovieUrl;			/// ?�상 URL
			private String  m_szDescription; 		///
			private String  m_szPublished;			/// ?�성?�자
			private int		m_nLikeCount;			///
			private float	m_fScore;				// 
			private String  m_szVideoId;			// video_id
		 */
		String szSql;
		szSql	=	"CREATE TABLE IF NOT EXISTS search_result_channel"
				+	"("
				+	"	src_idx INTEGER PRIMARY KEY AUTOINCREMENT "
				+   ",  ch_idx INTEGER "
				+	",	cp_no INTEGER DEFAULT 0 "
				+	",	movie_url VARCHAR(256) "
				+	",	thumb_img VARCHAR(256) "
				+	",	title VARCHAR(256) "
				+	",	playtime VARCHAR(256) "
				+	",	viewcount INTEGER DEFAULT 0"
				+	",	postuser VARCHAR(256) "
				+ 	",  description VARCHAR(256)"
				+	",	published VARCHAR(256)"
				+	",  like_count INTEGER DEFAULT 0"
				+   ",  score FLOAT DEFAULT 0"
				+ 	",  video_id VARCHAR(256)"
				+	");";

		db.execSQL(szSql);
	}
	
	/**
	 * ?��? �??�상 목록 ?�이�??�성
	 * @param db
	 */
	public void createIhaveSeenMovieInfo(SQLiteDatabase db)
	{
		String szSql;
		szSql	=	"CREATE TABLE IF NOT EXISTS ihaveseenmovie_info "
				+	"("
				+	"	ihsm_idx INTEGER PRIMARY KEY AUTOINCREMENT "
				+	",	movie_url VARCHAR(256) "
				+	",	cp_no INTEGER DEFAULT 0 "
				+	",	thumb_img VARCHAR(256) "
				+	",	title VARCHAR(256) "
				+	",	playtime VARCHAR(256) "
				+	",	viewcount INTEGER DEFAULT 0 "
				+	",	postuser VARCHAR(256) "
				+	",	date VARCHAR(40) "
				+ 	",  description VARCHAR(256)"
				+	",	published VARCHAR(256)"
				+	",  like_count INTEGER DEFAULT 0"
				+   ",  score FLOAT DEFAULT 0"
				+ 	",  video_id VARCHAR(256)"
				+	");"
				+	"CREATE INDEX IF NOT EXISTS ix_movie_url ON ihaveseenmovie_info (movie_url);"
				+	"CREATE INDEX IF NOT EXISTS ix_cp_no ON ihaveseenmovie_info (cp_no);";
		db.execSQL(szSql);
	}
	
	/**
	 * 공유???�상 목록
	 * @param db
	 */
	public void createSharedMovieInfo(SQLiteDatabase db)
	{
		String szSql;
		szSql	=	"CREATE TABLE IF NOT EXISTS sharedmovie_info"
				+	"("
				+	"	sm_idx INTEGER PRIMARY KEY AUTOINCREMENT "
				+	",	movie_url VARCHAR(256) "
				+	",	cp_no INTEGER DEFAULT 0 "
				+	",	thumb_img VARCHAR(256) "
				+	",	title VARCHAR(256) "
				+	",	playtime VARCHAR(256) "
				+	",	viewcount INTEGER DEFAULT 0 "
				+	",	postuser VARCHAR(256) "
				+	",	date VARCHAR(40) "
				+ 	",  description VARCHAR(256)"
				+	",	published VARCHAR(256)"
				+	",  like_count INTEGER DEFAULT 0"
				+   ",  score FLOAT DEFAULT 0"
				+ 	",  video_id VARCHAR(256)"
				+	");"
				+	"CREATE INDEX IF NOT EXISTS ix_movie_url ON sharedmovie_info (movie_url);"
				+	"CREATE INDEX IF NOT EXISTS ix_cp_no ON sharedmovie_info (cp_no);";
		db.execSQL(szSql);
	}
	
	/**
	 * 카테고리 리스???�이�??�성
	 * @param db
	 */
	public void createCategoryList(SQLiteDatabase db) {
		String szSql;
		szSql = "CREATE TABLE IF NOT EXISTS category_list ("
				+ "ct_idx INTEGER PRIMARY KEY AUTOINCREMENT"
				+ ", name VARCHAR(256)"
				+ ");";
				
		
		db.execSQL(szSql);
	}
	
	/**
	 * 채널 리스???�이�??�성
	 * @param db
	 */
	public void createChannelList(SQLiteDatabase db) {
//		channelItem.setChCategory(jArr.getJSONObject(i).getString("ch_category").toString());
//		channelItem.setChDate(jArr.getJSONObject(i).getString("ch_date").toString());
//		channelItem.setChDescription(jArr.getJSONObject(i).getString("ch_description").toString());
//		channelItem.setChOwner(jArr.getJSONObject(i).getString("ch_owner").toString());
//		channelItem.setIsDefault(jArr.getJSONObject(i).getInt("ch_isdefault"));


		String szSql;
//		szSql = "CREATE TABLE IF NOT EXISTS channel_list ("
//				+ "ch_id INTEGER PRIMARY KEY"
//				+ ", ch_name VARCHAR(256)"
//				+ ", ch_url VARCHAR(256)"
//				+ ", ch_thumbnail VARCHAR(1024)"
//				+ ", site_idx INTEGER"
//				+ ", ch_site VARCHAR(128)"
//				+ ");";
		szSql = "CREATE TABLE IF NOT EXISTS channel_list ("
				+ "ch_id INTEGER PRIMARY KEY"
				+ ", ch_name VARCHAR(256)"
				+ ", ch_url VARCHAR(256)"
				+ ", ch_thumbnail VARCHAR(1024)"
				+ ", site_idx INTEGER"
				+ ", ch_site VARCHAR(128)"
				+ ", ch_category VARCHAR(128)"
				+ ", ch_date VARCHAR(128)"
				+ ", ch_description VARCHAR(1024)"
				+ ", ch_owner VARCHAR(128)"
				+ ", ch_isdefault INTEGER"
				+ ", ch_lang VARCHAR(32)"
				+ ");";

		db.execSQL(szSql);
	}
	
	/**
	 * ?�택??채널 리스???�보
	 * @param db
	 */
	public void createSelectChannelList(SQLiteDatabase db) {
		String szSql;
		szSql	=	"CREATE TABLE IF NOT EXISTS select_channel_list ("
				+	"sch_idx INTEGER PRIMARY KEY AUTOINCREMENT"
				+	", ct_idx INTEGER"
				+	", ch_id INTEGER"
		//		+	", ch_update INTEGER"
				+	");"
				+	"CREATE INDEX IF NOT EXISTS ix_ct_idx ON select_channel_list (ct_idx);"
				+	"CREATE INDEX IF NOT EXISTS ix_ch_id ON select_channel_list (ch_id);";
		db.execSQL(szSql);
	}
	
	/**
	 * ?�속?�생 리스??
	 * @param db
	 */
	public void createStraightPlayList(SQLiteDatabase db)
	{
		String szSql;
		szSql	=	"CREATE TABLE IF NOT EXISTS straight_play_list "
				+	"("
				+	"	spl_idx INTEGER PRIMARY KEY AUTOINCREMENT "
				+	",	cp_no INTEGER DEFAULT 0 "
				+	",	title VARCHAR(256) "
				+	",	content TEXT "
				+	",	linkAlternate TEXT "
				+	",	thumbnailURL TEXT "
				+	",	watcherCount INTEGER DEFAULT 0 "
				+	",	author TEXT "
				+	");"
				+	"CREATE INDEX IF NOT EXISTS ix_linkAlternate ON straight_play_list (linkAlternate);";
		db.execSQL(szSql);
	}
	
	/**
	 * ?�합�?�� 결과 ??�� ?�이�?
	 * @param db
	 */
	public void createTotalSearch(SQLiteDatabase db)
	{
		String szSql;
		szSql	=	"CREATE TABLE IF NOT EXISTS search_result_list "
				+	"("
				+	"	srl_idx INTEGER PRIMARY KEY AUTOINCREMENT "
				+	",	cp_no INTEGER DEFAULT 0 "
				+	",	title VARCHAR(256) "
				+	",	content TEXT "
				+	",	linkAlternate TEXT "
				+	",	thumbnailURL TEXT "
				+	",	watcherCount INTEGER DEFAULT 0 "
				+	",	author TEXT "
				+	",	play_time INTEGER DEFAULT 0"		// 9
				+	",	published VARCHAR(256)"		// 10
				+	",	like_count INTEGER DEFAULT 0"	// 12
				+	",	score FLOAT DEFAULT 0"			// 13
				+	",	video_id VARCHAR(256)"		// 14
				+	");"
				+	"CREATE INDEX IF NOT EXISTS ix_linkAlternate ON search_result_list (linkAlternate);";
		db.execSQL(szSql);
	}
	
}
