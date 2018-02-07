package launcher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;

import java.util.List;

import database.AppsDbHelper;

/**
 * Created by ening on 06.02.18.
 */

public class AppLaunchClickListener implements View.OnClickListener {

    private Context mContext;
    private PackageManager mPackageManager;
    private List<AppModel> mData;
    private int mPosition;

    public AppLaunchClickListener(Context mContext, PackageManager mPackageManager, List<AppModel> mData, int mPosition) {
        this.mContext = mContext;
        this.mPackageManager = mPackageManager;
        this.mData = mData;
        this.mPosition = mPosition;
    }


    @Override
    public void onClick(View view) {
        final Intent intent = mPackageManager.getLaunchIntentForPackage(mData.get(mPosition).getName().toString());

        AppsDbHelper dbHelper = new AppsDbHelper(mContext);
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query(
                AppsDbHelper.appsDatabase.TABLE_NAME
                        + " WHERE " + AppsDbHelper.appsDatabase.Columns.COLUMN_APP_NAME
                        + " = '" + mData.get(mPosition).getName() + "';",
                null, null, null, null, null, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(cursor.getColumnIndex(AppsDbHelper.appsDatabase.Columns.COLUMN_APP_LAUNCHES_COUNT));
        }
        cursor.close();
        count++;
        Log.d("DB", "count - " + count);
        dbHelper.updateAppRecord(database, mData.get(mPosition).getName(), count);

        mContext.startActivity(intent);
    }
}
