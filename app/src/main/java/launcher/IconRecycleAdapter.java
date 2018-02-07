package launcher;

import android.content.Context;
import android.content.pm.PackageManager;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.tseluikoartem.ening.yandexmobdevproject.R;

import java.util.List;

/**
 * Created by ening on 30.01.18.
 */

public class IconRecycleAdapter extends LauncherRecyclerAbstractAdapter{

    List<AppModel> data;
    private PackageManager mPackageManager;
    private Context mContext;
    private int layoutType;

    public IconRecycleAdapter(List<AppModel> data, PackageManager mPackageManager, Context mContext, int layoutType) {
        this.data = data;
        this.mPackageManager = mPackageManager;
        this.mContext = mContext;
        this.layoutType = layoutType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (layoutType==1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.icon_linear_layout, parent, false);
        }
        else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.icon_grid_layout, parent, false);
        }
        return layoutType==1?new RecyclerViewHolder.IconLinearHolder(view):new RecyclerViewHolder.IconGridHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof RecyclerViewHolder.IconGridHolder){
            final View iconView = ((RecyclerViewHolder.IconGridHolder) holder).getIconView();
            iconView.setOnClickListener(new AppLaunchClickListener(mContext,mPackageManager,data,position));
            iconView.setOnLongClickListener(new AppExtendedInfoShowLongClickListener(mContext,position,this));
            iconView.setBackground(data.get(position).getIcon());
            final TextView textView = (TextView) ((RecyclerViewHolder.IconGridHolder) holder).getTitleTextView();
            textView.setText(data.get(position).getLabel());

        }else if(holder instanceof RecyclerViewHolder.IconLinearHolder){
            final View iconField = ((RecyclerViewHolder.IconLinearHolder) holder).getIconField();
            final View iconView = ((RecyclerViewHolder.IconLinearHolder) holder).getIconView();
            iconField.setOnClickListener(new AppLaunchClickListener(mContext,mPackageManager,data,position));
            iconField.setOnLongClickListener(new AppExtendedInfoShowLongClickListener(mContext,position,this));
            iconView.setBackground(data.get(position).getIcon());
            final TextView textView = (TextView) ((RecyclerViewHolder.IconLinearHolder) holder).getTitleTextView();
            textView.setText(data.get(position).getLabel());
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public PackageManager getPackageManager() {
        return mPackageManager;
    }

    public void setPackageManager(PackageManager packageManager) {
        this.mPackageManager = packageManager;
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    @Override
    public List<AppModel> getData() {
        return data;
    }

    @Override
    public void setData(List<AppModel> data) {
        this.data = data;
    }
}

