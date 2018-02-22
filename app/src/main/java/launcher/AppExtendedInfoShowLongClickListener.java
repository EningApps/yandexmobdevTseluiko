package launcher;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.tseluikoartem.ening.yandexmobdevproject.R;

import java.util.List;

import database.AppsDbHelper;

/**
 * Created by ening on 06.02.18.
 */

public class AppExtendedInfoShowLongClickListener implements View.OnLongClickListener{
    private Context context;
    private int viewPosition;
    private List<AppModel> mData;
    private LauncherRecyclerAbstractAdapter mAdapter;

    public AppExtendedInfoShowLongClickListener(Context context, int viewPosition, LauncherRecyclerAbstractAdapter adapter) {
        this.context = context;
        this.viewPosition = viewPosition;
        this.mAdapter = adapter;
        this.mData = adapter.getData();
    }

    @Override
    public boolean onLongClick(final View view) {
        Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake_animation);
        view.startAnimation(shake);
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.inflate(R.menu.icon_popmenu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.pop_menu_del:
                        if(mData.get(viewPosition).getName().toLowerCase().contains("yandexmobdevproject")){
                            Toast.makeText(context,"Нельзя удалить это произведение кодингого искусства",Toast.LENGTH_LONG).show();
                            break;
                        }

                        String eventDetete = "{\"name\":\""+mData.get(viewPosition).getName()+"\"}";

                        Intent delIntent = new Intent(Intent.ACTION_DELETE);
                        delIntent.setData(Uri.parse("package:"+ mData.get(viewPosition).getName()));
                        context.startActivity(delIntent);
                        AppsDbHelper dbHelper = new AppsDbHelper(context);
                        SQLiteDatabase database = dbHelper.getWritableDatabase();
                        database.delete(AppsDbHelper.appsDatabase.TABLE_NAME,
                                AppsDbHelper.appsDatabase.Columns.COLUMN_APP_NAME
                                        + " = '" + mData.get(viewPosition).getName()+"'",null);
                        mData.remove(viewPosition);
                        mAdapter.notifyItemRemoved(viewPosition);
                        mAdapter.notifyItemRangeChanged(viewPosition, mData.size());
                        break;
                    case R.id.pop_menu_settings:
                        final Intent settingsIntent = new Intent();
                        settingsIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", mData.get(viewPosition).getName(), null);
                        settingsIntent.setData(uri);
                        context.startActivity(settingsIntent);

                        String eventSetngs= "{\"name\":\""+mData.get(viewPosition).getName()+"\"}";

                        break;
                    case R.id.pop_menu_info:
                        final Intent infoIntent = new Intent(context, IconExtendedInformationActivity.class);
                        infoIntent.putExtra("appname", mData.get(viewPosition).getName());
                        context.startActivity(infoIntent);

                        String eventInfo = "{\"name\":\""+mData.get(viewPosition).getName()+"\"}";

                        break;
                }
                return false;
            }
        });
        popupMenu.show();
        return true;
    }
}
