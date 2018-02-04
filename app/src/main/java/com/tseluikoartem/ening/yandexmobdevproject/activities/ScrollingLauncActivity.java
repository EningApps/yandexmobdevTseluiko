package com.tseluikoartem.ening.yandexmobdevproject.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;


import com.tseluikoartem.ening.yandexmobdevproject.R;

import java.util.ArrayList;
import java.util.List;

import database.AppsDbHelper;
import launcher.AppModel;
import launcher.ApplicationOperationsReciver;
import launcher.IconRecycleAdapter;
import launcher.OffsetItemDecoration;

public class ScrollingLauncActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private boolean mIsFirstLaunch;
    private ApplicationOperationsReciver mReciver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mIsFirstLaunch = checkForFirstLaunch();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_launcher_activity);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final View navigationHeaderView = navigationView.getHeaderView(0);
        final View profileImage = navigationHeaderView.findViewById(R.id.imageViewHeader);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(getApplicationContext(),GreetingActivity.class);
                startActivity(intent);
            }
        });

        mReciver = new ApplicationOperationsReciver();

        createGridLayout();//тут есть костыль на получение адаптера ресивером в методе createGridLayout()

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package");
        registerReceiver(mReciver, intentFilter);

    }


    private void createGridLayout() {
        final RecyclerView recyclerView = findViewById(R.id.louncher_content);
        recyclerView.setHasFixedSize(true);
        final int offset = 8;
        recyclerView.addItemDecoration(new OffsetItemDecoration(offset));

        final int spanCount = 4;
        final GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        recyclerView.setLayoutManager(layoutManager);

        final PackageManager packageManager = getPackageManager();
        List<AppModel> data = null;
        AppsDbHelper bsHelper = new AppsDbHelper(this);
        if(mIsFirstLaunch)
            data = loadDataFromSystem(bsHelper,packageManager);
        else data = loadDataFromDB(bsHelper,packageManager);
        final IconRecycleAdapter launcherAdapter = new IconRecycleAdapter(data,packageManager,this);
        recyclerView.setAdapter(launcherAdapter);
        mReciver.setAdapter(launcherAdapter);
    }

    private List<AppModel> loadDataFromDB(AppsDbHelper dbHelper,PackageManager packageManager){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                AppsDbHelper.appsDatabase.Columns.COLUMN_APP_NAME,
                AppsDbHelper.appsDatabase.Columns.COLUMN_APP_LABEL };
        List<AppModel> data = new ArrayList<>();
        Cursor cursor = db.query(AppsDbHelper.appsDatabase.TABLE_NAME,projection,null,null,null,null, null);
        while (cursor.moveToNext()) {
           AppModel appModel = new AppModel();
           appModel.setName(cursor.getString(cursor.getColumnIndex(AppsDbHelper.appsDatabase.Columns.COLUMN_APP_NAME)));
           appModel.setLabel(cursor.getString(cursor.getColumnIndex(AppsDbHelper.appsDatabase.Columns.COLUMN_APP_LABEL)));
           ApplicationInfo appInfo=null;
           try {
               appInfo = packageManager.getApplicationInfo(appModel.getName(),0);
           } catch (PackageManager.NameNotFoundException e) {
               Log.d("DB", "не найдено - " + appModel.getName());
           }
           appModel.setIcon(appInfo.loadIcon(packageManager));
           data.add(appModel);
        }
        return data;
    }

    private List<AppModel> loadDataFromSystem(AppsDbHelper dbHelper, PackageManager packageManager) {
        List<AppModel> apps = new ArrayList<>();
        List<ApplicationInfo> avalibleActivities = packageManager.getInstalledApplications(0);
        for(ApplicationInfo appInfo: avalibleActivities){
            if( packageManager.getLaunchIntentForPackage(appInfo.packageName) != null ) {
                AppModel appModel = new AppModel();
                String appName = appInfo.packageName;
                String appLabel = (String) appInfo.loadLabel(packageManager);
                Drawable appIcon = appInfo.loadIcon(packageManager);
                String sourceDir = appInfo.publicSourceDir;
                int appLaunchesCount = 0;
                long appInstalledTime = 0;
                try {
                    appInstalledTime = packageManager.getPackageInfo(appInfo.packageName,0).firstInstallTime;
                } catch (PackageManager.NameNotFoundException e) {
                    Log.d("APPMODEl","Not found");
                }

                dbHelper.insertAppRecord(dbHelper.getWritableDatabase(),appName,appLabel,sourceDir,appLaunchesCount,appInstalledTime);

                appModel.setName(appName);
                appModel.setIcon(appIcon);
                appModel.setLabel(appLabel);
                apps.add(appModel);
            }
        }
        return apps;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent=null;
        if (id == R.id.nav_launcher) {
            intent=new Intent(this,ScrollingLauncActivity.class);
        } else if (id == R.id.nav_list) {
            intent=new Intent(this,ScrollingListActivity.class);
        } else if (id == R.id.nav_settings) {
            intent=new Intent(this,SettingsActivity.class);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_launcher_activity);
        drawer.closeDrawer(GravityCompat.START);
        startActivity(intent);
        return true;
    }

    private boolean checkForFirstLaunch(){
        final String SP_NAME = "spName";
        final String SP_KEY_FIRST_START = "spKeyFirstStart";
        SharedPreferences sp = getSharedPreferences(SP_NAME, MODE_PRIVATE);
        boolean firstStart = sp.getBoolean(SP_KEY_FIRST_START, true);
        if(firstStart) {
            sp.edit().putBoolean(SP_KEY_FIRST_START, false).apply();
            Intent intent = new Intent(this, AppInfoActivity.class);
            startActivity(intent);
        }
        return firstStart;
    }


}
