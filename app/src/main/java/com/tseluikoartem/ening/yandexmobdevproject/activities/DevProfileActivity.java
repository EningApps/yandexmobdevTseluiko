package com.tseluikoartem.ening.yandexmobdevproject.activities;


import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.tseluikoartem.ening.yandexmobdevproject.R;
import com.yandex.metrica.YandexMetrica;

import backgroundimage.ImagesLoadedReciver;
import io.fabric.sdk.android.Fabric;
import utils.ImageViewRounder;

import static utils.ApplicationConstants.SharedPreferenciesConstants.SHOW_FACEBOOK_KEY;
import static utils.ApplicationConstants.SharedPreferenciesConstants.SHOW_GIT_KEY;
import static utils.ApplicationConstants.SharedPreferenciesConstants.SHOW_TWITTER_KEY;
import static utils.ApplicationConstants.SharedPreferenciesConstants.SHOW_VK_KEY;
import static utils.ApplicationConstants.SharedPreferenciesConstants.THEME_CHOICE_KEY;
import static utils.ApplicationConstants.SharedPreferenciesConstants.THEME_DARK;
import static utils.ApplicationConstants.SharedPreferenciesConstants.THEME_LIGHT;

public class DevProfileActivity extends AppCompatActivity {

    private ImageView mProfileImageView;
    private TextView mGitLink, mFacebookLink, mTwitterLink, mVkLink;
    private View mGitField, mFacebookField, mTwitterField, mVkField;
    private Bitmap mBackgroundBitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String themeType = sp.getString(THEME_CHOICE_KEY, THEME_LIGHT);
        if(themeType.equals(THEME_LIGHT)) {
            setTheme(R.style.AppLightTheme);
        }
        else if(themeType.equals(THEME_DARK)) {
            setTheme(R.style.AppDarkTheme);
        }

        YandexMetrica.reportEvent("Был выполнен переход в ProfileActivity");

        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.profile_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarProfile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.face);
        mProfileImageView = (ImageView) findViewById(R.id.faceImageView);
        mProfileImageView.setImageBitmap(ImageViewRounder.getRoundedBitmap(bitmap));

        mGitField=findViewById(R.id.gitField);
        mFacebookField=findViewById(R.id.facebookField);
        mTwitterField=findViewById(R.id.twitterField);
        mVkField=findViewById(R.id.vkField);


        mGitLink = findViewById(R.id.textViewGit);
        mFacebookLink = findViewById(R.id.textViewFacebook);
        mTwitterLink = findViewById(R.id.textViewTwitter);
        mVkLink = findViewById(R.id.textViewVk);


        if (savedInstanceState != null) {
            mBackgroundBitmap = (Bitmap) savedInstanceState.getParcelable("BackgroundBitmap");
        }

        setFieldsClickListeners();
        setFieldsVisibility();


        checkForUpdates();
    }

    private void setFieldsVisibility(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        final boolean showGit = sp.getBoolean(SHOW_GIT_KEY,true);
        final boolean showFacebook = sp.getBoolean(SHOW_FACEBOOK_KEY,true);
        final boolean showTwitter = sp.getBoolean(SHOW_TWITTER_KEY,true);
        final boolean showVk = sp.getBoolean(SHOW_VK_KEY,true);
        if(!showGit) {
            mGitField.setVisibility(View.INVISIBLE);
            mGitField.setClickable(false);
        }
        if(!showFacebook) {
            mFacebookField.setVisibility(View.INVISIBLE);
            mFacebookField.setClickable(false);
        }
        if(!showTwitter) {
            mTwitterField.setVisibility(View.INVISIBLE);
            mTwitterField.setClickable(false);
        }
        if(!showVk) {
            mVkField.setVisibility(View.INVISIBLE);
            mVkField.setClickable(false);
        }
    }

    private void setFieldsClickListeners(){
        mGitField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent browser_intent=new Intent(Intent.ACTION_VIEW, Uri.parse(mGitLink.getText().toString()));
                startActivity(browser_intent);
            }
        });
        mFacebookField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent browser_intent=new Intent(Intent.ACTION_VIEW, Uri.parse(mFacebookLink.getText().toString()));
                startActivity(browser_intent);
            }
        });
        mTwitterField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent browser_intent=new Intent(Intent.ACTION_VIEW, Uri.parse(mTwitterLink.getText().toString()));
                startActivity(browser_intent);
            }
        });
        mVkField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent browser_intent=new Intent(Intent.ACTION_VIEW, Uri.parse(mVkLink.getText().toString()));
                startActivity(browser_intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        final View rootView = findViewById(R.id.dev_prof_root_layout);
        if(mBackgroundBitmap!=null){
            rootView.setBackground(new BitmapDrawable(getResources(),mBackgroundBitmap));
        }else {
            mBackgroundBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
            final ImagesLoadedReciver imagesLoadedReciver = ImagesLoadedReciver.getInstance();
            imagesLoadedReciver.registerBackground(rootView);
            checkForCrashes();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterManagers();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterManagers();
    }

    private void checkForCrashes() {
        CrashManager.register(this);
    }

    private void checkForUpdates() {
        // Remove this for store builds!
        UpdateManager.register(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("BackgroundBitmap",mBackgroundBitmap);
        super.onSaveInstanceState(outState);
    }

    private void unregisterManagers() {
        UpdateManager.unregister();
    }

}
