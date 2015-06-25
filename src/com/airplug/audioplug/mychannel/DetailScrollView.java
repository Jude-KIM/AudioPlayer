package com.airplug.audioplug.mychannel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

public class DetailScrollView extends ScrollView {
	private GestureDetector gesture_detector_;
	public DetailScrollView(Context context) {
		super(context);
		init();
	}

	public DetailScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public DetailScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		gesture_detector_ = new GestureDetector(getContext(), new SimpleGestureListener());
	}
	
	
	@Override 
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		if (on_up_down_scroll_listener_ != null) {
			on_up_down_scroll_listener_.onScroll(this, getScrollY());			
			if (t == oldt) {
				
			}
			else if (t < oldt) {
				if (oldt - t > 6) {
					on_up_down_scroll_listener_.onScroll(true);
				}
			}
			else {
				if (t - oldt > 6) {
					on_up_down_scroll_listener_.onScroll(false);
				}
			}
		}
	}
	
	private final class SimpleGestureListener extends GestureDetector.SimpleOnGestureListener {
	      // Implementation
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			return super.onScroll(e1, e2, distanceX, distanceY);
		}
	}
	
	public interface OnUpDownScrollListener {
		public void onScroll(boolean is_up);
		public void onScroll(View view, int position);
	};
	
	private OnUpDownScrollListener on_up_down_scroll_listener_;
	
	public void setOnUpDownScrollListner(OnUpDownScrollListener onUpDownScrollListener) {
		on_up_down_scroll_listener_ = onUpDownScrollListener;
	}
}
