package android.library.framework.image.multiplexloader;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * 
 * @author hushihua
 *
 */
class MemoryCache implements IMemoryCache{

	private static MemoryCache instance;
	private static final String TAG = "MemoryCache";
	//������һ���̸߳ռ�����ɣ�׼�������¼����һ���߳���׼����ѯ�Ƿ��л���ʱ���Ƿ�Ҫ���������ƣ�����������ǰ�ټ��һ�»������Ƿ��Ѿ����ڣ�
	//�Ѿ�����
	private Map<String, SoftReference<Bitmap>> lruCache = Collections.synchronizedMap(new LinkedHashMap<String, SoftReference<Bitmap>>(10, 1.5f, true));
	private long currentCacheSize = 0;
	private long limit = 1000000;
	private final int SIZE_PRESENT = 30;

	public static MemoryCache getInstance(){
		if(instance == null){
			instance = new MemoryCache();
		}
		return instance;
	}
	
	private MemoryCache() {
		setLimit(Runtime.getRuntime().maxMemory() / SIZE_PRESENT);
	}

	/**
	 * 
	 * @param present
	 */
	public void setLimit(float present) {
		limit = (long) (Runtime.getRuntime().maxMemory() * present);
		Log.i(TAG, "MemoryCache will use up to " + limit / 1024. / 1024. + "MB");
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public Bitmap getCacheBitmap(String key) {
		if (lruCache.containsKey(key)) {
			SoftReference<Bitmap> softRef = lruCache.get(key);
			return softRef.get();
		}
		return null;
	}

    /**
     * ��ӻ���ͼƬ��KEY�ظ������Ǵ洢
     * @param key
     * @param bitmap
     */
	@Override
	public void putCacheBitmap(String key, Bitmap bitmap) {
		try {
			if (!lruCache.containsKey(key)){
				lruCache.put(key, new SoftReference<Bitmap>(bitmap));
				currentCacheSize += getBitmapSize(bitmap);
				autoCheckSize();
			}
		} catch (Throwable th) {
			th.printStackTrace();
		}
	}
	
	/**
	 * ������л���ͼƬ
	 */
	@Override
	public void clearAllCache() {
		lruCache.clear();
	}
	
	/**
	 * 
	 * @param key
	 */
	@Override
	public void removeCache(String key){
		if (lruCache.containsKey(key)) {
			lruCache.remove(key);
		}
	}

	private void autoCheckSize() {
		Log.i(TAG, "cache size=" + currentCacheSize + " length=" + lruCache.size());
		if (currentCacheSize > limit) {
			Iterator<Entry<String, SoftReference<Bitmap>>> iter = lruCache.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<String, SoftReference<Bitmap>> entry = iter.next();
				currentCacheSize -= getBitmapSize(entry.getValue().get());
				iter.remove();//�Ƴ������ã����������ϻ��գ��Ƿ���BITMAP����������
				if (currentCacheSize <= limit)
					break;
			}
		}
		Log.i(TAG, "Clean cache. New size " + lruCache.size());
	}

	/**
	 * ͼƬռ�õ��ڴ�
	 * @return
	 */
	private long getBitmapSize(Bitmap bitmap) {
		if (bitmap != null){
			return bitmap.getRowBytes() * bitmap.getHeight();
		}
		return 0;
	}
}
