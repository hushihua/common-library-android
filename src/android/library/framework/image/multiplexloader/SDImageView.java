/**
 * 
 */
package android.library.framework.image.multiplexloader;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * @author hushihua
 *
 */
public class SDImageView extends ImageView {
	
	private ImageRequestSchedule _singleSchedule;
	
	public SDImageView(Context context) {
		super(context);
		init();
	}

	public SDImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public SDImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init(){
		_singleSchedule = new ImageRequestSchedule();
	}
	
	public void loadBitmapFromUrl(String url, float round){
		_singleSchedule.imageView = this;
		_singleSchedule.urlRef = url;
		_singleSchedule.round = round;
		DispatchCenter.getInstance().postRequest(_singleSchedule);
	}
}
