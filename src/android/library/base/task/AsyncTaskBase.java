/**
 * 
 */
package android.library.base.task;

import java.io.IOException;

import org.apache.http.client.HttpResponseException;
import org.xmlpull.v1.XmlPullParserException;

import android.library.base.exception.RequestException;
import android.os.AsyncTask;

/**
 * @author hushihua
 *
 */
public abstract class AsyncTaskBase<P, T> extends AsyncTask<P, Integer, ResultObject<T>> {

	@Override
	protected ResultObject<T> doInBackground(P... arg0) {
		// TODO Auto-generated method stub
		ResultObject<T> result = new ResultObject<T>();
		try {
			doInBackground(result);
		} catch (HttpResponseException e) {
			e.printStackTrace();
			result.exp = true;
			result.message = e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			result.exp = true;
			result.message = "网络连接异常，请重试";
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			result.exp = true;
			result.message = e.getMessage();
		}catch (RequestException e) {
			e.printStackTrace();
			result.exp = true;
			result.message = "调用接口异常";
		}
		return result;
	}
	
	protected abstract void doInBackground(ResultObject<T> result) throws HttpResponseException,IOException, XmlPullParserException, RequestException;

}
