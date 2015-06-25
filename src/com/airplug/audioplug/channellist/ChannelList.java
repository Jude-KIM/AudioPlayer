package com.airplug.audioplug.channellist;

import java.io.Serializable;

import com.airplug.audioplug.channellist.ChannelListFragment.Type;

import android.content.Context;

public class ChannelList implements Serializable {
	
	private static final long serialVersionUID = 9016911282963916802L;

	
	
	public Type type;
	public String[] urls;	
	
	
	public ChannelList(Type type) {		
		this.type = type;		
		this.urls = URLS;		
	}

	@Override
	public String toString() {
		return " " + type;
	}

	public static ParserBase getParser(Context context, Type type) {
		ParserBase parser = null;

		switch(type) {
		case RSS: parser = new ParserRSS(context); break;
		//case ICECAST: parser = new ParserIcecast(context); break;
		}
		return parser;
	}

	private static final String[] URLS = {			
			"http://wizard2.sbs.co.kr/w3/podcast/V0000349378A.xml",			
			"http://mbn.mk.co.kr/rss/vod/vod_rss_6.xml",
			"http://iamnormal.co.kr/programs/iamnormal.xml",
			"http://wizard2.sbs.co.kr/w3/podcast/V0000328482.xml",
			"http://xsfm.co.kr/xml/podera.xml",		
			"http://www.ted.com/talks/rss",			
			"http://www.docdocdoc.co.kr/podcast/iam_doctors.xml",
			"http://pod.ssenhosting.com/rss/changbi.xml",
			"http://svc.jtbc.co.kr/api/news/data/rss/JTBC_News9_AOD_Podcast.xml",
			"http://feeds.feedburner.com/boyt",
			"http://www.hemamusic.co.kr/radio/love2.xml",
			"http://www.aoosung.com/podcast/aoosung.xml",			
			"http://mondomedia.libsyn.com/rss",
	};

}
