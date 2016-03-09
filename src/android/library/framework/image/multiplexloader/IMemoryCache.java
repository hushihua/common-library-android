/**
 * 
 */
package android.library.framework.image.multiplexloader;

import android.graphics.Bitmap;

/**
 * @author hushihua
 *
 */
interface IMemoryCache {

	public void putCacheBitmap(String key, Bitmap bitmap);
	
	public Bitmap getCacheBitmap(String key);
	
	public void clearAllCache();
	
	public void removeCache(String key);
	
}
