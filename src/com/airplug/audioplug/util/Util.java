package com.airplug.audioplug.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.airplug.audioplug.dev.R;

/**
 * ?�이브러�??�수 묶음
 * @author 1Ticket
 *
 */
public class Util {
	public static int mColor;
	private static final float DEFAULT_HDIP_DENSITY_SCALE = 1.5f;
	public static ShapeDrawable setNetworkCircle(int alpha, int color){
		ShapeDrawable sd = new ShapeDrawable(new OvalShape());
		
		mColor = color;
		
		Paint paint = sd.getPaint();
		paint.setStyle(Paint.Style.FILL);
		paint.setStrokeWidth(3);
		paint.setColor(color);
		paint.setAlpha(alpha);
		return sd;
	}
	public static String numberToTUnitComma(int number) {
		DecimalFormat df = new DecimalFormat("#,##0");
		return df.format(number);
	}
	
	
	public static String convertStreamToString(InputStream is)
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try
        {
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
        	try {
                is.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
	
	
	/**
	 * ?�텐???�우�?
	 * @param c
	 * @param context
	 * @param isActivityFinish
	 */
	public static void startActivity(Class<?> c, Context context, boolean isActivityFinish)
	{
		Intent intent = new Intent(context, c);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		((Activity)context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
		((Activity)context).startActivity(intent);
		if(isActivityFinish)
			((Activity)context).finish();
	}
	
	public static void startActivity(Context context, Intent intent, boolean isActivityFinish)
	{
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		((Activity)context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
		((Activity)context).startActivity(intent);
		if(isActivityFinish)
			((Activity)context).finish();
	}
	
	public static  void startActivity(Intent intent, Context context, boolean isActivityFinish)
	{
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		((Activity)context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
		((Activity)context).startActivity(intent);
		if(isActivityFinish)
			((Activity)context).finish();
	}
	
	public static void startActivityForResult(Class<?> c, Context context, int requestCode)
	{
		Intent intent = new Intent(context, c);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		((Activity)context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
		((Activity)context).startActivityForResult(intent, requestCode);
	}

	public static void finishActivity(final Context c) {
		/*
		AlertDialog.Builder alert = new AlertDialog.Builder(c);
		alert.setTitle(c.getResources().getString(R.string.app_name));
		alert.setMessage(c.getResources().getString(R.string.appFinish));
		alert.setIcon(R.drawable.icon);
		alert.setCancelable(false);
		alert.setPositiveButton(c.getResources().getString(R.string.yes), new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				((Activity) c).finish();
			}
		});
		alert.setNegativeButton(c.getResources().getString(R.string.no), new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		alert.show();
		*/
	}
	
	/**
	 * 바이??길이 �?��
	 * @param szStr
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static int getString2Byte(String szStr) throws UnsupportedEncodingException 
	{
		byte nByte[]	=	szStr.getBytes();
		return nByte.length;
	}
	
	/**
	 * 문자??바이?�수 ?�한
	 * strcut('?�본 문자??,null,?�한??문자??0, ?�그?�출?��?,마침?�노출여�?
	 * @param szText
	 * @param szKey
	 * @param nLength
	 * @param nPrev
	 * @param isNotag
	 * @param isAdddot
	 * @return
	 */
	public String strCut(String szText, String szKey, int nLength, int nPrev, boolean isNotag, boolean isAdddot)
	{
		String	r_val = szText;
		int	oF = 0, oL = 0, rF = 0, rL = 0;
		int nLengthPrev = 0;
		Pattern p = Pattern.compile("<(/?)([^<>]*)?>", Pattern.CASE_INSENSITIVE);  // ?�그?�거 ?�턴
		if(isNotag) {r_val = p.matcher(r_val).replaceAll("");}	// ?�그 ?�거
		r_val = r_val.replaceAll("&amp;", "&");
		r_val = r_val.replaceAll("(!/|\r|\n|&nbsp;)", "");		// 공백?�거

		try
		{
			byte[] bytes = r_val.getBytes("UTF-8");		// 바이?�로 보�?
			if(szKey != null && !szKey.equals(""))
			{
				nLengthPrev = (r_val.indexOf(szKey) == -1)? 0: r_val.indexOf(szKey);
				nLengthPrev = r_val.substring(0, nLengthPrev).getBytes("MS949").length;	// ?�치까�?길이�?byte�??�시 구한??
				nLengthPrev = (nLengthPrev-nPrev >= 0)? nLengthPrev-nPrev:0;
			}
			int j = 0;
			if(nLengthPrev > 0) while(j < bytes.length)
			{
				if((bytes[j] & 0x80) != 0)
				{
					oF+=2; rF+=3;
					if(oF+2 > nLengthPrev)	{	break;	}
					j+=3;
				}
				else
				{
					if(oF+1 > nLengthPrev)	{	break;	}
					++oF; ++rF; ++j;
				}
			}
			j = rF;
			while(j < bytes.length)
			{
				if((bytes[j] & 0x80) != 0)
				{
					if(oL+2 > nLength)	{	break;	}
					oL+=2; rL+=3; j+=3;
				}
				else
				{
					if(oL+1 > nLength)	{	break;	}
					++oL; ++rL; ++j;
				}
			}
			r_val = new String(bytes, rF, rL, "UTF-8");  // charset ?�션
			if(isAdddot && rF+rL+3 <= bytes.length)r_val+="...";
		}
		catch(UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return r_val;
	}
	
	private static ProgressDialog mProgressDialog;
	
	/**
	 * ?�로그레???�이?�로�?show
	 * @param c
	 */
	public static void showProgressDialog(Context c) 
	{
		if( mProgressDialog != null )
			mProgressDialog.cancel();
		
		mProgressDialog = ProgressDialog.show(c, "", "test", true );
	}
	
	public static void showProgressDialog(Context c, String message) 
	{
		if( mProgressDialog != null )
			mProgressDialog.cancel();
		
		mProgressDialog = ProgressDialog.show(c, "", message, true );
	}
	
	/**
	 * ?�로그레???�이?�로�?hide
	 */
	public static void hideProgressDialog() 
	{
		if (mProgressDialog != null) 
		{
			mProgressDialog.cancel();
			mProgressDialog = null;
		}
	}
	
	/**
	 * ?�이?�로�?show
	 * @param context
	 * @param strMessage
	 */
	public static void showDialog(Context context, String strMessage) {
		AlertDialog.Builder alert = new AlertDialog.Builder(context);
		alert.setTitle("AudioPlug");
		alert.setMessage(strMessage);
		//alert.setIcon(R.drawable.ic_launcher_web);
		alert.setCancelable(false);
		alert.setPositiveButton("OK",
				new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		alert.show();
	}
	
	public static boolean is3GConnected(Context context) {
		ConnectivityManager cManager;
		NetworkInfo mobile;
		 
		cManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		mobile = cManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		//wifi = cManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if(mobile != null){
			if(mobile.isConnected()){
				return true;
			}
		}
		return false; 
	}
	
	
	/**
	 * toast msg
	 * @param context
	 * @param str
	 */
	
	public static void showToast(Context context, String str) 
	{
		Toast toast = Toast.makeText(context, str, Toast.LENGTH_LONG);
		toast.show();
	}
	
	public static void showToast(Context context, String str, int gravity)
	{
		showToast(context, str, gravity, 0, 0);
	}
	
	public static void showToast(Context context, String str, int gravity, int xOffset, int yOffset)
	{
		Toast toast = Toast.makeText(context, str, Toast.LENGTH_LONG);
		toast.setGravity(gravity, xOffset, yOffset);
		toast.show();
	}
	
	/**
	 * remove html tag 
	 * @param str
	 * @return
	 */
	public static String htmlRemove(String str)
	{
		String	textWithoutTag	=	str.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
		return  textWithoutTag;
	}
	
	
	public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
	
	/**
	 * ?�이�???��??pixel to DP �?��
	 * @param context
	 * @param nPixel
	 * @return
	 */
	public static int getPixelToDP(Context context,int nPixel)
	{
		float	d		=	context.getResources().getDisplayMetrics().density;
		int		nDP		=	(int)(nPixel * d);
		return	nDP;
	}
	
	/**
	 * ?�용 ?�원 초기??
	 * @param root
	 */
	
	 public static void recursiveRecycle(View root) {
			if (root == null)
				return;
			root.setBackgroundDrawable(null);
			if (root instanceof ViewGroup) {
				ViewGroup group = (ViewGroup)root;
				int count = group.getChildCount();
				for (int i = 0; i < count; i++) {
					recursiveRecycle(group.getChildAt(i));
				}
				
				if (!(root instanceof AdapterView)) {
					group.removeAllViews();
				}
				
			}
			if (root instanceof ImageView) {
				((ImageView)root).setImageDrawable(null);
			}
			root = null;
			return;
	    }
	
	public static void recycleBitmap(ImageView iv) {
		Drawable d = iv.getDrawable();
		if (d == null)
			return ;
		if (d instanceof BitmapDrawable) {
			Bitmap b = ((BitmapDrawable)d).getBitmap();
			b.recycle();
		} // ?�재로서??BitmapDrawable ?�외??drawable ?�에 ??�� 직접?�인 메모�??�제??불�??�하?? 
		d.setCallback(null);
	}	
	
	
	public static void hideKeyboard( View view )
	{
	      InputMethodManager mgr = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
	      //mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
	      mgr.hideSoftInputFromInputMethod(null, 0);
	}
	

	public static String getTvpotVideoId(String movieUrl) {
		String videoId = getQueryValue(getQueryMap(getQuery(movieUrl)), "clipid");
		//clipid
		return videoId;
	}
	
	private static String getQuery(String url) {
		String query = null;
		if(url != null) {
			String[] segment = url.split("#");
			if(segment != null && 0 < segment.length) {
				query = "";
				for(int n = 0; n < segment.length; n++) {
					int index = segment[n].indexOf('?');
					if(0 <= index && 1 < segment[n].length()) {
						if(0 < n) query += "&";
						query += segment[n].substring(index + 1);
					}
				}
			}
		}
		return query;
	}

	private static Map<String, String> getQueryMap(String query) {
		Map<String, String> map = null;
		if(query != null && 0 < query.length()) {
			map = new HashMap<String, String>();
			String[] params = query.split("&");
			for(String param: params) {
				String[] segment = param.split("=");
				if(1 < segment.length) {
					map.put(segment[0], segment[1]);
				}
			}
		}
		return map;
	}

	private static String getQueryValue(Map<String, String> map, String keyIn) {
		String value = null;
		if(map != null && keyIn != null) {
			Set<String> keys = map.keySet();
			for(String key: keys) {
				if(keyIn.equals(key)) {
					value = map.get(key);
					break;
				}
			}
		}
		return value;
	}
	
	public static InputStream getInputStream(URL url) {
		int count = 0;
		while (true) {
			try {
				URLConnection con = url.openConnection();
				con.setReadTimeout(1000 * 60 * 5);
				con.setConnectTimeout(1000 * 60 * 5);
				InputStream is = con.getInputStream();
				return is;
			} catch (Exception e) {
				//e.printStackTrace();
				count ++;
				
				VPLog.e("Util", "Util getInputStream Fail " + url.toString());
				VPLog.e("Util", "Util getInputStream Fail Count : " + count);
				
				if( count > 3 ){
					return null;
				}
			}
		}
	}
	
	public static String dateFormatConvert(String str_date, String source_format, String dest_format) {
		SimpleDateFormat sdfSource = new SimpleDateFormat(source_format, Locale.ENGLISH);		
		Date date;						
		try {
//			String pubDate = sdfSource.format(str_date);			
//			Log.d("test" ,"pubDate : " + pubDate);			
//			date = sdfSource.parse(pubDate);
//			Log.d("jude" ,"date : " + date);
			date = sdfSource.parse(str_date);			
		} catch (ParseException e) {
			e.printStackTrace();			
			return str_date;
		}
		SimpleDateFormat destSource = new SimpleDateFormat(dest_format);
		String str_dest_date = destSource.format(date);
		return str_dest_date;					
	}
	
	
	/**
	   * �ȼ������� ���� ���÷��� ȭ�鿡 ����� ũ��� ��ȯ�մϴ�.
	   * 
	   * @param pixel �ȼ�
	   * @return ��ȯ�� �� (DP)
	   */
	  public static int DPFromPixel(Context c, int pixel)
	  {
	    
	    float scale = c.getResources().getDisplayMetrics().density;	    
	    return (int)(pixel / DEFAULT_HDIP_DENSITY_SCALE * scale);
	  }
	
	  /**
	   * ���� ���÷��� ȭ�鿡 ����� DP������ �ȼ� ũ��� ��ȯ�մϴ�.
	   * 
	   * @param DP 
	   * @return ��ȯ�� �� (pixel)
	   */
	  public static int PixelFromDP(Context c, int DP)
	  {
	    
	    float scale = c.getResources().getDisplayMetrics().density;	    
	    return (int)(DP / scale * DEFAULT_HDIP_DENSITY_SCALE);
	  }	 
	
}



