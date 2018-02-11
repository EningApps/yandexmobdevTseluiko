package launcher;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.tseluikoartem.ening.yandexmobdevproject.R;
import com.tseluikoartem.ening.yandexmobdevproject.activities.DevProfileActivity;
import com.tseluikoartem.ening.yandexmobdevproject.activities.LauncherApplication;
import com.tseluikoartem.ening.yandexmobdevproject.activities.SettingActivity;

import java.util.ArrayList;
import java.util.List;

import backgroundimage.LauncherBackgroundChanger;
import database.AppsDbHelper;
import launcher.fragments.GridIconsFragment;
import launcher.fragments.LauncherAbstractFragment;
import launcher.fragments.ListIconsFragment;
import launcher.fragments.OnFragmentsContentInteractionListener;
import utils.ApplicationConstants;
import utils.ImageViewRounder;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static utils.ApplicationConstants.SharedPreferenciesConstants.APPLICATION_SORT;
import static utils.ApplicationConstants.SharedPreferenciesConstants.IS_FIRST_LAUNCH_KEY;
import static utils.ApplicationConstants.SharedPreferenciesConstants.MAKET_TYPE_KEY;
import static utils.ApplicationConstants.SharedPreferenciesConstants.THEME_CHOICE_KEY;
import static utils.ApplicationConstants.SharedPreferenciesConstants.THEME_DARK;
import static utils.ApplicationConstants.SharedPreferenciesConstants.THEME_LIGHT;

public class MainLauncherActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnFragmentsContentInteractionListener {

    private boolean mIsFirstLaunch;
    private ApplicationOperationsReciver mReciver;
    private List<AppModel> mData;
    private List<LauncherAbstractFragment> mFragments;
    private AppsDbHelper mBdHelper;
    private PackageManager mPackageManager;
    private LauncherRecyclerAbstractAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        switchTheme();
        mIsFirstLaunch = checkIsFirstLaunch();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        findViews();

        mPackageManager = getPackageManager();
        mBdHelper = new AppsDbHelper(this);
        if(mIsFirstLaunch) {
            mData = loadDataFromSystem(mBdHelper, mPackageManager);
        }
        else {
            mData = loadDataFromDB(mBdHelper, mPackageManager);
        }

        mReciver = new ApplicationOperationsReciver();

        mFragments = new ArrayList<>();
        final GridIconsFragment gridIconsFragment = GridIconsFragment.newInstance();
        final ListIconsFragment listIconsFragment = ListIconsFragment.newInstance();
        mFragments.add(gridIconsFragment);
        mFragments.add(listIconsFragment);
        setLauncherFragment(gridIconsFragment);

        mReciver.setAdapter(mAdapter);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package");
        registerReceiver(mReciver, intentFilter);
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

    private void findViews(){
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
        FloatingActionButton floatingActionButton = findViewById(R.id.fab_launcher);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Мне просто очень нравится эта кнопка",Toast.LENGTH_SHORT).show();
            }
        });

        final ImageView profileImage = navigationHeaderView.findViewById(R.id.imageViewHeader);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.face);
        profileImage.setImageBitmap(ImageViewRounder.getRoundedBitmap(bitmap));
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(getApplicationContext(), DevProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setLauncherFragment(LauncherAbstractFragment fragment){
        final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentsContainer,fragment).commit();
    }

    private void sortData(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        int sortType = Integer.parseInt(sp.getString(APPLICATION_SORT, "0"));
        String orderBy = null;
        if(sortType==1) {
            orderBy = AppsDbHelper.appsDatabase.Columns.COLUMN_APP_INSTAll_TIME;
        }
        else if(sortType==2) {
            orderBy = AppsDbHelper.appsDatabase.Columns.COLUMN_APP_NAME;
        }
        else if(sortType==3) {
            orderBy = AppsDbHelper.appsDatabase.Columns.COLUMN_APP_NAME + " DESC";
        }
        else if(sortType==4) {
            orderBy = AppsDbHelper.appsDatabase.Columns.COLUMN_APP_LAUNCHES_COUNT + " DESC";
        }

        SQLiteDatabase db = mBdHelper.getReadableDatabase();
        String[] projection = {
                AppsDbHelper.appsDatabase.Columns.COLUMN_APP_NAME,
                AppsDbHelper.appsDatabase.Columns.COLUMN_APP_LABEL };
        List<AppModel> data = new ArrayList<>();
        Cursor cursor = db.query(AppsDbHelper.appsDatabase.TABLE_NAME,projection,null,null,null,null, orderBy);
        while (cursor.moveToNext()) {
            AppModel appModel = new AppModel();
            appModel.setName(cursor.getString(cursor.getColumnIndex(AppsDbHelper.appsDatabase.Columns.COLUMN_APP_NAME)));
            appModel.setLabel(cursor.getString(cursor.getColumnIndex(AppsDbHelper.appsDatabase.Columns.COLUMN_APP_LABEL)));
            ApplicationInfo appInfo = null;
            try {
                appInfo = mPackageManager.getApplicationInfo(appModel.getName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                Log.d("DB", "не найдено - " + appModel.getName());
            }
            appModel.setIcon(appInfo.loadIcon(mPackageManager));
            data.add(appModel);
        }
        mAdapter.setData(data);
        mAdapter.notifyDataSetChanged();
    }

    void switchTheme(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String themeType = sp.getString(THEME_CHOICE_KEY, THEME_LIGHT);
        if(themeType.equals(THEME_LIGHT)) {
            setTheme(R.style.AppLightTheme);
        }
        else if(themeType.equals(THEME_DARK)) {
            setTheme(R.style.AppDarkTheme);
        }
    }

    private boolean checkIsFirstLaunch(){
        SharedPreferences sp = getSharedPreferences(ApplicationConstants.SharedPreferenciesConstants.SP_FOR_DB_NAME,MODE_PRIVATE);
        boolean isFirstLaunch = sp.getBoolean(IS_FIRST_LAUNCH_KEY, true);
        sp.edit().putBoolean(IS_FIRST_LAUNCH_KEY,false).apply();
        return isFirstLaunch;
    }

    @Override
    protected void onResume(){
        super.onResume();
        sortData();
        RecyclerView.LayoutManager layoutManager = mFragments.get(0).getRecyclerView().getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            int spanCount = Integer.parseInt(sp.getString(MAKET_TYPE_KEY, "4"));
            if(getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE) {
                spanCount += 2;
            }
            ((GridLayoutManager) layoutManager).setSpanCount(spanCount);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReciver);
    }

    @Override
    public void setRecyclerViewComponents(RecyclerView recyclerView, int layoutType) {
        RecyclerView.LayoutManager layoutManager = null;
        if(layoutType==0) {
            final int offset = 8;
            recyclerView.addItemDecoration(new OffsetItemDecoration(offset));
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            int spanCount = Integer.parseInt(sp.getString(MAKET_TYPE_KEY, "4"));
            if (getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE) {
                spanCount += 2;
            }
            layoutManager = new GridLayoutManager(this, spanCount);
            mAdapter = new IconRecycleAdapter(this.mData, mPackageManager, this, 0);

        }else if(layoutType==1) {
            layoutManager = new LinearLayoutManager(this);
            mAdapter= new IconRecycleAdapter(this.mData, mPackageManager, this, 1);

        }
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
        mReciver.setAdapter(mAdapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_launcher) {
            setLauncherFragment(mFragments.get(0));
        } else if (id == R.id.nav_list) {
            setLauncherFragment(mFragments.get(1));
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingActivity.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_launcher_activity);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
