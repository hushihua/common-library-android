/**
 * 
 */
package android.library.base.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author hushihua
 * 
 */
public class DownLoadFileTask extends BaseTask<String, File> {

	@Override
	protected ResultObject<File> doInBackground(String... params) {
		// TODO Auto-generated method stub
		ResultObject<File> result = new ResultObject<File>();
		try {
			URL url = new URL(params[0]);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
			if (conn.getResponseCode() == 200) {
				InputStream inputStream = conn.getInputStream();
				File fileName = new File(params[1]);
				if (!fileName.exists()) {
					fileName.createNewFile();
				}
				FileOutputStream outPutStream = new FileOutputStream(fileName);
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = inputStream.read(buffer)) != -1) {
					outPutStream.write(buffer, 0, len);
				}
				result.result = fileName;
				outPutStream.close();
				inputStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.exp = true;
			result.message = e.getMessage();
		}
		return result;
	}

}
