package desktop.roomdatabase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by ening on 17.02.18.
 */

@Dao
public interface AppDAO {

    @Query("SELECT * FROM appmodel")
    List<AppModel> getAll();

    @Query("SELECT * FROM appmodel WHERE mname = :appname")
    AppModel getByName(String appname);

    @Insert
    void insert(List<AppModel> appModel);

    @Update
    void update(AppModel appModel);

    @Delete
    void delete(AppModel appModel);

    @Query("DELETE FROM appmodel")
    void deleteAll();

}