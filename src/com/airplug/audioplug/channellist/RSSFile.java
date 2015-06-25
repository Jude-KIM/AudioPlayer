package com.airplug.audioplug.channellist;


import java.io.Serializable;
import java.util.ArrayList;

import org.xml.sax.Attributes;

import android.util.Log;

import com.airplug.audioplug.util.Util;




public class RSSFile implements Serializable {


	private static final String NAME = "RSSFile";
	private final String CLASS = NAME + "@" + Integer.toHexString(hashCode());
	
	private static final long serialVersionUID = -8555952521621654857L;


	public static class CommonElements implements Serializable {
		private static final long serialVersionUID = 8287148251812341206L;
		public String title;
		public String link;
		public String description;
		public String itunes_author;
	}
	
	public static class ChannelElements extends CommonElements {
		private static final long serialVersionUID = 5039766525246165260L;
		public String language;
		public String copyright;
		public String itunes_subtitle;
		public String itunes_summary;
		public String itunes_explicit;
		public String itunes_image;
		public String itunes_category;
		public String itnues_name;
		public String itunes_email;
	}
	
	public static class FeedElements extends CommonElements {
		private static final long serialVersionUID = -1351925330091463316L;
		public String enclosure_url;
		public String enclosure_type;
		public String enclosure_length;
		public String guid;
		public String pubDate;
		
		@Override
		public String toString() {
			return enclosure_type + " " + enclosure_url;
		}
	}
	
	public final ChannelElements channel = new ChannelElements();
	public final ArrayList<FeedElements> feeds = new ArrayList<FeedElements>();
	
	private int depth;
	private String value;
	private FeedElements feed;
	
	@Override
	public String toString() {
		String text = channel.title + " " + channel.link + " " + channel.description;
		return text;
	}
	
	public void clear() {
		feeds.clear();
	}
	
	public void begin(String qName, Attributes attributes) {
		value = null;
		
		if(qName.equalsIgnoreCase("channel")) {
			depth = 0;
		} else if(qName.equalsIgnoreCase("itunes:owner")) {
			depth++;
		} else if(qName.equalsIgnoreCase("item")) {
			depth++;
			feed = new FeedElements();
		} else if(qName.equalsIgnoreCase("title")) {
			value = "";
		} else if(qName.equalsIgnoreCase("link")) {
			value = "";
		} else if(qName.equalsIgnoreCase("itunes:author")) {
			value = "";
		} else if(qName.equalsIgnoreCase("description")) {
			value = "";
		} else if(qName.equalsIgnoreCase("language")) {
			value = "";
		} else if(qName.equalsIgnoreCase("itunes:subtitle")) {
			value = "";
		} else if(qName.equalsIgnoreCase("itunes:summary")) {
			value = "";
		} else if(qName.equalsIgnoreCase("copyright")) {
			value = "";
		} else if(qName.equalsIgnoreCase("itunes:explicit")) {
			value = "";
		} else if(qName.equalsIgnoreCase("itunes:image")) {
			if(depth == 0) {
				channel.itunes_image = attributes.getValue("href");
			}
		} else if(qName.equalsIgnoreCase("itunes:category")) {
			if(depth == 0) {
				channel.itunes_category = attributes.getValue("text");
			}
		} else if(qName.equalsIgnoreCase("itunes:name")) {
			value = "";
		} else if(qName.equalsIgnoreCase("itunes:email")) {
			value = "";
		} else if(qName.equalsIgnoreCase("enclosure")) {
			if(depth == 1) {
				feed.enclosure_url = attributes.getValue("url");
				feed.enclosure_type = attributes.getValue("type");
				feed.enclosure_length = attributes.getValue("length");
			}
		} else if(qName.equalsIgnoreCase("guid")) {
			if(depth == 1) {
				feed.guid = attributes.getValue("isPermaLink");
			}
		} else if(qName.equalsIgnoreCase("pubDate")) {
			value = "";
		}
	}
	
	public void end(String qName) {
		if(qName.equalsIgnoreCase("channel")) {
			
		} else if(qName.equalsIgnoreCase("itunes:owner")) {
			depth--;
		} else if(qName.equalsIgnoreCase("item")) {
			depth--;
			feeds.add(feed);
			feed = null;
		} else if(qName.equalsIgnoreCase("title")) {
			if(depth == 0) {
				channel.title = value;
			} else if(depth == 1) {
				feed.title = value;
			}
		} else if(qName.equalsIgnoreCase("link")) {
			if(depth == 0) {
				channel.link = value;
			} else if(depth == 1) {
				feed.link = value;
			}
		} else if(qName.equalsIgnoreCase("itunes:author")) {
			if(depth == 0) {
				channel.itunes_author = value;
			} else if(depth == 1) {
				feed.itunes_author = value;
			}
		} else if(qName.equalsIgnoreCase("description")) {
			if(depth == 0) {
				channel.description = value;
			} else if(depth == 1) {
				feed.description = value;
			}
		} else if(qName.equalsIgnoreCase("language")) {
			if(depth == 0) {
				channel.language = value;
			}
		} else if(qName.equalsIgnoreCase("itunes:subtitle")) {
			if(depth == 0) {
				channel.itunes_subtitle = value;
			}
		} else if(qName.equalsIgnoreCase("itunes:summary")) {
			if(depth == 0) {
				channel.itunes_summary = value;
			}
		} else if(qName.equalsIgnoreCase("copyright")) {
			if(depth == 0) {
				channel.copyright = value;
			}
		} else if(qName.equalsIgnoreCase("itunes:explicit")) {
			if(depth == 0) {
				channel.itunes_explicit = value;
			}
		} else if(qName.equalsIgnoreCase("itunes:name")) {
			if(depth == 1) {
				channel.itnues_name = value;
			}
		} else if(qName.equalsIgnoreCase("itunes:email")) {
			if(depth == 1) {
				channel.itunes_email = value;
			}
		} else if(qName.equalsIgnoreCase("pubDate")) {
			if(depth == 1) {
				//feed.pubDate = value;
				feed.pubDate = Util.dateFormatConvert(value, "EEE, dd MMM yyyy HH:mm:ss Z", "yyyy-MM-dd, HH:mm:ss");				
			}
		}
		
		if(value != null) {
			Log.i("jude", "RSSFile.java, end(), depth" + " " + qName + " " + value);			
		}
	}
	
	public void read(String qName, String value) {
//		RDLog.v(TAG, "RSS: " + qName + " " + value);
		
		if(this.value != null) {
			this.value += value;
		}
	}
}
