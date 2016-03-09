package android.library.base.util;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.NinePatch;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;

public class ResourceUtil {

	public static Drawable getDrawabldeFromAssets(Context context, String imageFileName) {
		Drawable bitmap = null;
		AssetManager assetManager = context.getAssets();
		InputStream is = null;
		try {
			is = assetManager.open(imageFileName);
			bitmap = Drawable.createFromStream(is, null);
			is.close();
			is = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}
	
	
	public static Drawable getNinePatchDrawableFromAssets(Context context, String imageFileName) {
		InputStream stream = null;
		try {
			stream = context.getAssets().open(imageFileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Bitmap bitmap = BitmapFactory.decodeStream(stream);
		byte[] chunk = bitmap.getNinePatchChunk();
		boolean bResult = NinePatch.isNinePatchChunk(chunk);
		NinePatchDrawable patchy = new NinePatchDrawable(bitmap, chunk,new Rect(), null);
		return patchy;
	}

}
