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

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.airplug.audioplug.MainActivity;
import com.airplug.audioplug.data.ConstData;
import com.airplug.audioplug.dev.R;
import com.airplug.audioplug.player.AudioFile;
import com.airplug.audioplug.player.AudioService;
import com.airplug.audioplug.player.IAudioService;
import com.airplug.audioplug.player.ServiceManager;
import com.airplug.audioplug.util.ImageCache;

/**
 * 1x4 widget that shows title, artist, album art, a play/pause button, and a
 * next button.
 */
public class FourLongWidget extends AppWidgetProvider {
	private static boolean sEnabled;
	private static ServiceManager srvManager;
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
		int state = 0;

		ServiceManager srvManager = new ServiceManager(context);
		service = srvManager.getService();
		if (service != null) {
			song = service.getAudioEntry();
			state = service.getState();
		}
		sEnabled = true;
		updateWidget(context, manager, song, state);
	}

	/**
	 * Check if there are any instances of this widget placed.
	 */
	public static void checkEnabled(Context context, AppWidgetManager manager)
	{
		sEnabled = manager.getAppWidgetIds(new ComponentName(context, FourLongWidget.class)).length != 0;
	}

	/**
	 * Populate the widgets with the given ids with the given info.
	 *
	 * @param context A Context to use.
	 * @param manager The AppWidgetManager that will be used to update the
	 * widget.
	 * @param song The current Song in AudioService.
	 * @param state The current AudioService state.
	 */
	public static void updateWidget(Context context, AppWidgetManager manager, AudioFile song, int state)
	{
		
		Log.d("updateWidget:", ""+state+" "+sEnabled);

		if (!sEnabled)
			return;
		
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.four_long_widget);
		Log.d("updateWidget:", ""+state);

		if (state == ConstData.FLAG_NO_MEDIA) {
			views.setViewVisibility(R.id.play_pause, View.GONE);
			views.setViewVisibility(R.id.next, View.GONE);
			views.setViewVisibility(R.id.title, View.GONE);
			views.setInt(R.id.artist, "setText", R.string.no_songs);
			views.setViewVisibility(R.id.cover, View.GONE);
		} else if (song == null) {
			views.setViewVisibility(R.id.play_pause, View.VISIBLE);
			views.setViewVisibility(R.id.next, View.VISIBLE);
			views.setViewVisibility(R.id.title, View.GONE);
			views.setInt(R.id.artist, "setText", R.string.app_name);
			views.setViewVisibility(R.id.cover, View.GONE);
		} else {
			views.setViewVisibility(R.id.play_pause, View.VISIBLE);
			views.setViewVisibility(R.id.next, View.VISIBLE);
			views.setViewVisibility(R.id.title, View.VISIBLE);
			views.setTextViewText(R.id.title, song.title);
			views.setTextViewText(R.id.artist, song.channelTitle);
			Log.d("updateWidget", song.thumbnail+state);
			ImageCache imgCahce = ImageCache.getInstance(context);
			views.setViewVisibility(R.id.cover, View.VISIBLE);
			imgCahce.getImageLoader().loadImage(song.thumbnail, null, imgCahce.getOption(), imgCahce.getListener(views, R.id.cover));
//			if (cover == null) {
//				views.setViewVisibility(R.id.cover, View.GONE);
//			} else {			
//				views.setViewVisibility(R.id.cover, View.VISIBLE);
//				imgCahce.getImageLoader().loadImage(song.thumbnail, null, imgCahce.getOption(), imgCahce.getListener(views, R.id.cover));
////				views.setImageViewBitmap(R.id.cover, cover);
//			}
			
			
		}

		if(state == ConstData.FLAG_PLAYING){
			views.setImageViewResource(R.id.play_pause,  R.drawable.pause);
		}else if(state == ConstData.FLAG_PAUSE){
			views.setImageViewResource(R.id.play_pause, R.drawable.play);
		}
		Intent intent;
		PendingIntent pendingIntent;

		ComponentName service = new ComponentName(context, AudioService.class);

		intent = new Intent(context, MainActivity.class);
		intent.setAction(Intent.ACTION_MAIN);
		pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
		views.setOnClickPendingIntent(R.id.cover, pendingIntent);
		views.setOnClickPendingIntent(R.id.text_layout, pendingIntent);

		intent = new Intent(AudioService.ACTION_TOGGLE).setComponent(service);
		pendingIntent = PendingIntent.getService(context, 0, intent, 0);
		views.setOnClickPendingIntent(R.id.play_pause, pendingIntent);

		intent = new Intent(AudioService.ACTION_NEXT).setComponent(service);
		pendingIntent = PendingIntent.getService(context, 0, intent, 0);
		views.setOnClickPendingIntent(R.id.next, pendingIntent);

		manager.updateAppWidget(new ComponentName(context, FourLongWidget.class), views);
	}
}
