package com.tseluikoartem.ening.yandexmobdevproject.activities;


import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;


import com.tseluikoartem.ening.yandexmobdevproject.R;

import java.util.List;

import backgroundimage.ImageLoadJobService;

import static utils.ApplicationConstants.BackgroundImagesConstants.JOB_ID_LOAD_IMAGE;

public class SettingActivity extends PreferenceActivity {

    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            final boolean isUpdateImagesNeeded = sharedPreferences.getBoolean(key,false);
            if(isUpdateImagesNeeded){
                JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
                if (jobScheduler != null) {
                    jobScheduler.schedule(
                            new JobInfo.Builder(JOB_ID_LOAD_IMAGE,
                                    new ComponentName(getApplicationContext(), ImageLoadJobService.class))
                                    .setOverrideDeadline(0L)
                                    .build()
                    );
                }
                sharedPreferences.edit().putBoolean(key,false).apply();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    @Override
    protected void onResume() {
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(preferenceChangeListener);
        super.onResume();
    }

    @Override
    protected void onPause() {
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
        super.onPause();
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return true;
    }

    public static class ProfilePrefsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.pref_profile);
        }
    }


    /**
     * This fragment shows the preferences for the second header.
     */
    public static class GeneralPrefsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.pref_general);
        }
    }


}
