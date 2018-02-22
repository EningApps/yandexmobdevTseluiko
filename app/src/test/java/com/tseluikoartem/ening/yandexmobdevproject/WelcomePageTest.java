package com.tseluikoartem.ening.yandexmobdevproject;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.RadioButton;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;


import java.util.Random;

import welcomepage.IconsAmountSettingsFragment;
import welcomepage.ThemeChoiceFragment;
import welcomepage.WelcomeGuide1;
import welcomepage.WelcomeGuide2;
import welcomepage.WelcomeGuide3;
import welcomepage.WelcomeSettingsActivity;
import static utils.ApplicationConstants.SharedPreferenciesConstants.*;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class WelcomePageTest {

    @Test
    public void testSettingsFragmentsOrder() {
        final WelcomeSettingsActivity mActivity = Robolectric.buildActivity( WelcomeSettingsActivity.class ).create().resume().get();
        final FragmentPagerAdapter viewPagerAdapter = mActivity.getmFragmentPagerAdapter();
        Assert.assertTrue(viewPagerAdapter.getItem(0) instanceof WelcomeGuide1);
        Assert.assertTrue(viewPagerAdapter.getItem(1) instanceof WelcomeGuide2);
        Assert.assertTrue(viewPagerAdapter.getItem(2) instanceof WelcomeGuide3);
        Assert.assertTrue(viewPagerAdapter.getItem(3) instanceof ThemeChoiceFragment);
        Assert.assertTrue(viewPagerAdapter.getItem(4) instanceof IconsAmountSettingsFragment);
        Assert.assertTrue(viewPagerAdapter.getItem(new Random().nextInt(1000)+5) instanceof IconsAmountSettingsFragment);
    }

    @Test
    public void testThemeChoiceFragment(){
        final WelcomeSettingsActivity mActivity = Robolectric.buildActivity( WelcomeSettingsActivity.class ).create().resume().get();
        final IconsAmountSettingsFragment fragment = IconsAmountSettingsFragment.newInstance();
        mActivity.getSupportFragmentManager().beginTransaction().add(fragment,null).commit();
        final RadioButton standartMaket = fragment.getmStandartMaketButton();
        final RadioButton largeMaket = fragment.getmLargeMaketButton();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        largeMaket.performClick();
        Assert.assertEquals("5", preferences.getString(MAKET_TYPE_KEY,""));
        standartMaket.performClick();
        Assert.assertEquals("4", preferences.getString(MAKET_TYPE_KEY,""));
    }
}