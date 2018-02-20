package desktop.appchooser;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by ening on 17.02.18.
 */

@Dao
public interface AppDAOPersistant {

    @Query("SELECT * FROM appmodelpersistant")
    List<AppModelPersistant> getAll();

    @Query("SELECT * FROM appmodelpersistant WHERE mname = :appname")
    AppModelPersistant getByName(String appname);

    @Insert
    void insert(List<AppModelPersistant> appModel);

    @Insert
    void insert(AppModelPersistant appModel);

    @Update
    void update(AppModelPersistant appModel);

    @Query("DELETE FROM appmodelpersistant WHERE mname = :appname")
    void delete(String appname);

    @Query("DELETE FROM appmodelpersistant")
    void deleteAll();

}