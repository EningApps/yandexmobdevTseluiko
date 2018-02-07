package launcher;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.tseluikoartem.ening.yandexmobdevproject.R;

import database.AppsDbHelper;

public class IconExtendedInformationActivity extends AppCompatActivity {

    private TextView mAppNameTextView, mAppLabelTextView, mAppLaunchCountTextView, mAppInstallTimeTextView, mSourceDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon_extended_information);
        mAppNameTextView = findViewById(R.id.textViewAppName);
        mAppLabelTextView = findViewById(R.id.textViewAppLabel);
        mAppLaunchCountTextView = findViewById(R.id.textViewAppLaunch);
        mAppInstallTimeTextView = findViewById(R.id.textViewInstalledTime);
        mSourceDir = findViewById(R.id.textViewSourceDir);


        final Intent fIntent = getIntent();
        int launchCount=0;
        long appInstallTime=0;
        String appLabel="", appSourceDir="";
        String appName = fIntent.getStringExtra("appname");
        if(appName!=null) {
            AppsDbHelper appsDbHelper = new AppsDbHelper(this);
            SQLiteDatabase database = appsDbHelper.getReadableDatabase();
            Cursor cursor = database.query(
                    AppsDbHelper.appsDatabase.TABLE_NAME
                            +" WHERE "+AppsDbHelper.appsDatabase.Columns.COLUMN_APP_NAME
                            +" = '"+appName+"';",
                    null,null,null,null,null,null);

            if (cursor.moveToFirst()) {
                appLabel = cursor.getString(cursor.getColumnIndex(AppsDbHelper.appsDatabase.Columns.COLUMN_APP_LABEL));
                launchCount = cursor.getInt(cursor.getColumnIndex(AppsDbHelper.appsDatabase.Columns.COLUMN_APP_LAUNCHES_COUNT));
                appInstallTime = cursor.getLong(cursor.getColumnIndex(AppsDbHelper.appsDatabase.Columns.COLUMN_APP_INSTAll_TIME));
                appSourceDir = cursor.getString(cursor.getColumnIndex(AppsDbHelper.appsDatabase.Columns.COLUMN_APP_SOURCE_DIR));
            }
        }
        mAppNameTextView.setText(appName);
        mAppLabelTextView.setText(appLabel);
        String launchCountStr =  String.valueOf(launchCount);
        mAppLaunchCountTextView.setText(
                                launchCountStr+
                                ((launchCountStr.endsWith("2")||launchCountStr.endsWith("3")||launchCountStr.endsWith("4"))
                                ? " раза":" раз"));
        mAppInstallTimeTextView.setText(String.valueOf(appInstallTime));
        mSourceDir.setText(appSourceDir);

    }
}
