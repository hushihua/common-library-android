package android.library.base.view.banner;

import android.content.Context;
import android.library.base.util.ResourceUtil;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * 本地离线加载
 * @author hushihua
 *
 */
public class SlideEffectBanner extends FrameLayout implements OnPageChangeListener{

	private LinearLayout imageContent;
	private ImageView[] tips;
	private ViewPager viewPager;
	
	public SlideEffectBanner(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView();
	}

	public SlideEffectBanner(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView();
	}
	
	private void initView(){
		viewPager = new ViewPager(getContext());
		viewPager.setOnPageChangeListener(this);
		addView(viewPager, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		
		RelativeLayout layout = new RelativeLayout(getContext());
		addView(layout, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		
		LinearLayout tipLayout = new LinearLayout(getContext());
		tipLayout.setGravity(Gravity.CENTER_HORIZONTAL);
		tipLayout.setOrientation(LinearLayout.HORIZONTAL);
		android.widget.RelativeLayout.LayoutParams params = new android.widget.RelativeLayout.LayoutParams(android.widget.RelativeLayout.LayoutParams.FILL_PARENT, android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		int padding = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, this.getResources().getDisplayMetrics());
		params.setMargins(0, 0, 0, padding);
		layout.addView(tipLayout, params);
		imageContent = tipLayout;
	}
	
	public void addImage(int[] image){
		tips = new ImageView[image.length];
		int padding = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, this.getResources().getDisplayMetrics());
		for(int i=0; i<image.length; i++){
			ImageView imageView = new ImageView(getContext());
			android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.WRAP_CONTENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
			params.setMargins(padding, padding, padding, padding);
	    	imageView.setLayoutParams(params);
	    	imageView.setPadding(padding, padding, padding, padding);
	    	tips[i] = imageView;
	    	if(i == 0){
	    		tips[i].setBackgroundDrawable(ResourceUtil.getDrawabldeFromAssets(getContext(), "page_indicator_focused.png"));
	    	}else{
	    		tips[i].setBackgroundDrawable(ResourceUtil.getDrawabldeFromAssets(getContext(), "page_indicator_unfocused.png"));
	    	}
	    	imageContent.addView(imageView);
		}
		viewPager.setAdapter(new SlideEffectBannerAdapter(getContext(), image));
		viewPager.setCurrentItem((image.length) * 100);//TODO
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		setImageBackground(arg0 % tips.length);
	}
	
	/**
	 * 设置选中的tip的背景
	 * @param selectItems
	 */
	private void setImageBackground(int selectItems){
		for(int i=0; i<tips.length; i++){
			if(i == selectItems){
				tips[i].setBackgroundDrawable(ResourceUtil.getDrawabldeFromAssets(getContext(), "page_indicator_focused.png"));
			}else{
				tips[i].setBackgroundDrawable(ResourceUtil.getDrawabldeFromAssets(getContext(), "page_indicator_unfocused.png"));
			}
		}
	}

}
