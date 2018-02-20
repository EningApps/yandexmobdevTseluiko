package com.tseluikoartem.ening.yandexmobdevproject;


import android.support.v4.app.FragmentPagerAdapter;

import junit.framework.Assert;

import org.junit.Test;


import java.util.Random;

import welcomepage.IconsAmountSettingsFragment;
import welcomepage.ThemeChoiceFragment;
import welcomepage.WelcomeGuide1;
import welcomepage.WelcomeGuide2;
import welcomepage.WelcomeGuide3;
import welcomepage.WelcomeSettingsActivity;
import static org.junit.Assert.assertNotNull;

public class WelcomePageTest {

    private WelcomeSettingsActivity mActivity;


    @Test
    public void shouldNotBeNull() throws Exception
    {
        assertNotNull( mActivity);
    }



    @Test
    public void testFragmentsOrder() {
        final FragmentPagerAdapter viewPagerAdapter = mActivity.getmFragmentPagerAdapter();
        Assert.assertEquals(viewPagerAdapter.getItem(0), WelcomeGuide1.newInstance());
        Assert.assertEquals(viewPagerAdapter.getItem(1), WelcomeGuide2.newInstance());
        Assert.assertEquals(viewPagerAdapter.getItem(2), WelcomeGuide3.newInstance());
        Assert.assertEquals(viewPagerAdapter.getItem(3), ThemeChoiceFragment.newInstance());
        Assert.assertEquals(viewPagerAdapter.getItem(4), IconsAmountSettingsFragment.newInstance());
        Assert.assertEquals(viewPagerAdapter.getItem(new Random().nextInt(1000)+5), IconsAmountSettingsFragment.newInstance());
    }




}