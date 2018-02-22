package com.tseluikoartem.ening.yandexmobdevproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import com.tseluikoartem.ening.yandexmobdevproject.activities.DevProfileActivity;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class DevProfileActivityTest {

    @Test
    public void testTextVisibility() {
        final DevProfileActivity activity = Robolectric.setupActivity(DevProfileActivity.class);
        final View mGitField, mFacebookField, mTwitterField, mVkField;
        mGitField = activity.findViewById(R.id.gitField);
        mTwitterField = activity.findViewById(R.id.twitterField);
        mFacebookField = activity.findViewById(R.id.facebookField);
        mVkField = activity.findViewById(R.id.vkField);

        Assert.assertTrue(mFacebookField.getVisibility()== View.VISIBLE);
        Assert.assertTrue(mGitField.getVisibility()== View.VISIBLE);
        Assert.assertTrue(mVkField.getVisibility()== View.VISIBLE);
        Assert.assertTrue(mTwitterField.getVisibility()== View.VISIBLE);
    }
    private Context context = Robolectric.buildActivity( DevProfileActivity.class ).get();

    @Test
    public void testTextInvisibility() {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putBoolean("show_facebook",false).commit();
        preferences.edit().putBoolean("show_github",false).commit();
        preferences.edit().putBoolean("show_vk",false).commit();
        preferences.edit().putBoolean("show_twitter",false).commit();

        final DevProfileActivity activity = Robolectric.buildActivity( DevProfileActivity.class ).create().resume().get();;
        final View mGitField, mFacebookField, mTwitterField, mVkField;
        mGitField = activity.findViewById(R.id.gitField);
        mTwitterField = activity.findViewById(R.id.twitterField);
        mFacebookField = activity.findViewById(R.id.facebookField);
        mVkField = activity.findViewById(R.id.vkField);

        Assert.assertTrue(mFacebookField.getVisibility()== View.INVISIBLE);
        Assert.assertTrue(mGitField.getVisibility()== View.INVISIBLE);
        Assert.assertTrue(mVkField.getVisibility()== View.INVISIBLE);
        Assert.assertTrue(mTwitterField.getVisibility()== View.INVISIBLE);
    }
}