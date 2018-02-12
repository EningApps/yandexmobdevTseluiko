package backgroundimage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static utils.ApplicationConstants.BackgroundImagesConstants.*;

/**
 * Created by ening on 11.02.18.
 */

public class ImagesLoadedReciver extends BroadcastReceiver {

    private ImagesContainer mImagesContainer;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        String action = intent.getAction();
        if (BROADCAST_ACTION_IMAGES_LOADED.equals(action)) {
            final String[] imagesNames = intent.getStringArrayExtra(BROADCAST_PARAM_IMAGES_NAMES);
            mImagesContainer.setImages(imagesNames);

//            if (TextUtils.isEmpty(imagesNames[index]) == false) {
//                final Bitmap bitmap = ImageFileOperator.getInstance().loadImage(context, imagesNames[index]);
//                final Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);
//            }
        }
    }

    public void setImagesContainer(ImagesContainer mImagesContainer) {
        this.mImagesContainer = mImagesContainer;
    }

    public interface ImagesContainer {
        void setImages(String[] images);
    }
}

