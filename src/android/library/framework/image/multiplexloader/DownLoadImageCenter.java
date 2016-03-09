/**
 * 
 */
package android.library.framework.image.multiplexloader;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author hushihua
 *
 */
class DownLoadImageCenter {

	private static DownLoadImageCenter instance;
	private ExecutorService exeService;
	private int POOL_SIZE = 10;
	
	private DownLoadImageCenter(){
		exeService = Executors.newFixedThreadPool(POOL_SIZE);
	}
	
	public static DownLoadImageCenter getInstance(){
		if (instance == null) {
			instance = new DownLoadImageCenter();
		}
		return instance; 
	}
	
	public void submitRequest(String url) {
		DownLoadImageTask runnable = new DownLoadImageTask(url);
		exeService.submit(runnable);
	}
	
}
