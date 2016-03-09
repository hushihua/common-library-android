package android.library.base.network;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.library.base.exception.RequestException;
import android.util.Log;

public class HttpRequestBase {

	private static HttpClient httpClient;
	
	static{
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, 20000);  
        HttpConnectionParams.setSoTimeout(params, 20000);
        httpClient = new DefaultHttpClient(params);
	}

	/**
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws RequestException
	 */
	protected String postRequest(String url, HashMap<String, String> params)throws ClientProtocolException, IOException, RequestException {
		HttpPost request = new HttpPost(url);
		Log.i("service", "-- post Request: " + url + " --");
		request.setHeader("Connection", "Close");  
		ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
		if (params != null) {
			for (String key : params.keySet()) {
				parameters.add(new BasicNameValuePair(key, params.get(key)));
				Log.i("service", "-- post params : " + key + " = " + params.get(key) + " --");
			}
		}
		request.setEntity(new UrlEncodedFormEntity(parameters, HTTP.UTF_8));
		HttpResponse response = httpClient.execute(request);
		int state = response.getStatusLine().getStatusCode();
		Log.i("service", "-- response state £º" + state + " --");
		if (state == HttpStatus.SC_OK) {
			String json = EntityUtils.toString(response.getEntity());
			Log.i("service", "-- entity: " + json + " --");
			return json;
		}
		throw new RequestException();
	}
	
	/**
	 * 
	 * 
	 * 
	 * @param url
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	protected Bitmap imageRequest(String url) throws ClientProtocolException, IOException{
		Log.i("service", "load image, url: " + url);
		HttpGet request = new HttpGet(url);
		request.setHeader("Connection", "Close"); 
		HttpResponse response = httpClient.execute(request);
		int state = response.getStatusLine().getStatusCode();
		Log.i("service", "Http state £º" + state + "");
		if (state == HttpStatus.SC_OK) {
			HttpEntity entity = response.getEntity();
			InputStream inputStream = entity.getContent();
			Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
			inputStream.close();
			return bitmap;
		}
		return null;
	}

}
