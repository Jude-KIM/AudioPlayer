/*
 *Copyright linuxs by Airplug.,
 *All rights reserved.
 *Airplug.com
*/
package com.airplug.audioplug.util;

public interface ReceiveTaskEvent {
	void updateUI();
	void updateDB(boolean isDBInsert);
}
