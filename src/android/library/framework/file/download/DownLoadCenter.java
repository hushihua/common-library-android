package android.library.framework.file.download;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.library.framework.file.download.DownLoadTask.DownLoadEventListener;
import android.library.framework.file.download.bean.DownLoadSchedule;

class DownLoadCenter {

	private static DownLoadCenter instance;
	private ExecutorService exeService;
	private int POOL_SIZE = 5;

	private DownLoadCenter() {
		exeService = Executors.newFixedThreadPool(POOL_SIZE);
	}

	public static DownLoadCenter getInstance() {
		if (instance == null) {
			instance = new DownLoadCenter();
		}
		return instance;
	}

	public void submitRequest(DownLoadSchedule schedule, DownLoadEventListener listener) {
		DownLoadTask runnable = new DownLoadTask(schedule, listener);
		exeService.submit(runnable);
	}
	
	public void shutdown(){
		if (exeService != null) {
			exeService.shutdown();
		}
	}

}
