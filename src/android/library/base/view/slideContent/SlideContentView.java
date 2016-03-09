/**
 * 
 */
package android.library.base.view.slideContent;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

/**
 * @author hushihua
 * 
 */
public class SlideContentView extends FrameLayout {

	private final int PULL_DOWN_STATE = 1;
	private final int PULL_UP_STATE = 2;
	private float raw_y;
	private View contentView;
	private int currentState;
	private Scroller scroller;
	private SlideContentFooter footer;
	private SlideContentHeader header;
	private boolean isLoading = false, hasMore = true;
	private onSlideContentRefreshListener listener;

	public interface onSlideContentRefreshListener{
		public void onHeaderRefresh();
		public void onFooterRefresh();
	}
	
	public SlideContentView(Context context) {
		super(context);
		initView();
	}

	public SlideContentView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}
	
	private void initView() {
		scroller = new Scroller(getContext());
		footer = new SlideContentFooter(getContext());
		header = new SlideContentHeader(getContext());
	}
	
	public void setListener(onSlideContentRefreshListener listener){
		this.listener = listener;
	}
	
	public void canLoadMore(boolean hasMore){
		this.hasMore = hasMore;
	}
	
	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		addView(footer);
		addView(header);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		contentView = getChildAt(0);
		contentView.layout(0, 0, contentView.getMeasuredWidth(), contentView.getMeasuredHeight());
		header.layout(0, -header.getMeasuredHeight(), getMeasuredWidth(), 0);
		footer.layout(0, getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight() + footer.getMeasuredHeight());
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		Log.i("test", "action name:  " + event.getAction());
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			raw_y = event.getRawY();
			break;
			
		case MotionEvent.ACTION_MOVE:
//			Log.i("test", "-- MotionEvent.ACTION_MOVE --");
			if (onInterceptContentEvent(event)) {
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			
			break;
		}
		return false; // true 拦截事件, false 事件穿透
	}

	/**
	 * 
	 * @param event
	 * @return true 拦截事件, false 事件穿透
	 */
	private boolean onInterceptContentEvent(MotionEvent event) {
//		Log.i("test", "-- onInterceptContentEvent --");
		
		if (isLoading) {
			return false;
		}
		int offset_y = (int) (event.getRawY() - this.raw_y);
		if (contentView instanceof AdapterView<?>) {
			AdapterView<?> view = (AdapterView<?>) contentView;
			View child = view.getChildAt(0);
			if (child == null) {
				return false;
			}
			if (view.getFirstVisiblePosition() == 0 && child.getTop() == 0 && offset_y > 0) {//向下滑动
				currentState = PULL_DOWN_STATE;
				return true;
			}
			if (view.getLastVisiblePosition() == view.getCount() - 1 && offset_y < 0 && hasMore) {//向上滑动
				currentState = PULL_UP_STATE;
				return true;
			}
		} else if (contentView instanceof ScrollView) {
			ScrollView view = (ScrollView) contentView;
//			Log.i("test", "ScrollView total height: " + view.getChildAt(0).getHeight());
//			Log.i("test", "getScrollY: " + view.getScrollY());
			if (view.getScrollY() == 0 && offset_y > 0) {//向下滑动
				currentState = PULL_DOWN_STATE;
//				Log.i("test", "onInterceptContentEvent : PULL_DOWN_STATE");
				return true;
			} else if (view.getChildAt(0).getHeight() <= view.getHeight()+ view.getScrollY() && offset_y < 0 && hasMore) { //向上滑动
				currentState = PULL_UP_STATE;
//				Log.i("test", "getMeasuredHeight: " + view.getMeasuredHeight() + ", height: " + view.getHeight());
//				Log.i("test", "onInterceptContentEvent : PULL_UP_STATE");
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (isLoading) {
//			return super.onTouchEvent(event);
			return true;
		}
		int offset_y = (int) (event.getRawY() - this.raw_y);
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			Log.i("test", "-- ACTION_MOVEL --");
			if (currentState == PULL_DOWN_STATE) {
				if (Math.abs(offset_y) > header.getMeasuredHeight()) {
					offset_y = header.getMeasuredHeight();
				}
				updateFooterOrHeaderView(event);
				scrollTo(0, -offset_y);
			} else if (currentState == PULL_UP_STATE && hasMore) {
				if (Math.abs(offset_y) > footer.getMeasuredHeight()) {
					offset_y = -footer.getMeasuredHeight();
				}
				updateFooterOrHeaderView(event);
				scrollTo(0, -offset_y);
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			if (Math.abs((int) (event.getRawY() - this.raw_y)) > header.getMeasuredHeight() * 0.86) {
				scrollBackToLoad(Math.abs(offset_y));
			}else{
				scrollBack();
			}
			
			break;
		}
//		Log.i("test", "action name:  " + event.getAction());
		return super.onTouchEvent(event);
	}
	
	private void updateFooterOrHeaderView(MotionEvent event){
		if (isLoading) return;
		int offset_y = (int) (event.getRawY() - this.raw_y);
		if (currentState == PULL_DOWN_STATE) {
			if (Math.abs(offset_y) > header.getMeasuredHeight() * 0.86 ) {
//				Log.i("test", "onEventHandler : header");
				header.setActionUpState();
			}
		} else if (currentState == PULL_UP_STATE && hasMore) {
			if (Math.abs(offset_y) > footer.getMeasuredHeight() * 0.86 ) {
//				Log.i("test", "onEventHandler : footer");
				footer.setActionUpState();
			}
		}
	}
	
	private void scrollBackToLoad(int offset) {
		Log.i("test", "scrollBack");
		if (currentState == PULL_DOWN_STATE) {
			isLoading = true;
			scrollTo(0, -(int)(header.getMeasuredHeight() * 0.6));
			if (listener != null) {
				listener.onHeaderRefresh();
			}
			header.setRefreshState();
		} else if (currentState == PULL_UP_STATE && hasMore) {
			isLoading = true;
			scrollTo(0, (int)(footer.getMeasuredHeight() * 0.6));
			if (listener != null) {
				listener.onFooterRefresh();
			}
			footer.setRefreshState();
		}
		postInvalidate();
	}
	
	public void scrollBack(){
		isLoading = false;
		Log.i("test", "scrollBack");
		if (currentState == PULL_DOWN_STATE) {
			scroller.startScroll(0, header.getScrollY(), -header.getScrollY(),Math.abs(header.getScrollY()));
		} else if (currentState == PULL_UP_STATE) {
			scroller.startScroll(0, footer.getScrollY(), -footer.getScrollY(),Math.abs(footer.getScrollY()));
		}
		postInvalidate();
		header.setDefaultState();
		footer.setDefaultState();
	}

	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		if (scroller.computeScrollOffset()) {
			scrollTo(scroller.getCurrX(), 0);
		}
	}

}
