package android.library.base.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.library.base.exception.RequestException;

@Deprecated
public class HttpSingletonRequest {

	private static final int SUCCESS = 200;
	private static HttpClient httpClient = new DefaultHttpClient();

	protected String postRequest(String url, HashMap<String, String> params)throws ClientProtocolException, IOException, RequestException {
		HttpPost request = new HttpPost(url);
		ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
		if (params != null) {
			for (String key : params.keySet()) {
				parameters.add(new BasicNameValuePair(key, params.get(key)));
			}
		}
		
		request.setEntity(new UrlEncodedFormEntity(parameters, HTTP.UTF_8));
		HttpResponse response = httpClient.execute(request);
		if (response.getStatusLine().getStatusCode() == SUCCESS) {
			return EntityUtils.toString(response.getEntity());
		}
		throw new RequestException();
	}

}
