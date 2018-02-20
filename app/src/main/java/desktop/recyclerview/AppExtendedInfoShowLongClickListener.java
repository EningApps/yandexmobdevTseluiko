package desktop.recyclerview;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.PopupMenu;


import com.tseluikoartem.ening.yandexmobdevproject.R;

import java.util.List;

import desktop.appchooser.AppDAOPersistant;
import desktop.appchooser.AppModelPersistant;
import desktop.roomdatabase.AppDatabase;
import desktop.roomdatabase.AppModel;


/**
 * Created by ening on 06.02.18.
 */

public class AppExtendedInfoShowLongClickListener implements View.OnLongClickListener{
    private Context context;
    private int viewPosition;
    private List<AppModel> mData;
    private RecyclerView.Adapter mAdapter;
    private AppDatabase database;

    public AppExtendedInfoShowLongClickListener(Context context, int viewPosition, RecyclerView.Adapter adapter, List<AppModel> data) {
        this.context = context;
        this.viewPosition = viewPosition;
        this.mAdapter = adapter;
        this.mData = data;
        this.database =  Room.databaseBuilder(context,//todo вынести это в синглтон
                AppDatabase.class, "database").build();
    }

    @Override
    public boolean onLongClick(final View view) {
        Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake_animation);
        view.startAnimation(shake);
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.inflate(R.menu.desktop_popmenu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.pop_menu_del:
                        final AppDAOPersistant appDAOPersistant = database.appDaoPersistant();
                        final AppModelPersistant appModelPersistant = new AppModelPersistant();
                        appModelPersistant.setLabel(mData.get(viewPosition).getLabel());
                        appModelPersistant.setName(mData.get(viewPosition).getName());
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                appDAOPersistant.insert(appModelPersistant);
                            }
                        }).start();

                        mData.remove(viewPosition);
                        mAdapter.notifyItemRemoved(viewPosition);
                        mAdapter.notifyItemRangeChanged(viewPosition, mData.size());
                        break;

                }
                return false;
            }
        });
        popupMenu.show();
        return true;
    }
}
