package com.tseluikoartem.ening.yandexmobdevproject;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RadioButton;


import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import database.AppsDbHelper;
import desktop.recyclerview.DesktopFragment;
import launcher.AppModel;
import launcher.MainLauncherActivity;
import launcher.fragments.GridIconsFragment;
import launcher.fragments.LauncherAbstractFragment;
import launcher.fragments.ListIconsFragment;
import welcomepage.IconsAmountSettingsFragment;
import welcomepage.WelcomeSettingsActivity;

import static utils.ApplicationConstants.SharedPreferenciesConstants.MAKET_TYPE_KEY;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class MainLauncherActivityTest {

    private WelcomeSettingsActivity welcomeSettingsActivity;

    @Before
    public void setup(){
        welcomeSettingsActivity = Robolectric.buildActivity( WelcomeSettingsActivity.class ).create().start().resume().get();
    }

    @Test
    public void spanLargeCountTest() {
        final IconsAmountSettingsFragment fragment = IconsAmountSettingsFragment.newInstance();
        welcomeSettingsActivity.getSupportFragmentManager().beginTransaction().add(fragment,null).commit();
        final RadioButton standartMaket = fragment.getmLargeMaketButton();
        standartMaket.performClick();

        final MainLauncherActivity activity = Robolectric.buildActivity( MainLauncherActivity.class ).create().start().resume().get();
        final RecyclerView recyclerView = activity.getmFragments().get(0).getRecyclerView();
        if (recyclerView != null) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager && layoutManager != null) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
                sp.edit().putInt(MAKET_TYPE_KEY,4);
                int spanCount = Integer.parseInt(sp.getString(MAKET_TYPE_KEY, ""));
                Assert.assertEquals(5, spanCount);
            }
        }
    }

    @Test
    public void spanStandartCountTest() {
        final IconsAmountSettingsFragment fragment = IconsAmountSettingsFragment.newInstance();
        welcomeSettingsActivity.getSupportFragmentManager().beginTransaction().add(fragment,null).commit();
        final RadioButton standartMaket = fragment.getmStandartMaketButton();
        standartMaket.performClick();

        final MainLauncherActivity activity = Robolectric.buildActivity( MainLauncherActivity.class ).create().start().resume().get();
        final RecyclerView recyclerView = activity.getmFragments().get(0).getRecyclerView();
        if (recyclerView != null) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager && layoutManager != null) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
                sp.edit().putInt(MAKET_TYPE_KEY,4);
                int spanCount = Integer.parseInt(sp.getString(MAKET_TYPE_KEY, ""));
                Assert.assertEquals(4, spanCount);
            }
        }
    }

    @Test
    public void adapterDataTest() {
        final MainLauncherActivity activity = Robolectric.buildActivity( MainLauncherActivity.class ).create().get();
        final List<AppModel> data = activity.getmData();
        Assert.assertNotNull(data);
        final ArrayList<String> appNames = new ArrayList<>();
        for (AppModel appModel :data ) {
            appNames.add(appModel.getName());
        }
        Assert.assertTrue(appNames.contains("com.tseluikoartem.ening.yandexmobdevproject"));
    }

    @Test
    public void fragmentsOrderTest(){
        final MainLauncherActivity activity = Robolectric.buildActivity( MainLauncherActivity.class ).create().get();
        final List<LauncherAbstractFragment> fragments = activity.getmFragments();

        Assert.assertTrue(fragments.size()==3);
        Assert.assertTrue(fragments.get(0) instanceof GridIconsFragment);
        Assert.assertTrue(fragments.get(1) instanceof ListIconsFragment);
        Assert.assertTrue(fragments.get(2) instanceof DesktopFragment);
    }


    @Test
    public void dbHelperTest(){
        final MainLauncherActivity activity = Robolectric.buildActivity( MainLauncherActivity.class ).create().get();
        final AppsDbHelper dbHelper = new AppsDbHelper(activity);
        final SQLiteDatabase database = dbHelper.getWritableDatabase();
        final  String test_name = "testname";
        dbHelper.insertAppRecord(database,test_name,"", "",0,0);
        database.close();

        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                AppsDbHelper.appsDatabase.Columns.COLUMN_APP_NAME };
        List<AppModel> data = new ArrayList<>();
        Cursor cursor = db.query(AppsDbHelper.appsDatabase.TABLE_NAME,projection,null,null,null,null, null);
        while (cursor.moveToNext()) {
            AppModel appModel = new AppModel();
            appModel.setName(cursor.getString(cursor.getColumnIndex(AppsDbHelper.appsDatabase.Columns.COLUMN_APP_NAME)));
            data.add(appModel);
        }
        db.close();
        Assert.assertNotNull(data);
        final ArrayList<String> appNames = new ArrayList<>();
        for (AppModel appModel :data ) {
            appNames.add(appModel.getName());
        }
        Assert.assertTrue(appNames.contains(test_name));
    }
}