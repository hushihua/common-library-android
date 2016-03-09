/**
 * 
 */
package android.library.base.view.banner;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

/**
 * @author hushihua
 *
 */
public class SlideEffectBannerAdapter extends PagerAdapter {
	
	private ImageView[] imageViews;
	
	public SlideEffectBannerAdapter(Context context, int[] res){
		imageViews = new ImageView[res.length];
		for(int i = 0; i < res.length; i++){
			ImageView imageView = new ImageView(context);
			imageViews[i] = imageView;
			imageView.setBackgroundResource(res[i]);
		}
	}
	
	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	
	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager)container).removeView(imageViews[position % imageViews.length]);
	}

	@Override
	public Object instantiateItem(View container, int position) {
		((ViewPager)container).addView(imageViews[position % imageViews.length], 0);
		return imageViews[position % imageViews.length];
	}
}
