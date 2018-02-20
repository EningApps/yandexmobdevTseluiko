
package desktop.recyclerview;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.tseluikoartem.ening.yandexmobdevproject.R;

import java.util.Collections;
import java.util.List;

import desktop.roomdatabase.AppModel;


public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ItemViewHolder>
        implements ItemTouchHelperAdapter {

    private List<AppModel> mData;
    private Context mContext;

    public List<AppModel> getmData() {
        return mData;
    }

    private final OnStartDragListener mDragStartListener;

    public RecyclerListAdapter(Context context, OnStartDragListener dragStartListener, List<AppModel> data) {
        mDragStartListener = dragStartListener;
        mContext = context;
        mData = data;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.icon_grid_layout, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {

        holder.titleTextView.setText(mData.get(position).getLabel());
        holder.iconView.setOnLongClickListener(new AppExtendedInfoShowLongClickListener(mContext,position,this,mData));

        holder.iconView.setOnClickListener(new AppLaunchClickListener(mContext,mContext.getPackageManager(),mData,position,this));

        holder.iconView.setOnLongClickListener(new AppExtendedInfoShowLongClickListener(mContext,position,this,mData));

        ApplicationInfo appInfo=null;
        try {
            appInfo = mContext.getPackageManager().getApplicationInfo(mData.get(position).getName(), 0);
        } catch (PackageManager.NameNotFoundException e) {

        }if(appInfo!=null) {
            holder.iconView.setBackground(appInfo.loadIcon(mContext.getPackageManager()));
        }

        holder.iconView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });
    }

    @Override
    public void onItemDismiss(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mData, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        notifyDataSetChanged();
        return true;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }



    public static class ItemViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        public final TextView titleTextView;
        public final View iconView;

        public ItemViewHolder(View view) {
            super(view);
            iconView = view.findViewById(R.id.icon_view);
            titleTextView = view.findViewById(R.id.icon_label);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }

}
