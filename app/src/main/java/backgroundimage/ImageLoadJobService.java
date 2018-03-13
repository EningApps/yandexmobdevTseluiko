package backgroundimage;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;


import java.util.List;

import static utils.ApplicationConstants.BackgroundImagesConstants.*;

public class ImageLoadJobService extends JobService {


    @Override
    public boolean onStartJob(final JobParameters params) {
        int jobId = params.getJobId();
        if (jobId == JOB_ID_LOAD_IMAGE) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final List<String> loadedUrls = ImageLoader.getsInstance().getImageUrls();
                    final String[] imagesUrls = new String[loadedUrls.size()];
                    for (int i = 0; i < loadedUrls.size(); i++) {
                        imagesUrls[i]=loadedUrls.get(i);
                    }

                    final Intent broadcastIntent = new Intent(BROADCAST_ACTION_IMAGES_LOADED);
                    broadcastIntent.putExtra(BROADCAST_PARAM_IMAGES_NAMES, imagesUrls);
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
