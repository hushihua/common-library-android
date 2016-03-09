package android.library.base.view.combobox;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.library.base.util.ResourceUtil;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 组合单选组件
 * 
 * @author hushihua
 *
 */
public class ComboRadioButtonView extends LinearLayout implements OnClickListener{
	
	public interface ComboRadioButtonOnClickListener{
		public void onItemClick(int index, String value);
	}

	private ArrayList<String> values = new ArrayList<String>();
	private ArrayList<TextView> itemViews = new ArrayList<TextView>(); 
	private ComboRadioButtonOnClickListener listener = null;
	private View lastSelectView;
	
	public ComboRadioButtonView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		setLayoutParams( params );
		setOrientation(LinearLayout.VERTICAL);
		initView();
	}
	
	public void setData(ArrayList<String> collection){
		values.addAll(collection);
		removeAllViews();
		initView();
	}
	
	public void setListener(ComboRadioButtonOnClickListener listener){
		this.listener = listener;
	}
	
	/**
	 * get count
	 * @return
	 */
	private int sizeCount(){
		int size = values.size();
		int re = size % 3;
		int count = size / 3 ;
		if (re > 0) {
			count++; 
		}
		return count;
	}
	
	/**
	 * 1. init item view
	 * 2. set item values
	 */
	private void initView(){
		int count = sizeCount();
		for (int i = 0; i < count; i++) {
			LinearLayout layout = new LinearLayout(getContext());
			layout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			layout.setBackgroundColor(Color.WHITE);
			layout.setWeightSum(1);
			
			float density = getContext().getResources().getDisplayMetrics().density;
			int height = (int)(80 * density / 2);
			int padding = (int)(6 * density / 2);
			
			for (int item = 0; item < 3; item++) {
				TextView text = new TextView(getContext());
				
				LayoutParams params = new LayoutParams(0, height);
				params.weight = 0.33f;
				params.topMargin = padding;
				params.leftMargin = padding;
				params.rightMargin = padding;
				params.bottomMargin = padding;
				text.setLayoutParams( params );
				text.setPadding(padding, padding, padding, padding);
				
				text.setTextColor(Color.BLACK);
				text.setGravity(Gravity.CENTER);
				text.setVisibility(View.INVISIBLE);
				text.setOnClickListener(this);
				text.setTag(i * 3 + item );
				text.setBackgroundDrawable(ResourceUtil.getNinePatchDrawableFromAssets(getContext(), "combo_bg_normal.9.png"));
				
				itemViews.add(text);
				layout.addView(text);
			}
			addView(layout);
		}
		setItemValue();
	}
	
	/**
	 * 1. set item TextView text
	 * 2. set TextView visiable
	 */
	private void setItemValue(){
		for (int i = 0; i < values.size(); i++) {
			TextView text = itemViews.get(i);
			text.setText(values.get(i));
			text.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		int index = (Integer)view.getTag();
		if (listener != null && lastSelectView != view) {
			listener.onItemClick(index, values.get(index));
		}
		if (lastSelectView != null) {
			lastSelectView.setBackgroundDrawable(ResourceUtil.getNinePatchDrawableFromAssets(getContext(), "combo_bg_normal.9.png"));
		}
		view.setBackgroundDrawable(ResourceUtil.getNinePatchDrawableFromAssets(getContext(), "combo_bg_selected.9.png"));
		lastSelectView = view;
	}
	
	/**
	 * setSelectIndex
	 * @param index
	 */
	public void setSelectIndex(int index){
		if (listener != null && lastSelectView != itemViews.get(index)) {
			listener.onItemClick(index, values.get(index));
		}
		if (lastSelectView != null) {
			lastSelectView.setBackgroundDrawable(ResourceUtil.getNinePatchDrawableFromAssets(getContext(), "combo_bg_normal.9.png"));
		}
		itemViews.get(index).setBackgroundDrawable(ResourceUtil.getNinePatchDrawableFromAssets(getContext(), "combo_bg_selected.9.png"));
		lastSelectView = itemViews.get(index);
	}
	
	/**
	 * setSelectValue
	 * @param value
	 */
	public void setSelectValue(String value){
		if (value == null) {
			return;
		}
		for (int i = 0; i < values.size(); i++) {
			if (value.equals(values.get(i))) {
				setSelectIndex(i);
				break;
			}
		}
	}

}
