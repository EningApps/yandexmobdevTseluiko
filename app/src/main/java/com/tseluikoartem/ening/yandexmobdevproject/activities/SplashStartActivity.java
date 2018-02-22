package com.tseluikoartem.ening.yandexmobdevproject.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;



import launcher.MainLauncherActivity;
import welcomepage.AppWelcomeInfoActivity;

import static utils.ApplicationConstants.SharedPreferenciesConstants.SHOW_WELCOMEPAGE_KEY;

public class SplashStartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isWelcomePageNeeded = sp.getBoolean(SHOW_WELCOMEPAGE_KEY, true);
        Intent intent = null;
        if(isWelcomePageNeeded){
             intent = new Intent(this, AppWelcomeInfoActivity.class);

        }else{
            intent = new Intent(this, MainLauncherActivity.class);
        }
        startActivity(intent);

    }
}
