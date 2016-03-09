/**
 * 
 */
package android.library.framework.image.multiplexloader;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * @author hushihua
 *
 */
class DownLoadImageTask implements Runnable{

	private String urlCopy;
	
	public DownLoadImageTask(String url) {
		urlCopy = new String(url);
	}
	
	@Override
	public void run() {
		try {
			URL url = new URL(urlCopy);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream inputStream = conn.getInputStream();
			Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
			inputStream.close();
			DispatchCenter.getInstance().onFinishHandler(urlCopy, bitmap);
		} catch (IOException e) {
			e.printStackTrace();
			DispatchCenter.getInstance().onErrorHandler(urlCopy);
		}
	}
	
}
