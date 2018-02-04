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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import com.tseluikoartem.ening.yandexmobdevproject.R;

import launcher.LauncherRecyclerAbstractAdapter;

import java.util.ArrayList;
import java.util.List;
import database.AppsDbHelper;
import launcher.AppModel;
import launcher.ApplicationOperationsReciver;
import launcher.IconGridRecycleAdapter;
import launcher.OffsetItemDecoration;
import welcomepage.AppWelcomeInfoActivity;
import welcomepage.GreetingActivity;

public class ScrollingLauncActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private boolean mIsFirstLaunch;
    private ApplicationOperationsReciver mReciver;
    private List<AppModel> mData;
    private RecyclerView mLauncherRecyclerView;
    private AppsDbHelper mBdHelper;
    private PackageManager mPackageManager;


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
                final Intent intent = new Intent(getApplicationContext(), GreetingActivity.class);
                startActivity(intent);
            }
        });

        mPackageManager = getPackageManager();
        mBdHelper = new AppsDbHelper(this);
        if(mIsFirstLaunch)
            mData = loadDataFromSystem(mBdHelper,mPackageManager);
        else mData = loadDataFromDB(mBdHelper,mPackageManager);

        mLauncherRecyclerView = findViewById(R.id.louncher_content);

        mReciver = new ApplicationOperationsReciver();
        createGridLayout();//в этом методе происходит получение ресивером адаптера

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package");
        registerReceiver(mReciver, intentFilter);

    }


    private LauncherRecyclerAbstractAdapter createGridLayout() {
        final int offset = 8;
        mLauncherRecyclerView.addItemDecoration(new OffsetItemDecoration(offset));
        final int spanCount = 4;

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, spanCount);
        mLauncherRecyclerView.setLayoutManager(gridLayoutManager);
        final IconGridRecycleAdapter iconGridRecycleAdapter = new IconGridRecycleAdapter(this.mData,mPackageManager,this,0);
        mLauncherRecyclerView.setAdapter(iconGridRecycleAdapter);
        return iconGridRecycleAdapter;
    }

    private LauncherRecyclerAbstractAdapter createLinearLayout() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mLauncherRecyclerView.setLayoutManager(layoutManager);
        final IconGridRecycleAdapter linearRecycleAdapter = new IconGridRecycleAdapter(this.mData,mPackageManager,this,1);
        mLauncherRecyclerView.setAdapter(linearRecycleAdapter);
        return linearRecycleAdapter;
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
        if (id == R.id.nav_launcher) {
            createGridLayout();
        } else if (id == R.id.nav_list) {
            createLinearLayout();
        } else if (id == R.id.nav_settings) {
           // intent=new Intent(this,SettingsActivity.class);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_launcher_activity);
        drawer.closeDrawer(GravityCompat.START);
        //startActivity(intent);
        return true;
    }

    private boolean checkForFirstLaunch(){
        final String SP_NAME = "spName";
        final String SP_KEY_FIRST_START = "spKeyFirstStart";
        SharedPreferences sp = getSharedPreferences(SP_NAME, MODE_PRIVATE);
        boolean firstStart = sp.getBoolean(SP_KEY_FIRST_START, true);
        if(firstStart) {
            sp.edit().putBoolean(SP_KEY_FIRST_START, false).apply();
            Intent intent = new Intent(this, AppWelcomeInfoActivity.class);
            startActivity(intent);
        }
        return firstStart;
    }

}
