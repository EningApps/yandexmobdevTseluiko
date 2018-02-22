package com.tseluikoartem.ening.yandexmobdevproject.activities;

import android.app.Application;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;


import backgroundimage.ImageLoadJobService;

import static utils.ApplicationConstants.BackgroundImagesConstants.JOB_ID_LOAD_IMAGE;

/**
 * Created by ening on 11.02.18.
 */

public class LauncherApplication extends Application  {

    private String[] imagesFileNames;

    @Override
    public void onCreate() {
        super.onCreate();



        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (jobScheduler != null) {
            jobScheduler.schedule(
                    new JobInfo.Builder(JOB_ID_LOAD_IMAGE,
                            new ComponentName(getApplicationContext(), ImageLoadJobService.class))
                            .setOverrideDeadline(0L)
                            //.setPeriodic(90005)
                            .build()
            );
        }
    }

}
