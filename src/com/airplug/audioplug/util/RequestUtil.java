package com.airplug.audioplug.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.util.Log;

public class RequestUtil {

	private static final String NAME = "RequestUtil";
//	private final String CLASS = NAME + "@" + Integer.toHexString(hashCode());
	
	public static HttpURLConnection getConnection(String spec) throws IOException {		
		URL url = new URL(spec);		
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();		
		connection.setConnectTimeout(10 * 1000);
		connection.setReadTimeout(10 * 1000);		
		connection.connect();

		Log.i(NAME, "RESPONSE " +
				connection.getResponseCode() + " " +
				connection.getResponseMessage() + " " +
				(connection.getContentLength() == 0? "ZERO": connection.getContentLength()) + " " +
				connection.getHeaderField("Content-Length") + " " +
				connection.getContentType() + " " +
				spec);

		return connection;
	}

	public static long requestFile(String spec, String path) throws IOException {
		long length = -1;
		HttpURLConnection urlConnection = null;
		InputStream instream = null;
		
		try {
			urlConnection = getConnection(spec);
			
			instream = new BufferedInputStream(urlConnection.getInputStream());
			if(instream != null) {
				length = writeOutputStream(instream, new FileOutputStream (new File(path)));
				Log.v(NAME, "RESPONSE READ: " + length + " " + spec);
			}
		} finally {
			if(instream != null) instream.close();
			if(urlConnection != null) {
				urlConnection.disconnect();
			}
		}
		
		return length;
	}

	public static String requestString(String spec) throws IOException {				
		String page = null;
		HttpURLConnection urlConnection = null;
		InputStream instream = null;
		try {			
			urlConnection = getConnection(spec);			
			instream = new BufferedInputStream(urlConnection.getInputStream());			
			if(instream != null) {
				page = convertInputStreamToString(instream);
			}			
		} finally {
			if(instream != null) instream.close();
			if(urlConnection != null) {
				urlConnection.disconnect();
			}
		}		
		return page;
	}

	public static String loadThumbnail(Context context, String url) {
		String path = null;
		try {			

			File dir = context.getExternalCacheDir();
			if(dir.exists() == false) {
				dir.mkdir();
			}
			File file = new File(context.getExternalCacheDir(), getThumbnailName(url));
			path = file.getAbsolutePath();
			
			long length = requestFile(url, path);
			Log.i(NAME, "thumbna loaded " + length + " " + path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;
	}

	public static String getThumbnailName(String url) {
		return url
				.replace(":", "_")
				.replace("/", "_")
				.replace(".", "_")
				.replace("?", "_")
				.replace("&", "_")
				.replace("=", "_");
	}
	
	public static long writeOutputStream(InputStream instream, OutputStream output) throws IOException {
		long length = 0;
		try {
			byte[] buffer = new byte[1024*8];
			while(true) {
				int bytes = instream.read(buffer);
				if(bytes < 0) break;
				output.write(buffer, 0, bytes);
				length += bytes;
			}
		} finally {
			output.close();
		}
		return length;
	}

	public static String convertInputStreamToString(InputStream instream) throws IOException {
		Writer writer = new StringWriter();
		Reader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(instream));
			char[] buffer = new char[1024*8];
			while(true) {
				int bytes = reader.read(buffer);
				if(bytes < 0) break;
				writer.write(buffer, 0, bytes);
			}
		} finally {
			writer.close();
			reader.close();
		}
		return writer.toString();
	}
}
