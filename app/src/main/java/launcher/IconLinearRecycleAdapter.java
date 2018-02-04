package launcher;

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

public class IconLinearRecycleAdapter extends LauncherRecyclerAbstractAdapter{

    private List<AppModel> data;
    private int layoutType;

    public IconLinearRecycleAdapter(List<AppModel> data, int layoutType) {
        this.data = data;
        this.layoutType = layoutType;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (layoutType==1)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.icon_linear_layout, parent, false);
        else view = LayoutInflater.from(parent.getContext()).inflate(R.layout.icon_grid_layout, parent, false);

        return new RecyclerViewHolder.IconHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,final int position) {
        final View iconView = ((RecyclerViewHolder.IconHolder) holder).getIconView();
//        iconView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final Intent intent = mPackageManager.getLaunchIntentForPackage(data.get(position).getName().toString());
//
//                AppsDbHelper dbHelper = new AppsDbHelper(mContext);
//                SQLiteDatabase database = dbHelper.getReadableDatabase();
//                Cursor cursor = database.query(
//                        AppsDbHelper.appsDatabase.TABLE_NAME
//                                +" WHERE "+AppsDbHelper.appsDatabase.Columns.COLUMN_APP_NAME
//                                +" = '"+data.get(position).getName()+"';",
//                        null,null,null,null,null,null);
//                int count=0;
//                if(cursor.moveToFirst())
//                {
//                    count = cursor.getInt(cursor.getColumnIndex(AppsDbHelper.appsDatabase.Columns.COLUMN_APP_LAUNCHES_COUNT));
//                }
//                cursor.close();
//                count++;
//                Log.d("DB", "count - " + count);
//                dbHelper.updateAppRecord(database,data.get(position).getName(),count);
//
//                mContext.startActivity(intent);
//            }
//        });
        //iconView.setOnLongClickListener(new IconGridRecycleAdapter.PopMenuShower(mContext,position));
        iconView.setBackground(data.get(position).getIcon());
        final TextView textView = (TextView) ((RecyclerViewHolder.IconHolder) holder).getTitleTextView();
        textView.setText(data.get(position).getLabel());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
