package launcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;

import database.AppsDbHelper;

public class ApplicationOperationsReciver extends BroadcastReceiver {

    private IconRecycleAdapter adapter;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        String appPackage = intent.getDataString();
        Log.d("RECIVER", "action - " +intent.getAction());
        Log.d("RECIVER", "package - " +appPackage);

        if(action.equals("android.intent.action.PACKAGE_ADDED")){
            PackageManager packageManager = adapter.getPackageManager();
            ApplicationInfo appInfo=null;
            try {
                appInfo = packageManager.getApplicationInfo(appPackage.substring("package:".length()),0);
            } catch (PackageManager.NameNotFoundException e) {
                Log.d("RECIVER", "не найдено - "+appPackage);
            }
            if(appInfo!=null) {
                AppModel appModel = new AppModel();

                AppsDbHelper dbHelper = new AppsDbHelper(context);

                String appName = appInfo.packageName;
                String appLabel = (String) appInfo.loadLabel(packageManager);
                Drawable appIcon = appInfo.loadIcon(packageManager);
                String appPermission = appInfo.publicSourceDir;
                int appLaunchesCount = 0;
                long appInstalledTime = 0;
                try {
                    appInstalledTime = packageManager.getPackageInfo(appInfo.packageName,0).firstInstallTime;
                } catch (PackageManager.NameNotFoundException e) {
                    Log.d("APPMODEl","Not found");
                }
                dbHelper.insertAppRecord(dbHelper.getWritableDatabase(),appName,appLabel,appPermission,appLaunchesCount,appInstalledTime);

                appModel.setName(appName);
                appModel.setIcon(appIcon);
                appModel.setLabel(appLabel);
                adapter.data.add(appModel);
                adapter.notifyItemRemoved(0);
                adapter.notifyItemRangeChanged(0, adapter.data.size());
            }
        }else if(action.equals("android.intent.action.PACKAGE_REMOVED")){
            for (int i = 0; i < adapter.data.size(); i++) {
                Log.d("RECIVER", "appname - " +adapter.data.get(i).getName());
                if(appPackage.contains(adapter.data.get(i).getName())){
                    String appName = adapter.data.get(i).getName();
                    AppsDbHelper dbHelper = new AppsDbHelper(context);
                    SQLiteDatabase database = dbHelper.getWritableDatabase();
                    database.delete(AppsDbHelper.appsDatabase.TABLE_NAME,
                            AppsDbHelper.appsDatabase.Columns.COLUMN_APP_NAME
                                        + " = '" +appName+"'",null);
                    adapter.data.remove(i);
                    adapter.notifyItemRemoved(i);
                    adapter.notifyItemRangeChanged(i, adapter.data.size());
                }
            }
        }
    }

    public IconRecycleAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(IconRecycleAdapter adapter) {
        this.adapter = adapter;
    }
}
