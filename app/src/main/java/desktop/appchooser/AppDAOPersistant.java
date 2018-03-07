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

    @Query("SELECT * FROM AppModelChosen")
    List<AppModelChosen> getAll();

    @Query("SELECT * FROM AppModelChosen WHERE mname = :appname")
    AppModelChosen getByName(String appname);

    @Insert
    void insert(List<AppModelChosen> appModel);

    @Insert
    void insert(AppModelChosen appModel);

    @Update
    void update(AppModelChosen appModel);

    @Query("DELETE FROM AppModelChosen WHERE mname = :appname")
    void delete(String appname);

    @Query("DELETE FROM AppModelChosen")
    void deleteAll();

}