package desktop;

import android.arch.persistence.room.Room;
import android.content.Context;

import desktop.roomdatabase.AppDatabase;

/**
 * Created by ening on 20.02.18.
 */

public class RoomdatabaseSingleton
{
    private static AppDatabase database;

    public static AppDatabase getInstance(Context context) {
        if (database == null){
            database =  Room.databaseBuilder(context,AppDatabase.class, "database").build();
        }
        return database;
    }
}
