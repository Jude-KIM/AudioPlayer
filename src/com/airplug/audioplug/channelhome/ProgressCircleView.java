package com.airplug.audioplug.channelhome;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.lang.Math;
import java.lang.Override;

/**
 * Show progress by circle shape 
 * 
 * @author brownsoo
 *
 */
public class ProgressCircleView extends View {
    
	private float maxValue;
	private float currValue;
	private Paint backPaint;
	private Paint coverPaint;
	private RectF coverBound;
	private RectF backBound;
	private boolean channelmark = false;
	private int coverColor;
	private int backColor;
	private float coverWidth;
	private float backWidth;
	
    public ProgressCircleView(Context context) {
        this(context, null);
    }
    
    public ProgressCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        maxValue = 100f;
        currValue = 50f;
        coverColor = 0xff4bcf8f;
        backColor = 0xffcccccc;
        coverWidth = convertDpPixel(4);
        backWidth = convertDpPixel(2);

        coverBound = new RectF();
        backBound = new RectF();
        
        backPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backPaint.setStyle(Paint.Style.STROKE);
        backPaint.setColor(backColor);
        backPaint.setStrokeWidth(backWidth);
        
        coverPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        coverPaint.setStyle(Paint.Style.STROKE);
        coverPaint.setColor(coverColor);
        coverPaint.setStrokeWidth(coverWidth);
        
        
        
    }
    
    private float convertDpPixel(int dp){
        final Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        return px;
    }
    
    
    public void setCoverColor(int color) {
    	this.coverPaint.setColor(color);
    	this.coverColor = color;
    	postInvalidate();
    }
    
    /**
     * 채널 이미지만 표시할 것인지.
     * @param bool true 이면 채널 'c' 이미지만 표시, false 이면 프로그레스 표시 
     */
    public void setChannelMark(boolean bool) {
    	channelmark = bool;
    	postInvalidate();
    }
    
    
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // Do nothing. Do not call the superclass method--that would start a layout pass
        // on this view's children. PieChart lays out its children in onSizeChanged().
    }


    @Override
    protected void onDraw(Canvas canvas) {
    	    	
    	if(channelmark) {
    		backPaint.setStrokeWidth(coverWidth);
    		backPaint.setColor(0xffa8a8a8);
	    	canvas.drawArc(backBound, 50, 260, false, backPaint);
    	}
    	else {
    		backPaint.setStrokeWidth(backWidth);
    		backPaint.setColor(backColor);
    		canvas.drawOval(backBound, backPaint);
    		canvas.drawArc(coverBound, -90, 360 * (currValue/maxValue), false, coverPaint);
    	}
    	
        super.onDraw(canvas);
        
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	
        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int w = Math.max(minw, MeasureSpec.getSize(widthMeasureSpec));
        
        int minh = getPaddingBottom() + getPaddingTop();
        int h = Math.max(minh, MeasureSpec.getSize(heightMeasureSpec));

        setMeasuredDimension(w, h);
    }
    

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        
        // Account for padding
        float xpad = (float) (getPaddingLeft() + getPaddingRight());
        float ypad = (float) (getPaddingTop() + getPaddingBottom());

        float ww = (float) w - xpad;
        float hh = (float) h - ypad;
        float size = Math.min(ww, hh);

        // Figure out how big we can make the pie.
        backBound = new RectF(
        		0.0f,
                0.0f,
                size - coverPaint.getStrokeWidth(),
                size - coverPaint.getStrokeWidth());
        backBound.offsetTo(getPaddingLeft() + coverPaint.getStrokeWidth() / 2, getPaddingTop() + coverPaint.getStrokeWidth() / 2);
        
        coverBound = new RectF(
        		0.0f, 
        		0.0f, 
        		size - coverPaint.getStrokeWidth(), 
        		size - coverPaint.getStrokeWidth());
        coverBound.offsetTo(getPaddingLeft() + coverPaint.getStrokeWidth() / 2, getPaddingTop() + coverPaint.getStrokeWidth() / 2);
        
    }
    
	public float getMaxValue() {
		return maxValue;
	}

	/**
	 * Set maxValue, default 100
	 * @param maxValue
	 */
	public void setMaxValue(float maxValue) {
		this.maxValue = maxValue;
		postInvalidate();
	}
	
	public float getValue(){
		return this.currValue;
	}
	
	/**
	 * @param currValue 
	 */
	public void setValue(float currValue) {
		this.currValue = currValue;
		postInvalidate();
	}

}
