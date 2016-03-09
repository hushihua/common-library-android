/**
 * 
 */
package android.library.base.view.slideList;

import android.content.Context;
import android.library.base.view.slideList.SlideFrameLayout.onSlideMenuItemClickListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * @author hushihua
 * 
 */
public class SlideListView extends ListView implements OnItemClickListener {

	private float point_x, point_y;
	private SlideFrameLayout selectedItemView;
	private boolean isFrishChecked = false, slide = false;

	public SlideListView(Context context) {
		super(context);
		init();
	}

	public SlideListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		setOnItemClickListener(this);
	}

	/**
	 * 
	 * @param adapter
	 */
	public void setAdapter(SlideListViewAdapter<?> adapter, onSlideMenuItemClickListener listener) {
		if (adapter != null) {
			adapter.setSlideMenuItemClickListener(listener);
			setAdapter(adapter);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			point_x = event.getX();
			point_y = event.getY();
			
			int itemPosition = pointToPosition((int) point_x, (int) point_y);
			if (itemPosition == AdapterView.INVALID_POSITION) {
				break;
			}
			
			View item = getChildAt(itemPosition - getFirstVisiblePosition());
			if (selectedItemView != null && selectedItemView != item) {
				selectedItemView.onActionDown(event);
				selectedItemView = null;
			}
			
			if (item instanceof SlideFrameLayout) {
				if (((SlideFrameLayout) item).onActionDown(event)) {
					selectedItemView = (SlideFrameLayout) item;
				}
			}
			break;

		case MotionEvent.ACTION_MOVE:
			int deltaX = (int) (point_x - event.getX());
			int deltaY = (int) (point_y - event.getY());
			if (!isFrishChecked) {
				isFrishChecked = true;
				if (Math.abs(deltaX) > Math.abs(deltaY)) {
					slide = true;
				}
			}
			int touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
			if (slide && Math.abs(deltaX) > touchSlop && selectedItemView != null) {
				getSelector().setState(new int[] { 0 });
				event.setAction(MotionEvent.ACTION_CANCEL);
				super.onTouchEvent(event);
				selectedItemView.onActionMove(event);
				return true;
			}
			break;

		case MotionEvent.ACTION_UP:
			Log.i("test", "[ ACTION_Up ]");
			if (slide && selectedItemView != null) {
				if (!selectedItemView.onActionUp(event)) {
					selectedItemView = null;
					getSelector().setState(new int[] { 0 });
					event.setAction(MotionEvent.ACTION_CANCEL);
					super.onTouchEvent(event);
					isFrishChecked = false;
					slide = false;
					return true;
				}
			}
			isFrishChecked = false;
			slide = false;
			break;

		}
		return super.onTouchEvent(event);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// Toast.makeText(getContext(), "onItemClick", Toast.LENGTH_SHORT).show();
	}

}
