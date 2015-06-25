package com.airplug.audioplug.player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;


public abstract class AudioList {

	protected static final String NAME = "AudioList";
	protected final String CLASS = NAME + "@" + Integer.toHexString(hashCode());
	
	public String type;
	public int version;
	public int numberofentries;
	public final List<AudioFile> list = new ArrayList<AudioFile>();
	
	public abstract void read(List<AudioFile> list, AudioFile entry) throws IOException;
	
	public void open(AudioFile entry) throws IOException {
		read(list, entry);
	}
	
	public void close() {
		list.clear();
	}
	
	@Override
	public String toString() {
		String text = "type=" + type + " version=" + version +
				" numberofentries=" + numberofentries + " size=" + list.size();
		for(AudioFile entry: list) {
			text += "\n" + entry;
		}
		return text;
	}

	protected static String request(String address) throws IOException {
		URL url = new URL(address);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setConnectTimeout(20000);
		conn.setReadTimeout(20000);
		conn.connect();
		
		String response = null;
		if(HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
			Log.d(NAME, conn.getResponseMessage() + " " + conn.getContentLength());
			
			response = convertInputStreamToString(conn.getInputStream());
		}
		
		conn.disconnect();
		
		return response;
	}
	
	private static String convertInputStreamToString(InputStream instream) throws IOException {
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

	private static String removeQuery(String url) {
		return url.split("\\?")[0];
	}
	
	private static String removeAfterSpace(String url) {
		return url.split(" ")[0];
	}

	public static boolean isSHOUTcast(String url) {
		url = removeAfterSpace(url);
		url = removeQuery(url);
		return url.endsWith(".pls");
	}

	public static boolean isM3U(String url) {
		url = removeAfterSpace(url);
		url = removeQuery(url);
		return url.endsWith(".m3u");
	}
	
	public static boolean isMMS(String url) {
		return url != null && url.startsWith("mms");
	}
}
