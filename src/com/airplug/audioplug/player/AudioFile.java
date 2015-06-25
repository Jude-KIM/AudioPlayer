package com.airplug.audioplug.player;


import java.io.Serializable;


import com.airplug.audioplug.channellist.RSSFile.FeedElements;
import com.airplug.audioplug.player.AudioPlayer.State;


public class AudioFile implements Serializable {
	private static final long serialVersionUID = 6031157792533123906L;
	
	public String url;
	public String title;
	public String channelTitle;
	public String type;
	
	public String description;
	public String author;
	public String link;
	public String pubDate;
	public String thumbnail;	
	
	public int length;
	public State state;
	public int index;


	public AudioFile(AudioFile entry) {
		this(entry.url, entry.title, entry.channelTitle, entry.index, entry.type, entry.description,
				entry.author, entry.link, entry.pubDate, entry.thumbnail, entry.length, entry.state);
	}
	
	public AudioFile(FeedElements item, String thumbnail, String channelTitle, int index, State state) {
		this(item.enclosure_url, item.title, channelTitle, index, item.enclosure_type, item.description,
				item.itunes_author, item.link, item.pubDate, thumbnail, 
				item.enclosure_length != null && 0 < item.enclosure_length.length()?
						Integer.parseInt(item.enclosure_length): 0, state);
	}


	public AudioFile(String url, String title, String channelTitle, int index, String type, String description,
			String author, String link, String pubDate, String thumbnail, int length, State state) {
		this.url = url;
		this.title = title;
		this.channelTitle = channelTitle;
		this.index = index;
		this.type = type;
		
		this.description = description;
		this.author = author;
		this.link = link;
		this.pubDate = pubDate;
		this.thumbnail = thumbnail;		
		this.length = length;
		this.state = state;
	}



	@Override
	public String toString() {
		return state.name() + " " + type + " " + length + " " + url + " " + title;
	}
	
	public boolean equal(AudioFile entry) {
		return entry != null &&
				entry.url != null &&
				url.equals(entry.url);
	}
}

