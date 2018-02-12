package com.tseluikoartem.ening.yandexmobdevproject.activities;

import android.app.Application;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;

import backgroundimage.ImageLoadJobService;
import backgroundimage.ImagesLoadedReciver;
import utils.ApplicationConstants;

import static utils.ApplicationConstants.BackgroundImagesConstants.JOB_ID_LOAD_IMAGE;

/**
 * Created by ening on 11.02.18.
 */

public class LauncherApplication extends Application implements ImagesLoadedReciver.ImagesContainer {

    private String[] imagesFileNames;
    private static LauncherApplication lSauncherApplication;

    @Override
    public void onCreate() {
        super.onCreate();

        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (jobScheduler != null) {
            jobScheduler.schedule(
                    new JobInfo.Builder(JOB_ID_LOAD_IMAGE,
                            new ComponentName(getApplicationContext(), ImageLoadJobService.class))
                            .setOverrideDeadline(0L)
                            .build()
            );
        }
        ImagesLoadedReciver imagesReciver = new ImagesLoadedReciver();
        imagesReciver.setImagesContainer(this);
        registerReceiver(imagesReciver,
                new IntentFilter(ApplicationConstants.BackgroundImagesConstants.BROADCAST_ACTION_IMAGES_LOADED));

        lSauncherApplication = this;

    }

    public static LauncherApplication getInstance(){
        return lSauncherApplication;

    }

    @Override
    public void setImages(String[] images) {
        this.imagesFileNames = images;
    }

    public String[] getImagesFileNames() {
        return imagesFileNames;
    }
}
