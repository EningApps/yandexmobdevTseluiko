package launcher;

import android.graphics.drawable.Drawable;

/**
 * Created by ening on 01.02.18.
 */

public class AppModel {
    private String mName, mLabel;
    private Drawable mIcon;

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

    public Drawable getIcon() {
        return mIcon;
    }

    public void setIcon(Drawable icon) {
        this.mIcon = icon;
    }


    @Override
    public String toString() {
        return "---------------------\n"+
                "AppModel : " +
                "mName='" + mName + '\'' +
                ", mLabel='" + mLabel + '\'' +
                ", mIcon=" + mIcon + '}';
    }
}
