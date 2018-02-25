package desktop.appchooser;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.tseluikoartem.ening.yandexmobdevproject.R;

import java.util.List;

import desktop.RoomdatabaseSingleton;
import desktop.roomdatabase.AppDatabase;
import launcher.MainLauncherActivity;

/**
 * Created by ening on 30.01.18.
 */

public class IconRecycleAdapterPersistant extends RecyclerView.Adapter{

    List<AppModelPersistant> data;
    private PackageManager mPackageManager;
    private Context mContext;

    private AppDatabase database;

    public IconRecycleAdapterPersistant(List<AppModelPersistant> data, PackageManager mPackageManager, Context mContext) {
        this.data = data;
        this.mPackageManager = mPackageManager;
        this.mContext = mContext;
        database = RoomdatabaseSingleton.getInstance(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.icon_linear_layout, parent, false);
        return new DesktopRecyclerViewHolder.IconLinearHolder(view);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final View iconField = ((DesktopRecyclerViewHolder.IconLinearHolder) holder).getIconField();
        final View iconView = ((DesktopRecyclerViewHolder.IconLinearHolder) holder).getIconView();
        iconField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(mContext, MainLauncherActivity.class);
                intent.putExtra("app_name",data.get(position).getName());
                final AppDAOPersistant appDAOPersistant = database.appDaoPersistant();
                mContext.startActivity(intent);
            }
        });
        ApplicationInfo appInfo=null;
        try {
            appInfo = mPackageManager.getApplicationInfo(data.get(position).getName(), 0);
        } catch (PackageManager.NameNotFoundException e) {}
        iconView.setBackground(appInfo.loadIcon(mPackageManager));
        final TextView textView = (TextView) ((DesktopRecyclerViewHolder.IconLinearHolder) holder).getTitleTextView();
        textView.setText(data.get(position).getLabel());

    }


    public PackageManager getPackageManager() {
        return mPackageManager;
    }

    public void setPackageManager(PackageManager packageManager) {
        this.mPackageManager = packageManager;
    }

}

