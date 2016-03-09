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
 * 组合多选组件
 * 
 * @author hushihua
 * 
 */
public class ComboMultipleButtonView extends LinearLayout implements OnClickListener {

	public interface ComboMultipleButtonOnClickListener {
		public void onItemSelected(int index, String value);

		public void onItemUnselected(int index, String value);
	}

	private ArrayList<String> values = new ArrayList<String>();
	private ArrayList<TextView> itemViews = new ArrayList<TextView>();
	private ComboMultipleButtonOnClickListener listener = null;
	private ArrayList<String> selValues = new ArrayList<String>();

	public ComboMultipleButtonView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		setOrientation(LinearLayout.VERTICAL);
		initView();
	}

	public void setData(ArrayList<String> collection) {
		values.addAll(collection);
		removeAllViews();
		initView();
	}

	public ArrayList<String> getSelectedValues() {
		return selValues;
	}

	public void setListener(ComboMultipleButtonOnClickListener listener) {
		this.listener = listener;
	}

	/**
	 * get count
	 * 
	 * @return
	 */
	private int sizeCount() {
		int size = values.size();
		int re = size % 3;
		int count = size / 3;
		if (re > 0) {
			count++;
		}
		return count;
	}

	/**
	 * 1. init item view 2. set item values
	 */
	private void initView() {
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
				text.setLayoutParams(params);
				text.setPadding(padding, padding, padding, padding);

				text.setTextColor(Color.BLACK);
				text.setGravity(Gravity.CENTER);
				text.setVisibility(View.INVISIBLE);
				text.setOnClickListener(this);
				text.setTag(i * 3 + item);
				text.setBackgroundDrawable(ResourceUtil.getNinePatchDrawableFromAssets(getContext(), "combo_bg_normal.9.png"));

				itemViews.add(text);
				layout.addView(text);
			}
			addView(layout);
		}
		setItemValue();
	}

	/**
	 * 1. set item TextView text 2. set TextView visiable
	 */
	private void setItemValue() {
		for (int i = 0; i < values.size(); i++) {
			TextView text = itemViews.get(i);
			text.setText(values.get(i));
			text.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		int index = (Integer) view.getTag();
		for (int i = 0; i < selValues.size(); i++) {
			if (selValues.get(i).equals(values.get(index))) {
				// has selected
				view.setBackgroundDrawable(ResourceUtil.getNinePatchDrawableFromAssets(getContext(), "combo_bg_normal.9.png"));
				if (listener != null) {
					listener.onItemUnselected(index, values.get(index));
				}
				selValues.remove(values.get(index));
				return;
			}
		}

		if (listener != null) {
			listener.onItemSelected(index, values.get(index));
		}
		view.setBackgroundDrawable(ResourceUtil.getNinePatchDrawableFromAssets(getContext(), "combo_bg_selected.9.png"));
		selValues.add(values.get(index));
	}
}
