/**
 * 
 */
package android.library.base.task;

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
public class LoadImageTask extends BaseTask<String, Bitmap> {

	@Override
	protected ResultObject<Bitmap> doInBackground(String... params) {
		// TODO Auto-generated method stub
		ResultObject<Bitmap> result = new ResultObject<Bitmap>();

		try {
			URL url = new URL(params[0]);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream inputStream = conn.getInputStream();
			Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
			inputStream.close();
			result.result = bitmap;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.exp = true;
			result.message = e.getLocalizedMessage();
		}

		return result;
	}

}
