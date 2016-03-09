/**
 * 
 */
package android.library.base.task;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.xmlpull.v1.XmlPullParserException;

import android.library.base.exception.RequestException;

/**
 * @author hushihua
 * 
 */
public abstract class HttpRequestWorkBase<P, R> extends BaseTask<P, R> {

	@Override
	protected ResultObject<R> doInBackground(P... params) {
		// TODO Auto-generated method stub
		ResultObject<R> result = new ResultObject<R>();
		try {
			onRequest(result, params);
		} catch (IOException e) {
			e.printStackTrace();
			result.exp = true;
			result.message = "网络连接异常，请重试";
		} catch (RequestException e) {
			e.printStackTrace();
			result.exp = true;
			result.message = "调用方法出错";
		} catch (Exception e) {
			e.printStackTrace();
			result.exp = true;
			result.message = e.getMessage();
		}
		return result;
	}
	
	protected void processException(ResultObject<R> result, Exception exception){
		result.message = exception.getMessage();
		if (exception instanceof IOException) {
			result.exp = true;
			result.message = "网络连接异常，请重试";
		}
		if (exception instanceof RequestException) {
			result.exp = true;
			result.message = "调用方法出错";
		}
		if (exception instanceof HttpResponseException) {
			result.exp = true;
			result.message = exception.getMessage();
		}
		if (exception instanceof XmlPullParserException) {
			result.exp = true;
			result.message = "协议解析异常";
		}
	}
	
	protected abstract void onRequest(ResultObject<R> result, P... params) throws ClientProtocolException, IOException, RequestException;


}
