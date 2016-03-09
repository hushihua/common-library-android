/**
 * 
 */
package android.library.base.view.slideList;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.widget.ScrollerCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author hushihua
 *
 */
public class SlideFrameLayout extends FrameLayout implements OnClickListener{

	private float point_x;
	private boolean isFinished = false, slideable = true;
	private ScrollerCompat scroller;
	private View contentView, menuView;
	private onSlideMenuItemClickListener listener;
	private int position;
	
	public interface onSlideMenuItemClickListener{
		 void onMenuItemClick(View view, int position, int menuId);
	}
	
	public SlideFrameLayout(Context context){
		super(context);
		init();
	}

	public void setSlideable(boolean slideable){
		this.slideable = slideable;
	}
	
	public SlideFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public void setSlideMenuItemClickListener(onSlideMenuItemClickListener listener){
		this.listener = listener;
	}
	
	public View getItemView(){
		return contentView;
	}
	
	private void init(){
		scroller = ScrollerCompat.create(getContext());
	}
	
	public void setContentMenuView(View itemView, int position, ArrayList<SlideMenu> menus){
		this.position = position;
		
		LayoutParams contentParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		contentParams.gravity = Gravity.CENTER;
		itemView.setLayoutParams(contentParams);
		itemView.setBackgroundColor(Color.WHITE);
		this.contentView = itemView;
		addView(itemView);
		
		LinearLayout layout = new LinearLayout(getContext());
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
		layout.setLayoutParams(params);
		layout.setOrientation(LinearLayout.HORIZONTAL);
//		layout.setBackground(ResourceUtil.getNinePatchDrawableFromAssets(getContext(), "slide_menu_bg.9.png"));
		layout.setGravity(Gravity.CENTER);
		int layPadding = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getContext().getResources().getDisplayMetrics());
		layout.setPadding(layPadding, layPadding, layPadding, layPadding);
		this.menuView = layout;
		
		if (menus != null && menus.size() > 0) {
			for (int i = 0; i < menus.size(); i++) {
				SlideMenu menu = menus.get(i);
				TextView text = new TextView(getContext());
				if (menu.menuId > 0) {
					text.setId(menu.menuId);
				}
				if (menu.menuText != null) {
					text.setText(menu.menuText);
				}
				if (menu.isBold) {
					text.getPaint().setFakeBoldText(true);
				}
				if (menu.padding > 0) {
					int padding = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, menu.padding, getContext().getResources().getDisplayMetrics());
					text.setPadding(padding, padding, padding, padding);
				}
				if (menu.textSize > 0) {
					text.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, menu.textSize, getContext().getResources().getDisplayMetrics()));
				}
				if (menu.backgroundDrawable != null) {
					text.setBackgroundDrawable(menu.backgroundDrawable);
				}
				if (menu.textColor != 0) {
					text.setTextColor(menu.textColor);
				}
				text.setClickable(true);
				text.setOnClickListener(this);
				layout.addView(text, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			}
		}
		addView(layout);
	}
	
    @Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
    	contentView.layout(0, 0, getMeasuredWidth(), getMeasuredHeight());
    	menuView.layout(getMeasuredWidth(), 0, getMeasuredWidth() + menuView.getMeasuredWidth(), getMeasuredHeight());
	}
    
    @Override
   	public void computeScroll() {
   		// TODO Auto-generated method stub
   		if (scroller.computeScrollOffset()) {
   			scrollTo(scroller.getCurrX(), 0);
   		}
   	}
    
    /**
     * 
     * @param event
     * @return 第一次选择具备打开条件返回true, 已经打开返回false
     */
    public boolean onActionDown(MotionEvent event){
    	if (!slideable) {
			return false;
		}
    	point_x = event.getX();
		if (isFinished) {
			scrollBack();
			return false;
		}
		return true;
    }
    
    /**
     * 
     * @param event
     * @return 向右打开返回true, 其它返回false
     */
    public boolean onActionMove(MotionEvent event){
    	if (!slideable) {
			return false;
		}
    	int deltaX = (int)(point_x - event.getX());
    	if (deltaX > 0) {//slide to right
    		if (Math.abs(deltaX) > menuView.getWidth()) {
	    		isFinished = true;
	    		deltaX = menuView.getWidth();
			}
    		scrollTo(deltaX, 0);
		}
		return true;
    }
    
    /**
     * onActionUp
     * @param event
     * @return 完成打开操作返回 true， 没有成功打开返回false
     */
    public boolean onActionUp(MotionEvent event){
    	if (!slideable) {
			return false;
		}
    	Log.i("test", "ACTION_UP");
//    	Log.i("test", "dis : " + Float.toString(point_x - event.getX()) + ", " + menuView.getWidth() / 3 );
		if ((point_x - event.getX()) > (menuView.getWidth() / 3) && point_x - event.getX() > 0) {
			scrollByDistanceX();
		}else{
			scrollBack();
			return false;
		}
		return true;
    }
    
    private void scrollLeft(){
    	Log.i("test", "scrollLeft");
    	isFinished = true;
    	int offsetX = menuView.getScrollX();
    	scroller.startScroll(offsetX, 0, menuView.getWidth() - menuView.getScrollX(), 0, Math.abs(offsetX));
    	postInvalidate();
    }
    
    private void scrollRight(){
    	Log.i("test", "scrollRight");
    	isFinished = true;
    	int offsetX = menuView.getScrollX();
    	scroller.startScroll(offsetX, 0, - (menuView.getWidth() + menuView.getScrollX()), 0, Math.abs(offsetX));
    	postInvalidate();
    }
    
    private void scrollBack(){
    	Log.i("test", "scrollBack");
    	isFinished = false;
    	scroller.startScroll(menuView.getScrollX(), 0, - menuView.getScrollX(), Math.abs(menuView.getScrollX()));
    	postInvalidate();
    }
    
    private boolean scrollByDistanceX(){
    	if (isFinished)
			return false;
//    	Log.i("test", "getScaleX : " + Float.toString(menuView.getScaleX()));
    	if (menuView.getScrollX() > 0) {
			scrollLeft();
			return true;
		}else if(menuView.getScrollX() < 0) {
			scrollRight();
		}else{
			scrollBack();
		}
    	return false;
    }

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if (listener != null) {
			scrollBack();
			listener.onMenuItemClick(view, position, view.getId());
		}
	}

}
