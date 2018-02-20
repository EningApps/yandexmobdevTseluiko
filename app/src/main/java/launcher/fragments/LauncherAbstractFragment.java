package launcher.fragments;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

/**
 * Created by ening on 10.02.18.
 */

public class LauncherAbstractFragment extends Fragment {

    private RecyclerView recyclerView;

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }
}
