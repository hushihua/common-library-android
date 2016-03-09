/**
 * 
 */
package android.library.base.view.slideContent;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * @author hushihua
 *
 */
public class SlideContentFooter extends LinearLayout {

	private TextView text;
	private ImageView arrow;
	private ProgressBar progressBar;
	
	public SlideContentFooter(Context context) {
		super(context);
		initView();
	}

	public SlideContentFooter(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}
	
	private void initView(){
		int height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getContext().getResources().getDisplayMetrics());
		setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, height));
		setBackgroundColor(/*Color.rgb(125, 125, 125)*/0xffeeeeee);
		
		int padding = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getContext().getResources().getDisplayMetrics());
		LinearLayout layout = new LinearLayout(getContext());
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		params.setMargins(0, 0, 0, padding);
		layout.setLayoutParams(params);
		layout.setGravity(Gravity.CENTER);
		layout.setPadding(padding, padding, padding, padding);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		addView(layout);
		
		arrow = new ImageView(getContext());
		arrow.setPadding(padding, padding, padding, padding);
//		arrow.setImageResource(R.drawable.ic_pulltorefresh_arrow_up);
		layout.addView(arrow, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)); 
		
		progressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleSmallInverse);
		progressBar.setPadding(padding, padding, padding, padding);
		layout.addView(progressBar, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)); 
		
		text = new TextView(getContext());
		text.setText("上拉加载更多");
		text.setTextColor(Color.BLACK);
		text.setPadding(padding, padding, padding, padding);
		layout.addView(text, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)); 
		
		setDefaultState();
	}
	
	public void setDefaultState(){
		progressBar.setVisibility(View.GONE);
		arrow.setVisibility(View.VISIBLE);
//		arrow.setImageResource(R.drawable.ic_pulltorefresh_arrow_up);
		text.setText("上拉加载更多");
	}
	
	public void setRefreshState(){
		progressBar.setVisibility(View.VISIBLE);
		arrow.setVisibility(View.GONE);
		text.setText("数据正在加载，请稍候");
	}
	
	public void setActionUpState(){
		progressBar.setVisibility(View.GONE);
		arrow.setVisibility(View.VISIBLE);
//		arrow.setImageResource(R.drawable.ic_pulltorefresh_arrow);
		text.setText("松开加载更多");
	}

}
