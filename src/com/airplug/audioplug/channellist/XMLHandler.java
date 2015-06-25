package com.airplug.audioplug.channellist;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;


public class XMLHandler extends DefaultHandler {

	private static final String NAME = "XMLHandler";
	private final String CLASS = NAME + "@" + Integer.toHexString(hashCode());
	
	private final RSSFile rss;
	private String qName;
	
	public XMLHandler() {
		rss = new RSSFile();
	}
	
	public RSSFile getRSS() {
		return rss;
	}
	
	public void foo() {
		Log.d(CLASS, "");
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
//		RDLog.v(TAG, ">> " + localName + " " + qName + " " + attributes.getLength());
		this.qName = qName;
		rss.begin(qName, attributes);
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
//		RDLog.v(TAG, "<< " + localName + " " + qName);
		rss.end(qName);
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		rss.read(qName, new String(ch, start, length));
	}
}
