/**
 * 
 */
package android.library.framework.file.download;

import android.content.Context;
import android.content.Intent;
import android.library.framework.file.download.bean.DownLoadSchedule;


/**
 * @author hushihua
 *
 */
public class DownLoadBoradcastHelper {

	public static String REFRESH_DOWNLOAD_LIST_ACTION = "android.library.framework.download:REFRESH_CACHE_LIST";
	public static String DOWNLOAD_ITEM_FINISH_ACTION = "android.library.framework.download:ITEM_FINISH";
	
	public static void sendRefreshDownloadCacheBroadcast(Context context){
		Intent intent = new Intent(REFRESH_DOWNLOAD_LIST_ACTION);
		context.sendBroadcast(intent);
//		LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
	}
	
	public static void sendItemFinishBroadcast(Context context, DownLoadSchedule schedule){
		Intent intent = new Intent(DOWNLOAD_ITEM_FINISH_ACTION);
		intent.putExtra("DATA", schedule);
		context.sendBroadcast(intent);
//		LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
	}
}
