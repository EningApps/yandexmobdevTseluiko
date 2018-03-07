package desktop.roomdatabase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import desktop.appchooser.AppDAOPersistant;
import desktop.appchooser.AppModelChosen;


/**
 * Created by ening on 17.02.18.
 */

@Database(entities = {AppModel.class, AppModelChosen.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AppDAO appDao();
    public abstract AppDAOPersistant appDaoPersistant();
}