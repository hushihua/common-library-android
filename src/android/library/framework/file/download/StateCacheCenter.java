/**
 * 
 */
package android.library.framework.file.download;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.library.framework.file.download.DownLoadTask.DownLoadEventListener;
import android.library.framework.file.download.bean.AppDownLoadInfo;
import android.library.framework.file.download.bean.DownLoadSchedule;
import android.library.framework.file.download.bean.DownLoadSchedule.RunType;
import android.library.framework.file.download.cacheDB.DownLoadDBManager;

/**
 * @author hushihua
 * 
 */
public class StateCacheCenter implements DownLoadEventListener{

	private static StateCacheCenter instance;
	private ArrayList<DownLoadSchedule> cacheData;
	private Context context;
	
	public static StateCacheCenter getInstance(){
		if (instance == null) {
			instance = new StateCacheCenter();
		}
		return instance;
	}
	
	private StateCacheCenter(){
		cacheData = new ArrayList<DownLoadSchedule>();
	}
	
	/**
	 * ��SQLite�ָ������б����ݣ��ں�̨���ط�����ʱ����
	 * @param context
	 */
	public void loadCacheDateFromSQlite(Context context){
		this.context = context;
		DownLoadDBManager.getInstance().initDB(context);
		ArrayList<AppDownLoadInfo> oldData = DownLoadDBManager.getInstance().loadAllSchedule();
		for (AppDownLoadInfo appDownLoadInfo : oldData) {
			//����ļ��Ƿ��Ѿ�ɾ����ɾ���ļ������������ݿ���ɾ��
			File file = new File(appDownLoadInfo.path);
			if (!file.exists()) {
				DownLoadDBManager.getInstance().deleteSchedule(appDownLoadInfo);
				continue;
			}
			//ǿ�����ó�ʼ״̬
			if (appDownLoadInfo.totalSize == appDownLoadInfo.completeSize) {
				appDownLoadInfo.state = RunType.FINISH;
			}else{
				appDownLoadInfo.state = RunType.PAUSE;//��ʱ���ó� pause
			}
			cacheData.add(appDownLoadInfo);
		}
//		cacheData.addAll(oldData);
	}
	
	/**
	 * ��ӡ�������������
	 * @param context
	 * @param schedule
	 */
	public void pushSchedule(Context context, DownLoadSchedule schedule){
		this.context = context;
		for (int i = 0; i < cacheData.size(); i++) { //���ظ����ͬһ���ؼƻ�
			if (cacheData.get(i).downLoadUrl.equals(schedule.downLoadUrl)) {
				return;
			}
		}
		schedule.state = RunType.WAITING;
		cacheData.add(schedule);
		//����һ���µ����ؼ�¼
		DownLoadDBManager.getInstance().insertSchedule((AppDownLoadInfo)schedule);
		//�п������е����ȼ����ǰ�����˳������
		DownLoadCenter.getInstance().submitRequest(schedule, this);
	}
	
	/**
	 * �ָ��������񣬼�������
	 * @param context
	 * @param schedule
	 */
	public void resumeScehdule(Context context, DownLoadSchedule schedule){
		this.context = context;
		boolean runable = false;
		for (int i = 0; i < cacheData.size(); i++) { //���ظ����ͬһ���ؼƻ�
			if (cacheData.get(i).downLoadUrl.equals(schedule.downLoadUrl)) {
				runable = true;
				break;
			}
		}
		if (runable) {
			schedule.state = RunType.WAITING;
			DownLoadCenter.getInstance().submitRequest(schedule, this);
		}
	}
	
	/**
	 * ɾ����������
	 * @param schedule
	 */
	public void popSchedule(DownLoadSchedule schedule){
		for (int i = 0; i < cacheData.size(); i++) {
			if (cacheData.get(i).downLoadUrl.equals(schedule.downLoadUrl)) {
				cacheData.remove(i);
				DownLoadDBManager.getInstance().deleteSchedule((AppDownLoadInfo)schedule);
				DownLoadBoradcastHelper.sendRefreshDownloadCacheBroadcast(context);
				return;
			}
		}
	}
	
	/**
	 * ɾ����������
	 * @param schedule
	 */
	public void popAllSchedule(){
		for (int i = 0; i < cacheData.size(); i++) {
			DownLoadSchedule info = cacheData.get(i);
			if (info.state == RunType.LOADING || info.state == RunType.WAITING) {
				info.common = RunType.DELETE;//���߳�ͣ����������������ʱ��������ô��ͣ��������ν��
			}
			DownLoadDBManager.getInstance().deleteSchedule((AppDownLoadInfo)info);
		}
		cacheData.clear();
		DownLoadBoradcastHelper.sendRefreshDownloadCacheBroadcast(context);
	}
	
	/**
	 * ��ȡ������������
	 * @return
	 */
	public ArrayList<DownLoadSchedule> getCacheState(){
		return cacheData;
	}
	
	/**
	 * �Ƿ��Ѿ��ύ��������
	 * @param url
	 * @return
	 */
	public boolean isLoading(String url){
		for (int i = 0; i < cacheData.size(); i++) {
			if (cacheData.get(i).downLoadUrl.equals(url)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * ��ȡָ������������
	 * @param url
	 * @return
	 */
	public DownLoadSchedule getDownLoadSchedule(String url){
		for (int i = 0; i < cacheData.size(); i++) {
			if (cacheData.get(i).downLoadUrl.equals(url)) {
				return cacheData.get(i);
			}
		}
		return null;
	}
	
	/**
	 * ��ͣ������������
	 */
	public void paushAllSchedule(){
		for (int i = 0; i < cacheData.size(); i++) {
			DownLoadSchedule schedule = cacheData.get(i);
			schedule.common = RunType.PAUSE;
		}
	}

	@Override
	public void onEventHandler(DownLoadSchedule schedule) {
		// TODO Auto-generated method stub
		if (schedule.state == RunType.DELETE) {
			popSchedule(schedule);//ɾ�����ؼ�¼�����͹㲥�������б�
			return;
		}
		
		if (schedule.state == RunType.FINISH) {//�������
			DownLoadBoradcastHelper.sendItemFinishBroadcast(context, schedule);
		}
	}

}
