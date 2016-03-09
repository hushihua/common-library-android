/**
 * 
 */
package android.library.base.view.slideList;


import java.util.ArrayList;

import android.content.Context;
import android.library.base.view.slideList.SlideFrameLayout.onSlideMenuItemClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * @author hushihua
 *
 */
public abstract class SlideListViewAdapter<T extends ViewGroup> extends BaseAdapter {

    private Context _context;
    private onSlideMenuItemClickListener listener;
    
    public SlideListViewAdapter(Context context) {
		this._context = context;
	}
	
    public void setSlideMenuItemClickListener(onSlideMenuItemClickListener listener){
		this.listener = listener;
	}
	
	public void setMenuItemClickListener(onSlideMenuItemClickListener listener){
		this.listener = listener;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			ArrayList<SlideMenu> menus = new ArrayList<SlideMenu>();
			addMenu(menus);
			
			SlideFrameLayout content = new SlideFrameLayout(_context);
			if (listener != null){
				content.setSlideMenuItemClickListener(listener);
			}
			
			content.setContentMenuView(getView(), position, menus);
			convertView = content;
		}
		SlideFrameLayout layout = (SlideFrameLayout) convertView;
		T itemView = (T) layout.getItemView();
		updateView(position, itemView, layout);
		
		return convertView;
	}
	
	public abstract void updateView(int position, T view, SlideFrameLayout parent);
	
	public abstract T getView();
	
	public abstract void addMenu(ArrayList<SlideMenu> menus);

}
