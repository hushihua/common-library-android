package android.library.framework.file.download;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import android.library.framework.file.download.bean.DownLoadSchedule;
import android.library.framework.file.download.bean.DownLoadSchedule.RunType;
import android.library.framework.file.download.cacheDB.DownLoadDBManager;
import android.util.Log;

public class DownLoadTask implements Runnable{
	
	private DownLoadSchedule schedule;
	private DownLoadDBManager dbManager;
	private DownLoadEventListener listener;
	
	public interface DownLoadEventListener{
		public void onEventHandler(DownLoadSchedule schedule);
	}
	
	public DownLoadTask(DownLoadSchedule info, DownLoadEventListener listener) {
		this.schedule = info;
		this.listener = listener;
		dbManager = DownLoadDBManager.getInstance();
	}
	
	@Override
	public void run() {
		if (schedule.common == RunType.DELETE) {
			return;
		}
		schedule.state = RunType.LOADING;
		try {
			URL downUrl = new URL(schedule.downLoadUrl);
			HttpURLConnection connection = (HttpURLConnection) downUrl.openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(1000);//ע�������ʱ��,ʱ��̵Ļ�����������ͣ�����죬�Ƿ�����������ʽʵ���ⲿ����ͣ��
			if (schedule.completeSize > 0) {
				connection.setRequestProperty("Range", "bytes="+ schedule.completeSize + "-" + schedule.totalSize);//�Ѿ����ع����ļ���ʵ���������µ��������񣬲����ø�ֵ
			}
			if (connection.getResponseCode() == 200 || connection.getResponseCode() == 206) {
				if (schedule.completeSize <= 0) {
					Log.i("DownLoad", "<-- ��ʼ�����ļ�: " + schedule.downLoadUrl + " -->");
					schedule.totalSize = connection.getContentLength(); //������������ʱ�����ø�ֵ
				}
				Log.i("DownLoad", "<-- load file: " + schedule.downLoadUrl + " -->");
				
				//���Կ�����ӹ��˻��ƣ�ȷ�������ļ��Ĵ�Сһ��
				InputStream inputStream = connection.getInputStream();
				
				File file = new File(schedule.path);
				if (!file.exists()) {
					file.createNewFile();
				}
				RandomAccessFile outPutStream = new RandomAccessFile(file, "rwd");
				outPutStream.seek(schedule.completeSize);
//				FileOutputStream outPutStream = new FileOutputStream(file);
				
				byte[] buffer = new byte[512];//1k
				int len = 0;
				while ((len = inputStream.read(buffer)) != -1) {
					outPutStream.write(buffer, 0, len);
					schedule.completeSize += len; // add cache-length
					dbManager.updateSchedule(schedule);
//					Log.i("DownLoad", "<-- ����Ƭ�δ�С: " + len + "�� ��ǰ������: " + schedule.completeSize + " -->");
					//������ͣ�ж� 
					if (schedule.common == RunType.PAUSE) {
						schedule.state = RunType.PAUSE;//��дINFO��״̬Ϊ��ͣ״̬
						outPutStream.close();
						inputStream.close();
						Log.i("DownLoad", "<-- ��ͣ: " + schedule.downLoadUrl + " -->");
						return;
					}
					//ɾ��״̬
					if (schedule.common == RunType.DELETE) {
						schedule.state = RunType.DELETE;
						outPutStream.close();
						inputStream.close();
						file.delete();//ɾ���ļ�
						listener.onEventHandler(schedule);
//						StateCacheCenter.getInstance().popSchedule(schedule);//ɾ�����ؼ�¼�����͹㲥�������б�
						Log.i("DownLoad", "<-- ɾ��: " + schedule.downLoadUrl + " -->");
						return;
					}
					
					//�ر�����һ�����
					if (schedule.common == RunType.REMOVE) {
						outPutStream.close();
						inputStream.close();
						file.delete();//ɾ���ļ�
						Log.i("DownLoad", "<-- REMOVE: " + schedule.downLoadUrl + " -->");
						return;
					}
					
				}
				outPutStream.close();
				inputStream.close();
				schedule.state = RunType.FINISH;
				listener.onEventHandler(schedule);
				//����ǣ�SQLITEҲҪ��¼һ��, ���Ǽ��߼��ж�
				Log.i("DownLoad", "<-- �������: " + schedule.downLoadUrl + " -->");
				
			}else{
				schedule.state = RunType.ERROR;
				Log.i("DownLoad", "<-- ���ӳ���������: " + connection.getResponseCode() + " -->");
			}
		} catch (Exception e) {
			e.printStackTrace();
			schedule.state = RunType.ERROR;
			Log.i("DownLoad", "<-- ���س���������Ϣ: " + e.getMessage() + " -->");
		}
	}
	
}
