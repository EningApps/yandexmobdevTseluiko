package com.tseluikoartem.ening.yandexmobdevproject;

import android.content.Context;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import desktop.RoomdatabaseSingleton;
import desktop.appchooser.AppChooseActivity;
import desktop.roomdatabase.AppDAO;
import desktop.roomdatabase.AppDatabase;
import desktop.roomdatabase.AppModel;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class AppChooseActivityTest {


    private AppDatabase roomDatabase;

    @Before
    public void init(){
        final Context context = Robolectric.buildActivity( AppChooseActivity.class ).get();
        roomDatabase = RoomdatabaseSingleton.getInstance(context);
    }

    @Test
    public void testDesktopData() {
        final AppDAO appDAO = roomDatabase.appDao();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<AppModel> daoData = appDAO.getAll();
                Assert.assertTrue(daoData.size() == 0);

            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<AppModel> appModels = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    appModels.add(new AppModel());
                }
                appDAO.insert(appModels);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<AppModel> daoData = appDAO.getAll();
                Assert.assertTrue(daoData.size() == 10);

            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {

                appDAO.deleteAll();
                List<AppModel> daoData = appDAO.getAll();
                Assert.assertTrue(daoData.size() == 0);
            }
        }).start();
    }

}