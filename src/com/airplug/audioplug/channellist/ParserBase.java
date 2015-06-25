package com.airplug.audioplug.channellist;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.airplug.audioplug.channellist.RSSFile.FeedElements;

public abstract class ParserBase {	
	public abstract ArrayList<FeedElements> parse(Channel channel)
			throws ParserConfigurationException, SAXException, IOException;
}
