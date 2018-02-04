package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import static database.AppsDbHelper.appsDatabase.Columns.COLUMN_APP_NAME;

/**
 * Created by ening on 03.02.18.
 */

public class AppsDbHelper extends SQLiteOpenHelper {

    public interface appsDatabase {

        static final String TABLE_NAME = "apps";

        static interface Columns extends BaseColumns {
            String COLUMN_APP_NAME = "name";
            String COLUMN_APP_LABEL = "label";
            String COLUMN_APP_LAUNCHES_COUNT = "launch";
            String COLUMN_APP_SOURCE_DIR = "source";
            String COLUMN_APP_INSTAll_TIME = "installtime";
        }

        static final String CREATE_TABLE_SCRIPT =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                        "(" +
                        COLUMN_APP_NAME + " TEXT, " +
                        Columns.COLUMN_APP_LABEL + " TEXT, " +
                        Columns.COLUMN_APP_LAUNCHES_COUNT + " INTEGER, " +
                        Columns.COLUMN_APP_SOURCE_DIR + " TEXT, " +
                        Columns.COLUMN_APP_INSTAll_TIME + " REAL" +
                        ")";

        static final String DROP_TABLE_SCRIPT =
                "DROP TABLE IF EXISTS " + TABLE_NAME;

    }


    static final int VERSION = 1;
    static final String DB_NAME = "appsdb.db";


    public AppsDbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(appsDatabase.CREATE_TABLE_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(appsDatabase.DROP_TABLE_SCRIPT);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void insertAppRecord(SQLiteDatabase db,String appName,String appLabel,String sourceDir,int launchesCount, long installTime){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_APP_NAME, appName);
        contentValues.put(appsDatabase.Columns.COLUMN_APP_LABEL, appLabel);
        contentValues.put(appsDatabase.Columns.COLUMN_APP_LAUNCHES_COUNT, launchesCount);
        contentValues.put(appsDatabase.Columns.COLUMN_APP_SOURCE_DIR, sourceDir);
        contentValues.put(appsDatabase.Columns.COLUMN_APP_INSTAll_TIME, installTime);
        try {
            db.insert(appsDatabase.TABLE_NAME, null, contentValues);
        } catch (SQLiteException e) {
            Log.d("DB", "insertion error : "+ e.getMessage());
        }

    }
    public void updateAppRecord(SQLiteDatabase db,String appName,int launchesCount){
        ContentValues contentValues = new ContentValues();
        contentValues.put(appsDatabase.Columns.COLUMN_APP_LAUNCHES_COUNT, launchesCount);
        try {
            db.update(appsDatabase.TABLE_NAME,contentValues,COLUMN_APP_NAME+" = "+"'"+appName+"'",null);
        } catch (SQLiteException e) {
            Log.d("DB", "update error : "+ e.getMessage());
        }
    }

}

