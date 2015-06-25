package com.airplug.audioplug.channellist;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

import com.airplug.audioplug.channellist.ChannelListFragment.Type;



public class Channel implements Serializable {


private static final long serialVersionUID = -9119676639282955141L;
	
	private String url;
	private String title;
	private String description;	
	private int backGroundColor;
	private Type type;
	
	private boolean loading;
	private boolean loadedText;
	private boolean loadedImage;
	private boolean exception;
	
	private RSSFile rss;

	private String thumbnailUrl;

	private int pos;
	
	public Channel(String url, String title, String description, Type type, int pos) {
		this.url = url;
		this.title = title;
		this.description = description;
		this.type = type;
		exception = false;
		this.pos = pos;
	}


	@Override
	public String toString() {
		return "channel=" + url + "," + title + "," + description;
	}

	public Type getType() {
		return type;
	}

	public String getUrl() {
		return url;
	}


	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


//	public String getThumbnail() {
//		return thumbnail;
//	}
//
//
//	public void setThumbnail(String thumbnail) {
//		this.thumbnail = thumbnail;
//	}


	public int getBackGroundColor() {
		return backGroundColor;
	}


	public void setBackGroundColor(int backGroundColor) {
		this.backGroundColor = backGroundColor;
	}


	public boolean isLoading() {
		return loading;
	}


	public void setType(Type type) {
		this.type = type;
	}
	
	public void setLoading(boolean loading) {
		this.loading = loading;
	}


	public boolean isLoadedText() {
		return loadedText;
	}


	public void setLoadedText(boolean loaded) {
		this.loadedText = loaded;
	}


	public boolean isLoadedImage() {
		return loadedImage;
	}


	public void setLoadedImage(boolean loadedImage) {
		this.loadedImage = loadedImage;
	}

	public void setException(boolean exception) {
		this.exception = exception;
	}
	public boolean getException() {
		return exception;
	}

	public RSSFile getRSS() {
		return rss;
	}


	public void setRSS(RSSFile rss) {
		this.rss = rss;
	}


	public static String request(String spec) throws IOException {
		String page = null;
		HttpURLConnection urlConnection = null;
		InputStream instream = null;
		
		try {
			URL url = new URL(spec);
			urlConnection = (HttpURLConnection)url.openConnection();
			urlConnection.setConnectTimeout(5 * 1000);
			urlConnection.setReadTimeout(5 * 1000);
			urlConnection.connect();
			
			instream = new BufferedInputStream(urlConnection.getInputStream());
			if(instream != null) {
				page = convertInputStreamToString(instream);
			}
		} finally {
			try {
				if(instream != null) instream.close();
			} catch(IOException e) {}
			if(urlConnection != null) {
				urlConnection.disconnect();
			}
		}
		
		return page;
	}


	public static String convertInputStreamToString(InputStream instream) throws IOException {
		Writer writer = new StringWriter();
		Reader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(instream));
			char[] buffer = new char[1024];
			while(true) {
				int bytes = reader.read(buffer);
				if(bytes < 0) break;
				writer.write(buffer, 0, bytes);
			}
		} finally {
			try {
				if(writer != null) writer.close();
			} catch (IOException e1) {
			}
			try {
				if(reader != null) reader.close();
			} catch (IOException e1) {
			}
		}
		return writer.toString();
	}


	public void setThumbnailUrl(String itunes_image) {
		this.thumbnailUrl = itunes_image;
	}


	public String getThumbnailUrl() {
		return thumbnailUrl;
	}


	public int getPosition() {
		// TODO Auto-generated method stub
		return pos;
	}
	

	
}

