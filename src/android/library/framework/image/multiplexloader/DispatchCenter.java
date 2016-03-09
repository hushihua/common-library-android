/**
 * 
 */
package android.library.framework.image.multiplexloader;

import java.util.HashMap;
import java.util.HashSet;

import android.graphics.Bitmap;
import android.library.framework.image.ImageTools;
import android.widget.ImageView;

/**
 * @author hushihua
 *
 */
public class DispatchCenter {

	private static DispatchCenter instance;
	//�Ƿ�Ҫ�����̰߳�ȫ���⣿�Ƿ����������ͬKEYֵ�ļ�¼ͬ����¼�룬ʱ��������Сʱ�䣿
	private HashMap<String, HashSet<ImageRequestSchedule>> loadingRequest;
	
	private IMemoryCache cachePolicy; //������
	
	private DispatchCenter(){
		loadingRequest = new HashMap<String, HashSet<ImageRequestSchedule>>();
		cachePolicy = MemoryCache.getInstance();//������
	}
	
	public static DispatchCenter getInstance(){
		if (instance == null) {
			instance = new DispatchCenter();
		}
		return instance; 
	}
	
	/**
	 * �����ݸ���URL����
	 * @deprecated
	 * @param view
	 * @param url
	 */
	public void postRequest(final ImageView view, final String url, float round){
		ImageRequestSchedule schedule = new ImageRequestSchedule();
		schedule.imageView = view;
		schedule.urlRef = url;
		schedule.round = round;
		Bitmap bitmap = cachePolicy.getCacheBitmap(url);
		if (bitmap == null) {
			if (loadingRequest.containsKey(url)) {
				HashSet<ImageRequestSchedule> scheduleSet = loadingRequest.get(url);
				scheduleSet.add(schedule);
			}else{
				HashSet<ImageRequestSchedule> newSet = new HashSet<ImageRequestSchedule>();
				newSet.add(schedule);
				loadingRequest.put(url, newSet); //url �� map���Ƿ���COPY������
				DownLoadImageCenter.getInstance().submitRequest(url);
			}
		}else{
			if (schedule.round > 0) {//Բ�Ǵ���
				bitmap = ImageTools.drawRoundBitmap(bitmap, schedule.round);
			}
			view.setImageBitmap(bitmap);
		}
	}
	
	/**
	 * �ɼ��ݸ���URL����
	 * @param schedule
	 */
	public void postRequest(ImageRequestSchedule schedule){
		Bitmap bitmap = cachePolicy.getCacheBitmap(schedule.urlRef);
		if (bitmap == null) {
			if (loadingRequest.containsKey(schedule.urlRef)) {
				HashSet<ImageRequestSchedule> scheduleSet = loadingRequest.get(schedule.urlRef);
				scheduleSet.add(schedule);
			}else{
				HashSet<ImageRequestSchedule> newSet = new HashSet<ImageRequestSchedule>();
				newSet.add(schedule);
				loadingRequest.put(schedule.urlRef, newSet);
				DownLoadImageCenter.getInstance().submitRequest(schedule.urlRef);
			}
		}else{
			if (schedule.round > 0) {//Բ�Ǵ���
				bitmap = ImageTools.drawRoundBitmap(bitmap, schedule.round);
			}
			schedule.imageView.setImageBitmap(bitmap);
		}
	}
	
	//��һ��ͼƬ����������һ�����̱߳�ʾ����ʧ�ܣ������¼����󡣡���������
	public void onFinishHandler(String url, Bitmap bitmap){
		cachePolicy.putCacheBitmap(url, bitmap);
		if (loadingRequest.containsKey(url)) {
			HashSet<ImageRequestSchedule> scheduleSet = loadingRequest.get(url);
			for (ImageRequestSchedule imageRequestSchedule : scheduleSet) {
				if (imageRequestSchedule.round > 0) { //Բ�Ǵ���
					if (bitmap != null) {
						bitmap = ImageTools.drawRoundBitmap(bitmap, imageRequestSchedule.round);
					}
				}
				if (url.equals(imageRequestSchedule.urlRef)) {
					UpdateImageViewTask update = new UpdateImageViewTask(imageRequestSchedule.imageView, bitmap);
					update.inflateImageBitmap();
				}
			}
			loadingRequest.remove(url);
		}
	}
	
	public void onErrorHandler(String url){
		if (loadingRequest.containsKey(url)) {
			loadingRequest.remove(url);
		}
	}
	
}
