/**
 * 
 */
package android.library.framework.file.download;

import java.util.ArrayList;

import android.app.Service;
import android.content.Intent;
import android.library.framework.file.download.bean.AppDownLoadInfo;
import android.library.framework.file.download.bean.DownLoadSchedule;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * @author hushihua
 *
 */
public class DownLoadServiceAdv extends Service {
	
	private StateCacheCenter _cacheCenter = StateCacheCenter.getInstance();
	
	@Override
	public void onCreate() {
		super.onCreate();
		_cacheCenter.loadCacheDateFromSQlite(this);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null) {
			Object extra = intent.getSerializableExtra("SCHEDULE");
			if (extra != null && extra instanceof AppDownLoadInfo ) {
				AppDownLoadInfo schedule = (AppDownLoadInfo)extra;
				_cacheCenter.pushSchedule(this, schedule);
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * ���⣺
     * 1.��������׼ȷ�Կ��ǣ��Ƿ���Ӧ���˳��ǣ���ͣ������������
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.i("download", "-------- DownLoadService onDestroy --------");
		DownLoadCenter.getInstance().shutdown();
		super.onDestroy();
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return binder;
	}
	
	private ServiceBinder binder = new ServiceBinder();
	
	public class ServiceBinder extends Binder {
		
        public DownLoadServiceAdv getService() {
            return DownLoadServiceAdv.this;
        }
        
        /**
    	 * �Ƿ��Ѿ��ύ��������
    	 * @param url
    	 * @return
    	 */
    	public boolean isLoading(String url){
    		return _cacheCenter.isLoading(url);
    	}
    	
    	/**
    	 * 
    	 * @return
    	 */
    	public ArrayList<DownLoadSchedule> getCacheState(){
    		return _cacheCenter.getCacheState();
    	}
    	
    	/**
    	 * ��ӡ�������������
    	 * @param context
    	 * @param schedule
    	 */
    	public void pushSchedule(DownLoadSchedule schedule){
    		_cacheCenter.pushSchedule(DownLoadServiceAdv.this, schedule);
    	}
    	
    	/**
    	 * �ָ��������񣬼�������
    	 * @param context
    	 * @param schedule
    	 */
    	public void resumeScehdule(DownLoadSchedule schedule){
    		_cacheCenter.resumeScehdule(DownLoadServiceAdv.this, schedule);
    	}
    	
    	/**
    	 * ��ȡָ������������
    	 * @param url
    	 * @return
    	 */
    	public DownLoadSchedule getDownLoadSchedule(String url){
    		return _cacheCenter.getDownLoadSchedule(url);
    	}
    	
    	/**
    	 * ɾ����������
    	 * @param schedule
    	 */
    	public void popSchedule(DownLoadSchedule schedule){
    		_cacheCenter.popSchedule(schedule);
    	}
    	
    	/**
    	 * ɾ����������
    	 * @param schedule
    	 */
    	public void popAllSchedule(){
    		_cacheCenter.popAllSchedule();
    	}
    }
}
