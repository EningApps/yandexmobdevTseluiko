package launcher;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.tseluikoartem.ening.yandexmobdevproject.R;
import com.tseluikoartem.ening.yandexmobdevproject.activities.IconExtendedInformationActivity;

import java.util.ArrayList;
import java.util.List;

import database.AppsDbHelper;

/**
 * Created by ening on 30.01.18.
 */

public class IconRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<AppModel> data;
    private PackageManager mPackageManager;
    private Context mContext;

    public IconRecycleAdapter(List<AppModel> data,PackageManager packageManager, Context context) {
        this.mContext = context;
        this.mPackageManager = packageManager;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.icon_layout, parent, false);
        return new IconHolder.GridHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final View iconView = ((IconHolder.GridHolder) holder).getIconColorView();
        iconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = mPackageManager.getLaunchIntentForPackage(data.get(position).getName().toString());

                AppsDbHelper dbHelper = new AppsDbHelper(mContext);
                SQLiteDatabase database = dbHelper.getReadableDatabase();
                Cursor cursor = database.query(
                        AppsDbHelper.appsDatabase.TABLE_NAME
                                +" WHERE "+AppsDbHelper.appsDatabase.Columns.COLUMN_APP_NAME
                                +" = '"+data.get(position).getName()+"';",
                        null,null,null,null,null,null);
                int count=0;
                if(cursor.moveToFirst())
                {
                    count = cursor.getInt(cursor.getColumnIndex(AppsDbHelper.appsDatabase.Columns.COLUMN_APP_LAUNCHES_COUNT));
                }
                cursor.close();
                count++;
                Log.d("DB", "count - " + count);
                dbHelper.updateAppRecord(database,data.get(position).getName(),count);

                mContext.startActivity(intent);
            }
        });
        iconView.setOnLongClickListener(new PopMenuShower(mContext,position));
        iconView.setBackground(data.get(position).getIcon());
        final TextView textView = (TextView) ((IconHolder.GridHolder) holder).getIconTextView();
        textView.setText(data.get(position).getLabel());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    private class PopMenuShower implements View.OnLongClickListener{
        private Context context;
        private int viewPosition;

        public PopMenuShower(Context context, int viewPosition) {
            this.context = context;
            this.viewPosition = viewPosition;
        }

        @Override
        public boolean onLongClick(final View view) {
            PopupMenu popupMenu = new PopupMenu(context, view);
            popupMenu.inflate(R.menu.icon_popmenu);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.pop_menu_del:
                            if(data.get(viewPosition).getName().toLowerCase().contains("yandexmobdevproject")){
                                Toast.makeText(context,"Нельзя удалить это произведение кодингого искусства",Toast.LENGTH_LONG).show();
                                break;
                            }
                            Intent delIntent = new Intent(Intent.ACTION_DELETE);
                            delIntent.setData(Uri.parse("package:"+data.get(viewPosition).getName()));
                            context.startActivity(delIntent);
                            AppsDbHelper dbHelper = new AppsDbHelper(context);
                            SQLiteDatabase database = dbHelper.getWritableDatabase();
                            database.delete(AppsDbHelper.appsDatabase.TABLE_NAME,
                                    AppsDbHelper.appsDatabase.Columns.COLUMN_APP_NAME
                                            + " = '" +data.get(viewPosition).getName()+"'",null);
                            data.remove(viewPosition);
                            notifyItemRemoved(viewPosition);
                            notifyItemRangeChanged(viewPosition, data.size());
                            break;
                        case R.id.pop_menu_fav:
                            //todo:implement something
                            break;
                        case R.id.pop_menu_info:

                            Intent intent = new Intent(context, IconExtendedInformationActivity.class);
                            intent.putExtra("appname",data.get(viewPosition).getName());
                            context.startActivity(intent);

                            break;
                    }
                    return false;
                }
            });
            popupMenu.show();
            return true;
        }

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
}

