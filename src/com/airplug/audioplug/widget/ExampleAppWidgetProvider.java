/*
 *Copyright linuxs by Airplug.,
 *All rights reserved.
 *Airplug.com
*/
package com.airplug.audioplug.widget;

import java.util.ArrayList;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.widget.RemoteViews;

import com.airplug.audioplug.MainActivity;
import com.airplug.audioplug.data.ConstData;
import com.airplug.audioplug.dev.R;
import com.airplug.audioplug.player.AudioFile;
import com.airplug.audioplug.player.AudioPlayer.State;
import com.airplug.audioplug.player.PlayInfo;
import com.airplug.audioplug.player.ServiceManager;

public class ExampleAppWidgetProvider extends AppWidgetProvider {

	private ServiceManager srvManager;
	private int currentPosition;
	private Context context;
	private int position;

	public ExampleAppWidgetProvider() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onEnabled(Context context) {
		PackageManager pm = context.getPackageManager();
		pm.setComponentEnabledSetting(
				new ComponentName(".templete", ".ExampleAppWidgetProvider"),
				PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
				PackageManager.DONT_KILL_APP
		);
		srvManager = new ServiceManager(context);
			
	
		this.context = context;

	}
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		for(int cnt = 0; cnt < appWidgetIds.length; cnt++) {
			int appWidgetId = appWidgetIds[cnt];

            // Create an Intent to launch ExampleActivity
//            Intent intent = new Intent(context, MainActivity.class);
//            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.player_view_widget);
//            views.setOnClickPendingIntent(R.id.buttonPlay, pendingIntent);
        	if (srvManager.isPlaying()) {
        		views.setImageViewResource(R.id.buttonPlay, R.drawable.ic_action_pause);
    		} else {
    			views.setImageViewResource(R.id.buttonPlay, R.drawable.ic_action_play);
    		}

//            views.setTextViewText(R.id.message_window, "An Test Message");
//            appWidgetManager.updateAppWidget(appWidgetId, views);
		}
//		super.onUpdate(context, appWidgetManager, appWidgetIds);
	};
	
	
//    static void updateWidgetState(Context paramContext, String paramString)
//    {
//      RemoteViews localRemoteViews = buildUpdate(paramContext, paramString); //CALL HERE
//      ComponentName localComponentName = new ComponentName(paramContext, TestwidgetProvider.class);
//      AppWidgetManager.getInstance(paramContext).updateAppWidget(localComponentName, localRemoteViews);
//    }

	
	@Override
	public void onReceive(Context context, Intent intent) {
		String str = intent.getAction();
		playOrStop();
		RemoteViews rview = new RemoteViews(context.getPackageName(),R.layout.player_view_widget);

			if (srvManager.isPlaying()) {
				rview.setImageViewResource(R.id.buttonPlay, R.drawable.ic_action_pause);
			} else {
				rview.setImageViewResource(R.id.buttonPlay, R.drawable.ic_action_play);
			}
		

//		if (intent.getAction().equals(TAG_IMAGE_CLICK)) {
//			updateWidgetState(context, str);
//		} else {
//			super.onReceive(context, intent);
//		}
	}

//	static void updateWidgetState(Context paramContext, String paramString) {
//		RemoteViews localRemoteViews = buildUpdate(paramContext, paramString); // CALL
//																				// HERE
//		ComponentName localComponentName = new ComponentName(paramContext,
//				TestwidgetProvider.class);
//		AppWidgetManager.getInstance(paramContext).updateAppWidget(
//				localComponentName, localRemoteViews);
//	}

//	private static RemoteViews buildUpdate(Context paramContext,
//			String paramString) {
//		rview = new RemoteViews(paramContext.getPackageName(),
//				R.layout.widget_layoutmain);
//		Intent active = new Intent(paramContext, TestwidgetProvider.class); // YOUR
//																			// WIDGET
//																			// NAME
//		active.setAction(TAG_IMAGE_CLICK);
//		PendingIntent actionPendingIntent = PendingIntent.getBroadcast(
//				paramContext, 0, active, 0);
//		rview.setOnClickPendingIntent(R.id.MyWidgetTxt, actionPendingIntent);
//		if (paramString.equals(TAG_IMAGE_CLICK)) {
//			if (ii == 0) {
//				rview.setImageViewResource(R.id.ImageView01a, R.drawable.off);
//				ii = 1;
//			} else {
//				rview.setImageViewResource(R.id.ImageView01a, R.drawable.on);
//				ii = 0;
//			}
//		}
//		return rview;
//	}

	private void playOrStop() {
		

		if (srvManager.isPlaying()) {
			srvManager.pause();
			if(srvManager.getService() != null){
				currentPosition = srvManager.getService().getCurrentPosition();
			}
//			views.
//			playMenu.setIcon(R.drawable.ic_action_play);

		} 
		else {
			if (srvManager.getService() == null) {
				SharedPreferences prefs = context.getSharedPreferences(
						ConstData.CurrentPlayPrefs, context.MODE_PRIVATE);
				String url = prefs.getString("url", "");
				String thumbnail = prefs.getString("thumbnail", "");
				String channelTitle = prefs.getString("channeltitle", "");
				String title = prefs.getString("title", "");
				int length = prefs.getInt("length", 0);
				String author = prefs.getString("author", "");
				String description = prefs.getString("description", "");
				String type = prefs.getString("type", "");
				String link = prefs.getString("link", "");
				String pubDate = prefs.getString("pubDate", "");
				AudioFile entry = new AudioFile(url, title, channelTitle, position, type, description,
						author, link, pubDate, thumbnail, length, State.IDLE);

				ArrayList<AudioFile> palylist = new ArrayList<AudioFile>();
				palylist.add(entry);
				PlayInfo pInfo = new PlayInfo();				
				pInfo.playlist = palylist;

//				srvManager.start(pInfo);
			} 
			else {
				currentPosition = srvManager.getService().getCurrentPosition();
				srvManager.seekTo(currentPosition);
				srvManager.play();
			}
		}
		
	}
	
	
}

