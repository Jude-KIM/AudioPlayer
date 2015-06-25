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
		createBookmarkInfo(db);			/// Î∂ÅÎßà?¨Ï†ïÎ≥??åÏù¥Î∏?create
		createCategoryList(db);			/// Ïπ¥ÌÖåÍ≥†Î¶¨ Î¶¨Ïä§???ïÎ≥¥ ?åÏù¥Î∏?create
		createChannelList(db);			/// Ï±ÑÎÑê Î¶¨Ïä§???åÏù¥Î∏?create
		createSelectChannelList(db);	/// ?†ÌÉù??Ï±ÑÎÑê Î™©Î°ù ?åÏù¥Î∏?create
		createIhaveSeenMovieInfo(db);	/// ?¥Í? Î≥??ÅÏÉÅ ?ÅÏÉÅ Î™©Î°ù ?åÏù¥Î∏?create
		createSharedMovieInfo(db);		/// Í≥µÏú†???ÅÏÉÅÎ™©Î°ù ?åÏù¥Î∏?create
		createStraightPlayList(db);		/// ?∞ÏÜç?¨ÏÉù Î™©Î°ù ?åÏù¥Î∏?create
		createTotalSearch(db);			/// ?µÌï© Í≤?Éâ Í≤∞Í≥º Î™©Î°ù ?åÏù¥Î∏?create
		createSearchResultChannel(db);  /// Í≤?ÉâÍ≤∞Í≥º Ï±ÑÎÑê Î™©Î°ù ?åÏù¥Î∏?create
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		
	}
	
	
	/**
	 * Î∂ÅÎßà???åÏù¥Î∏??ùÏÑ±
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
	 * Í≤?Éâ Í≤∞Í≥º Ï±ÑÎÑê Î™©Î°ù ?ùÏÑ±
	 * @param db
	 */
	public void createSearchResultChannel(SQLiteDatabase db)
	{
		/*
		 	private int		bi_idx			=	0;	/// Î∂ÅÎßà??Í≥†Ïú†Î≤àÌò∏
		 	private int 	ch_idx				;	/// ?åÏÜç Ï±ÑÎÑê
			private int		m_nCpNo			=	0;	/// CP Í≥†Ïú†Î≤àÌò∏(ConstData ?±Î°ùÍ∞?Ï∞∏Ï°∞)
			private String	m_szThumbImg;			/// ?∏ÎÑ§???¥Î?Ïß?
			private String	m_szTitle;				/// ?ÅÏÉÅ ?úÎ™©
			private int		m_nPlayTime;			/// ?ÅÏÉÅ Í∏∏Ïù¥
			private int		m_nViewCount	=	0;	/// Ï°∞Ìöå??
			private String	m_szPostUser;			/// ?ÅÏÉÅ ?±Î°ù??
			private String	m_szMovieUrl;			/// ?ÅÏÉÅ URL
			private String  m_szDescription; 		///
			private String  m_szPublished;			/// ?ëÏÑ±?ºÏûê
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
	 * ?¥Í? Î≥??ÅÏÉÅ Î™©Î°ù ?åÏù¥Î∏??ùÏÑ±
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
	 * Í≥µÏú†???ÅÏÉÅ Î™©Î°ù
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
	 * Ïπ¥ÌÖåÍ≥†Î¶¨ Î¶¨Ïä§???åÏù¥Î∏??ùÏÑ±
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
	 * Ï±ÑÎÑê Î¶¨Ïä§???åÏù¥Î∏??ùÏÑ±
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
	 * ?†ÌÉù??Ï±ÑÎÑê Î¶¨Ïä§???ïÎ≥¥
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
	 * ?∞ÏÜç?¨ÏÉù Î¶¨Ïä§??
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
	 * ?µÌï©Í≤?Éâ Í≤∞Í≥º ??û• ?åÏù¥Î∏?
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
