package desktop.roomdatabase;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity
public class AppModel {

    @NonNull
    @PrimaryKey
    private String mName;

    private String mLabel;


    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        this.mLabel = label;
    }


    @Override
    public String toString() {
        return "---------------------\n"+
                "AppModel : " +
                "mName='" + mName + '\'' +
                ", mLabel='" + mLabel + '\'' + '}';
    }

}
