package com.airplug.audioplug.player;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import android.util.Log;

public class AudioListSHOUTcast extends AudioList {
	/*
	[playlist]
	numberofentries=4
	File1=http://203.150.225.77:8100
	Title1=(#1 - 27/10000) COOLfahrenheit 93
	Length1=-1
	File2=http://203.150.225.77:8000
	Title2=(#2 - 29/10000) COOLfahrenheit 93
	Length2=-1
	File3=http://203.150.225.71:8100
	Title3=(#3 - 2025/10000) COOLfahrenheit 93
	Length3=-1
	File4=http://203.150.225.71:8000
	Title4=(#4 - 12645/10000) COOLfahrenheit 93
	Length4=-1
	Version=2
	*/

	@Override
	public void read(List<AudioFile> list, AudioFile entry) throws IOException {
		String text = request(entry.url);
		
		if(text != null && text.startsWith("[playlist]")) {
			type = "SHOUTcast";
			
			Scanner scanner = new Scanner(text);
			
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine();

				try {
					if(line.startsWith("numberofentries")) {
						numberofentries = Integer.parseInt(line.split("=")[1]);
					} else if(line.startsWith("File")) {
						
						int idx = Integer.parseInt("" + line.charAt(4));
						list.add(idx - 1, new AudioFile(entry));
						list.get(idx - 1).url = line.split("=")[1];

					} else if(line.startsWith("Title")) {

						int idx = Integer.parseInt("" + line.charAt(5));
						list.get(idx - 1).title = line.split("=")[1];

					} else if(line.startsWith("Length")) {

						int idx = Integer.parseInt("" + line.charAt(6));
						list.get(idx - 1).length = Integer.parseInt(line.split("=")[1]);

					} else if(line.startsWith("Version")) {

						version = Integer.parseInt(line.split("=")[1]);
					}
				} catch (NumberFormatException e) {
					Log.e(NAME, "jude, " + e);
				}
			}
		}
	}
}
