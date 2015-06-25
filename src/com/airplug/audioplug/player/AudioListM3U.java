package com.airplug.audioplug.player;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class AudioListM3U extends AudioList {
	/*
	http://87.230.101.49:80/top100station.mp3
	http://87.230.101.50:80/top100station.mp3
	http://91.250.76.18:80/top100station.mp3
	http://87.230.101.16:80/top100station.mp3
	http://87.230.101.56:80/top100station.mp3
	http://87.230.101.12:80/top100station.mp3
	 */

	@Override
	public void read(List<AudioFile> list, AudioFile entry) throws IOException {
		String text = request(entry.url);
		
		if(text != null) {
			type = "m3u";

			Scanner scanner = new Scanner(text);

			while(scanner.hasNextLine()) {
				String line = scanner.nextLine();
				
				if(0 < line.trim().length()) {
					AudioFile e = new AudioFile(entry);
					e.url = line;
					list.add(e);
				}
			}
		}
	}
}
