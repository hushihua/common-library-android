package android.library.framework.image.multiplexloader;

import android.app.Activity;
import android.graphics.Bitmap;
import android.widget.ImageView;

class UpdateImageViewTask implements Runnable{

	private ImageView imageView;
	private Bitmap bitmap;
	
	public UpdateImageViewTask(ImageView imageView, Bitmap bitmap) {
		this.imageView = imageView;
		this.bitmap = bitmap;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (imageView != null) {
			imageView.setImageBitmap(bitmap);
		}
	}
	
	public void inflateImageBitmap(){
		if (imageView != null) {
			Activity context = (Activity)imageView.getContext();
			context.runOnUiThread(this);
//			imageView.post(this);
		}
	}
}
