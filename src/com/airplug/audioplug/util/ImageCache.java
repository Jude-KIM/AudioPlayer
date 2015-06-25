/*
 *Copyright linuxs by Airplug.,
 *All rights reserved.
 *Airplug.com
*/
package com.airplug.audioplug.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.RemoteViews;

import com.airplug.audioplug.dev.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class ImageCache {

	public DisplayImageOptions options;
	static ImageCache instance;
	protected ImageLoader imageLoader = ImageLoader.getInstance();

	public ImageCache(Context ctx) {
		super();
		
		options = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.ic_channel_default)
//		.showImageOnFail(R.drawable.ic_launcher_web)
//		.resetViewBeforeLoading(true)
		.cacheOnDisc(true)
		.cacheInMemory(true)
		.imageScaleType(ImageScaleType.EXACTLY)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.displayer(new FadeInBitmapDisplayer(300))
		.build();
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(ctx)
		.threadPriority(Thread.NORM_PRIORITY - 2)
		.denyCacheImageMultipleSizesInMemory()
		.discCacheFileNameGenerator(new Md5FileNameGenerator())
		.tasksProcessingOrder(QueueProcessingType.LIFO)
//		.writeDebugLogs() // Remove for release app
		.build();
		imageLoader.init(config);
	}
	

	public static ImageCache getInstance(Context ctx){
		if(instance == null){
			instance = new ImageCache(ctx);
		}
		return instance;
	}
	public DisplayImageOptions getOption(){
		return options;
	}
	public ImageLoader getImageLoader(){
		return ImageLoader.getInstance();
	}
	
	public SimpleImageLoadingListener getListener(final RemoteViews rv, final int resId){
		return new SimpleImageLoadingListener(){
			@Override
			public void onLoadingComplete(String imageUri,
	                View view, Bitmap loadedImage) {
	            super.onLoadingComplete(imageUri, view,loadedImage);
	            rv.setImageViewBitmap(resId, loadedImage);	                
	        }
		};
	}
}
