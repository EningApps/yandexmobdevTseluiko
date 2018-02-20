package com.tseluikoartem.ening.yandexmobdevproject;


import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.FragmentPagerAdapter;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.Random;

import launcher.AppModel;
import launcher.IconRecycleAdapter;
import launcher.MainLauncherActivity;
import welcomepage.AppWelcomeInfoActivity;
import welcomepage.IconsAmountSettingsFragment;
import welcomepage.ThemeChoiceFragment;
import welcomepage.WelcomeGuide1;
import welcomepage.WelcomeGuide2;
import welcomepage.WelcomeGuide3;
import welcomepage.WelcomeSettingsActivity;
import static org.junit.Assert.assertNotNull;

public class LauncherAdapterTest {


    @Test
    public void testAdapterData() {
        ArrayList<AppModel> data= mock(ArrayList.class);
        int layouttype=1;

        IconRecycleAdapter adapter = mock(IconRecycleAdapter.class);
        Assert.assertNotNull(adapter.getData());

    }

}