/*
 * Copyright (C) 2010, 2011 Christopher Eby <kreed@kreed.org>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.airplug.audioplug.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.widget.RemoteViews;

import com.airplug.audioplug.dev.R;
import com.airplug.audioplug.player.AudioFile;
import com.airplug.audioplug.player.IAudioService;
import com.airplug.audioplug.player.ServiceManager;

/**
 * 1x1 widget that shows title, album art, and hidden next and play/pause
 * buttons.
 */
public class OneCellWidget extends AppWidgetProvider {
	private static boolean sEnabled;
	private IAudioService service;

	@Override
	public void onEnabled(Context context)
	{
		sEnabled = true;
	}

	@Override
	public void onDisabled(Context context)
	{
		sEnabled = false;
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager manager, int[] ids)
	{
		AudioFile song = null;
		boolean state = false ;
		ServiceManager srvManager= new ServiceManager(context);
		service = srvManager.getService();
		if (service != null) {
//			PlaybackService service = PlaybackService.get(context);
			song = service.getAudioEntry();
			state = service.isPlaying();
		}


		sEnabled = true;
		updateWidget(context, manager, song, state);
	}

	/**
	 * Check if there are any instances of this widget placed.
	 */
	public static void checkEnabled(Context context, AppWidgetManager manager)
	{
		sEnabled = manager.getAppWidgetIds(new ComponentName(context, OneCellWidget.class)).length != 0;
	}

	/**
	 * Populate the widgets with the given ids with the given info.
	 *
	 * @param context A Context to use.
	 * @param manager The AppWidgetManager that will be used to update the
	 * widget.
	 * @param song The current Song in PlaybackService.
	 * @param state The current PlaybackService state.
	 */
	public static void updateWidget(Context context, AppWidgetManager manager, AudioFile song, boolean state)
	{
		if (!sEnabled)
			return;

//		SharedPreferences settings = PlaybackService.getSettings(context);
//		boolean doubleTap = settings.getBoolean("double_tap", false);
//
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.one_cell_widget);
//
//		boolean playing = (state & PlaybackService.FLAG_PLAYING) != 0;
//		views.setImageViewResource(R.id.play_pause, playing ? R.drawable.hidden_pause : R.drawable.hidden_play);
//
//		ComponentName service = new ComponentName(context, PlaybackService.class);
//
//		Intent playPause = new Intent(doubleTap ? PlaybackService.ACTION_TOGGLE_PLAYBACK_DELAYED : PlaybackService.ACTION_TOGGLE_PLAYBACK);
//		playPause.setComponent(service);
//		views.setOnClickPendingIntent(R.id.play_pause, PendingIntent.getService(context, 0, playPause, 0));
//
//		Intent next = new Intent(doubleTap ? PlaybackService.ACTION_NEXT_SONG_DELAYED : PlaybackService.ACTION_NEXT_SONG);
//		next.setComponent(service);
//		views.setOnClickPendingIntent(R.id.next, PendingIntent.getService(context, 0, next, 0));
//
//		Bitmap cover = null;
//		if ((state & PlaybackService.FLAG_NO_MEDIA) != 0) {
//			views.setInt(R.id.title, "setText", R.string.no_songs);
//		} else if (song == null) {
//			views.setInt(R.id.title, "setText", R.string.app_name);
//		} else {
//			views.setTextViewText(R.id.title, song.title);
//			cover = song.getCover(context);
//		}
//
//		if (cover == null) {
//			views.setImageViewResource(R.id.cover, R.drawable.fallback_cover);
//		} else {
//			views.setImageViewBitmap(R.id.cover, cover);
//		}

		manager.updateAppWidget(new ComponentName(context, OneCellWidget.class), views);
	}
}
