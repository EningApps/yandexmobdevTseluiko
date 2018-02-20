package desktop.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import java.util.List;

import desktop.roomdatabase.AppModel;


/**
 * Created by ening on 06.02.18.
 */

public class AppLaunchClickListener implements View.OnClickListener {

    private Context mContext;
    private PackageManager mPackageManager;
    private RecyclerView.Adapter mAdapter;
    private List<AppModel> mData;
    private int mPosition;

    public AppLaunchClickListener(Context mContext, PackageManager mPackageManager, List<AppModel> mData, int mPosition, RecyclerView.Adapter adapter) {
        this.mContext = mContext;
        this.mPackageManager = mPackageManager;
        this.mData = mData;
        this.mPosition = mPosition;
        this.mAdapter = adapter;
    }


    @Override
    public void onClick(View view) {
        mAdapter.notifyItemChanged(mPosition);
        final Intent intent = mPackageManager.getLaunchIntentForPackage(mData.get(mPosition).getName().toString());

        mContext.startActivity(intent);
    }
}
