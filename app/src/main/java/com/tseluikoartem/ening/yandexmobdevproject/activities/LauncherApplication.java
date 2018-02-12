package com.tseluikoartem.ening.yandexmobdevproject.activities;

import android.app.Application;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;

import com.yandex.metrica.YandexMetrica;

import backgroundimage.ImageLoadJobService;
import backgroundimage.ImagesLoadedReciver;
import utils.ApplicationConstants;

import static utils.ApplicationConstants.BackgroundImagesConstants.JOB_ID_LOAD_IMAGE;

/**
 * Created by ening on 11.02.18.
 */

public class LauncherApplication extends Application  {

    private String[] imagesFileNames;

    @Override
    public void onCreate() {
        super.onCreate();

        YandexMetrica.activate(getApplicationContext(), ApplicationConstants.YandexAppMetricaConstants.API_KEY);
        YandexMetrica.enableActivityAutoTracking(this);
        YandexMetrica.reportEvent("Лаунчер был запущен");

        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (jobScheduler != null) {
            jobScheduler.schedule(
                    new JobInfo.Builder(JOB_ID_LOAD_IMAGE,
                            new ComponentName(getApplicationContext(), ImageLoadJobService.class))
                            .setOverrideDeadline(0L)
                         //   .setPeriodic(90005)
                            .build()
            );
        }
        ImagesLoadedReciver imagesReciver = ImagesLoadedReciver.getInstance();

        registerReceiver(imagesReciver,
                new IntentFilter(ApplicationConstants.BackgroundImagesConstants.BROADCAST_ACTION_IMAGES_LOADED));

    }
}
