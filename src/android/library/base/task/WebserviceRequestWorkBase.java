/**
 * 
 */
package android.library.base.task;

import java.io.IOException;

import org.apache.http.client.HttpResponseException;
import org.xmlpull.v1.XmlPullParserException;

import android.library.base.exception.RequestException;
import android.library.base.task.BaseTask;
import android.library.base.task.ResultObject;

/**
 * @author hushihua
 *
 */
public abstract class WebserviceRequestWorkBase<P,R> extends BaseTask<P, R> {

	@Override
	protected ResultObject<R> doInBackground(P... arg) {
		ResultObject<R> result = new ResultObject<R>();
		try {
			onRequest(result, arg);
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
			result.message = "协议解析异常";
		}catch (RequestException e) {
			e.printStackTrace();
			result.exp = true;
			result.message = "调用方法出错";
		}
		return result;
	}
	
	protected abstract void onRequest(ResultObject<R> result, P... arg) throws HttpResponseException, IOException, XmlPullParserException, RequestException;

}
