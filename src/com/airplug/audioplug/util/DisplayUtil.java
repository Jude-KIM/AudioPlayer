package com.airplug.audioplug.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class DisplayUtil {

	private static final float DEFAULT_HDIP_DENSITY_SCALE = 1.5f;

	public static int DPFromPixel(Context context, int pixel)
	{
		float scale = context.getResources().getDisplayMetrics().density;

		return (int)(pixel / DEFAULT_HDIP_DENSITY_SCALE * scale);
	}


	public static int PixelFromDP(Context context, int DP)
	{
		float scale = context.getResources().getDisplayMetrics().density;		
		return (int)(DP / scale * DEFAULT_HDIP_DENSITY_SCALE);
	}

	public static Bitmap decodeSampledBitmapFromFile(String path,
			int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(path, options);
	}

	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
			int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	public static int calculateInSampleSize(
			BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and width
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}
	
	
	
	public static Bitmap applyGaussianBlur(Bitmap src, int size) {
		  //set gaussian blur configuration
		  double[][] GaussianBlurConfig = new double[][] {
		    { 1, 2, 1 },
		    { 2, 4, 2 },
		    { 1, 2, 1 }
		  };
		  // create instance of Convolution matrix
		  ConvolutionMatrix convMatrix = new ConvolutionMatrix(size);
		  // Apply Configuration
		  convMatrix.applyConfig(GaussianBlurConfig);
		  convMatrix.Factor = 16;
		  convMatrix.Offset = 0;
		  //return out put bitmap
		  return ConvolutionMatrix.computeConvolution3x3(src, convMatrix);
	 }
}
