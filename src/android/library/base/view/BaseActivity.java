/**
 * 
 */
package android.library.base.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.library.base.task.ResultObject;
import android.widget.Toast;

/**
 * @author hushihua
 *
 */
public class BaseActivity extends Activity {
	
	private ProgressDialog progressDialog;
	
	/**
	 * 
	 * @return
	 */
	protected boolean checkException(){
		return false;
	}
	
	/**
	 * 
	 */
	public void showProgressDialog(){
		if (progressDialog == null) {
			progressDialog = ProgressDialog.show(this, "", "数据正在提交，请稍后...");
		}
		progressDialog.show();
		//progressDialog.setCancelable(false);
	}
	
	/**
	 * 
	 * @param msg
	 */
	public void showProgressDialog(String message){
		if (progressDialog == null) {
			progressDialog = ProgressDialog.show(this, "", message);
		}else{
			progressDialog.setMessage(message);
		}
		progressDialog.show();
		//progressDialog.setCancelable(false);
	}
	
	/**
	 * 
	 */
	protected void dissmissProgressDialog(){
		if (progressDialog != null) {
			progressDialog.cancel();
		}
	}
	
	/**
	 * 
	 * @param result
	 * @return false = success, true = error
	 */
	protected boolean catchException(ResultObject<?> result){
		if (result.exp){
			showToast(result.message);
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param message
	 */
	protected void showToast(String message){
		Toast.makeText(getApplicationContext(), message,Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	protected String getResString(int id){
		return getResources().getString(id);
	}

}
