package launcher;

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
import android.widget.ImageView;
import android.widget.Toast;

import com.tseluikoartem.ening.yandexmobdevproject.R;
import com.tseluikoartem.ening.yandexmobdevproject.activities.DevProfileActivity;
import com.tseluikoartem.ening.yandexmobdevproject.activities.SettingActivity;

import java.util.ArrayList;

import java.util.List;
import database.AppsDbHelper;
import utils.ImageViewRounder;
import welcomepage.AppWelcomeInfoActivity;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static utils.ApplicationConstants.SharedPreferenciesConstants.APPLICATION_SORT;
import static utils.ApplicationConstants.SharedPreferenciesConstants.MAKET_TYPE_KEY;
import static utils.ApplicationConstants.SharedPreferenciesConstants.SHOW_WELCOMEPAGE_KEY;
import static utils.ApplicationConstants.SharedPreferenciesConstants.THEME_CHOICE_KEY;
import static utils.ApplicationConstants.SharedPreferenciesConstants.THEME_DARK;
import static utils.ApplicationConstants.SharedPreferenciesConstants.THEME_LIGHT;

public class MainLauncherActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private boolean mIsFirstLaunch;
    private ApplicationOperationsReciver mReciver;
    private List<AppModel> mData;
    private RecyclerView mLauncherRecyclerView;
    private AppsDbHelper mBdHelper;
    private PackageManager mPackageManager;
    private LauncherRecyclerAbstractAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        switchTheme();
        mIsFirstLaunch = checkIsWelcomePageNeeded();

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

        mLauncherRecyclerView = findViewById(R.id.louncher_content);
        mAdapter = createGridLayout();//в этом методе происходит получение ресивером адаптера

        mReciver = new ApplicationOperationsReciver();

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
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        int spanCount = Integer.parseInt(sp.getString(MAKET_TYPE_KEY,"4"));
        if(getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE) {
            spanCount += 2;
        }
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, spanCount);
        mLauncherRecyclerView.setLayoutManager(gridLayoutManager);
        final IconRecycleAdapter iconRecycleAdapter = new IconRecycleAdapter(this.mData,mPackageManager,this,0);
        mLauncherRecyclerView.setAdapter(iconRecycleAdapter);
        return iconRecycleAdapter;
    }

    private LauncherRecyclerAbstractAdapter createLinearLayout() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mLauncherRecyclerView.setLayoutManager(layoutManager);
        final IconRecycleAdapter linearRecycleAdapter = new IconRecycleAdapter(this.mData,mPackageManager,this,1);
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

    @Override
    protected void onResume(){
        super.onResume();
        if(!mIsFirstLaunch) {
            sortData();
            RecyclerView.LayoutManager layoutManager = mLauncherRecyclerView.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
                int spanCount = Integer.parseInt(sp.getString(MAKET_TYPE_KEY, "4"));
                ((GridLayoutManager) layoutManager).setSpanCount(spanCount);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReciver);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_launcher) {
            mAdapter = createGridLayout();
        } else if (id == R.id.nav_list) {
            mAdapter = createLinearLayout();
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingActivity.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_launcher_activity);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean checkIsWelcomePageNeeded(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isWelcomePageNeeded = sp.getBoolean(SHOW_WELCOMEPAGE_KEY, true);
        if(isWelcomePageNeeded) {

            Intent intent = new Intent(this, AppWelcomeInfoActivity.class);
            startActivity(intent);
        }
        return isWelcomePageNeeded;
    }

}
