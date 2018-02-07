package launcher;

import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * Created by ening on 04.02.18.
 */

public abstract class LauncherRecyclerAbstractAdapter extends RecyclerView.Adapter {

    List<AppModel> data;

    public List<AppModel> getData() {
        return data;
    }

    public void setData(List<AppModel> data) {
        this.data = data;
    }
}
