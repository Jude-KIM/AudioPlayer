package com.airplug.audioplug.channellist;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.content.Context;
import android.util.Xml;

import com.airplug.audioplug.channellist.ChannelListFragment.Type;
import com.airplug.audioplug.channellist.RSSFile.FeedElements;
import com.airplug.audioplug.util.RequestUtil;

public class ParserRSS extends ParserBase {
	
	private final Context context;
	
	public ParserRSS(Context context) {
		this.context = context;
	}

	@Override
	public ArrayList<FeedElements> parse(Channel channel)
			throws ParserConfigurationException, SAXException, IOException {
				
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		XMLReader reader = parser.getXMLReader();
		XMLHandler xmlHandler = new XMLHandler();
		reader.setContentHandler(xmlHandler);		
		Xml.parse(RequestUtil.requestString(channel.getUrl()), xmlHandler);		
		RSSFile rss = xmlHandler.getRSS();		
		channel.setTitle(rss.channel.title);
		channel.setDescription(rss.channel.description);
		channel.setRSS(rss);
		channel.setType(Type.RSS);
		channel.setLoadedText(true);
		
		if(rss.channel.itunes_image != null &&
				rss.channel.itunes_image.equals("") == false) {
//			String path = RequestUtil.loadThumbnail(context, rss.channel.itunes_image);			
//			channel.setThumbnail(path);			
			channel.setLoadedImage(true);
			channel.setThumbnailUrl(rss.channel.itunes_image);
		}
		
		return rss.feeds;
		
	}
}
