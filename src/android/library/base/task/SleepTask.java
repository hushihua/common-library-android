/**
 * 
 */
package android.library.base.task;


/**
 * @author hushihua
 *
 */
public class SleepTask extends BaseTask<Integer, Void> {

	@Override
	protected ResultObject<Void> doInBackground(Integer... arg) {
		// TODO Auto-generated method stub
		ResultObject<Void> result  = new ResultObject<Void>();
		try {
			Thread.sleep(arg[0]);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.exp = true;
			result.message = e.getMessage();
		}
		return result;
	}

}
