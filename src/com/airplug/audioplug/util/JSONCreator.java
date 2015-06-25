/*
 *Copyright linuxs by Airplug.,
 *All rights reserved.
 *Airplug.com
*/
package com.airplug.audioplug.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.json.JSONObject;

import android.content.Context;

public class JSONCreator {
	
	

	private String filePath;
	private Context ctx;

	public JSONCreator(Context ctx, String filePath) {
		this.filePath = filePath;
		this.ctx = ctx;
	}

	public JSONObject getJsonParser()
	{
		JSONObject jsonOJ = null;  
		String jString;  
		jString = "";  
		try
		{
			// linuxs@airplug.com
			FileInputStream ais = new FileInputStream(
					ctx.getFileStreamPath(filePath));
			BufferedReader br = new BufferedReader(new InputStreamReader(ais,
					Charset.forName("UTF-8")));// new BufferedReader(new
			
			StringBuilder		sb	=	new StringBuilder();
			
			int		bufferSize	=	1024 * 1024;  
			char	readBuf[]	=	new char[bufferSize];  
			int		resultSize	=	0;  
	  
			while((resultSize = br.read(readBuf)) != -1)
			{
				if (resultSize == bufferSize)
				{
					sb.append(readBuf);
				}
				else
				{
					for(int i = 0; i < resultSize; i++)
					{
						sb.append(readBuf[i]);
					}
				}
			}
			jString	+=	sb.toString();
		}
		catch(Exception e)
		{
			// TODO: handle exception  
			e.printStackTrace();  
		}
	
		try
		{
			jsonOJ	=	new JSONObject(jString);
		}
		catch(Exception e)
		{
			// TODO: handle exception  
			e.printStackTrace();
		}
		return jsonOJ;
	}
}
