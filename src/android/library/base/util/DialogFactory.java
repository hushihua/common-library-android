/**
 * 
 */
package android.library.base.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

/**
 * @author hushihua
 * 
 */
public class DialogFactory {
	
	private static DialogFactory instance = null;
	
	private final String SUMBIT = "确认";
	private final String CANCEL = "取消";
	
	private DialogFactory(){};
	
	public static DialogFactory getInstance(){
		if (instance == null) {
			instance = new DialogFactory();
		}
		return instance;
	}
	
	public OnClickListener cancelListener = new OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			dialog.dismiss();
		}
	};

	public void showAlertDialog(Context context, String title, String message, boolean cancelable, OnClickListener listener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setCancelable(cancelable);
		builder.setPositiveButton(SUMBIT, listener);
		builder.setNegativeButton(CANCEL, cancelListener);
		AlertDialog dialog = builder.create();
		dialog.show();
	}

}
