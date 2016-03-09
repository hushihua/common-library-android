/**
 * 
 */
package android.library.base.network;

import java.io.IOException;



import org.apache.http.client.HttpResponseException;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.library.base.exception.RequestException;
import android.util.Log;

/**
 * @author hushihua
 * 
 */
public class SSLWebserviceRequestBase {


	/**
	 * Ã·ΩªSOAP«Î«Û
	 * @param namespace
	 * @param method
	 * @param url
	 * @param soapAction
	 * @param params
	 * @return
	 * @throws HttpResponseException
	 * @throws IOException
	 * @throws XmlPullParserException
	 * @throws RequestException
	 */
	protected SoapObject methodRequest(String namespace, String method, String url, String soapAction, SoapObject params) throws HttpResponseException, IOException, XmlPullParserException, RequestException {
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut = params;
		envelope.dotNet = false;
		envelope.setOutputSoapObject(params);
		envelope.encodingStyle = "UTF-8";
		
		SSLConnection.allowAllSSL();
		HttpTransportSE transport = new HttpTransportSE(url);
		Log.i("service", "-- " + url + " --");
		transport.debug = true;
		transport.call(soapAction, envelope);
		Log.i("service", "-- action: " + soapAction + " --");
		Log.i("service", "-- params:" + params.toString() + " --");
		
		if (envelope.getResponse() != null) {
			SoapObject result = (SoapObject) envelope.bodyIn;
			Log.i("service", "-- result: " + result.toString() + " --");
			return result;
		}
		throw new RequestException();
	}

}
