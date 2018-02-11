package backgroundimage;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;

import java.util.List;

import static utils.ApplicationConstants.BackgroundImagesConstants.*;

public class ImageLoadJobService extends JobService {

    private final ImageLoader mImageLoader;

    public ImageLoadJobService() {
        mImageLoader = new ImageLoader();
    }

    @Override
    public boolean onStartJob(final JobParameters params) {
        int jobId = params.getJobId();
        if (jobId == JOB_ID_LOAD_IMAGE) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final List<String> imageUrls = mImageLoader.getImageUrls();
                    final String[] imagesNames = new String[imageUrls.size()];
                    for (int i = 0; i < imageUrls.size(); i++) {
                        if (TextUtils.isEmpty(imageUrls.get(i)) == false) {
                            final Bitmap bitmap = mImageLoader.loadBitmap(imageUrls.get(i));
                            final String imageName = "BackgroundImage"+i+".png";
                            ImageFileOperator.getInstance().saveImage(getApplicationContext(), bitmap, imageName);
                            imagesNames[i]=imageName;
                        }
                    }
                    final Intent broadcastIntent = new Intent(BROADCAST_ACTION_IMAGES_LOADED);
                    broadcastIntent.putExtra(BROADCAST_PARAM_IMAGES_NAMES, imagesNames);
                    sendBroadcast(broadcastIntent);
                    jobFinished(params, false);
                }
            }).start();
            return true;
        }
        return false;
    }

    @Override
    public boolean onStopJob(final JobParameters params) {
        return false;
    }

}
