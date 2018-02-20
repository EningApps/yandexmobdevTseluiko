package desktop.appchooser;

import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import com.tseluikoartem.ening.yandexmobdevproject.R;

import java.util.ArrayList;
import java.util.List;

import desktop.roomdatabase.AppDatabase;


public class AppChooseActivity extends AppCompatActivity {

    private List<AppModelPersistant> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_choose);

        RecyclerView recyclerView = findViewById(R.id.app_choose_recycler_view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        AppDatabase appDatabase =  Room.databaseBuilder(this, AppDatabase.class, "database").build();


        new LoadFromDBAsyncTask(this,appDatabase).execute();
        while (mData==null){
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
            }
        }
        IconRecycleAdapterPersistant adapter = new IconRecycleAdapterPersistant(mData,getPackageManager(),this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    public void setData(List<AppModelPersistant> data) {
        this.mData = data;
    }

    private class LoadFromDBAsyncTask extends AsyncTask<Void, Void, Void> {

        private AppChooseActivity activity;
        private AppDatabase appDatabase;

        public LoadFromDBAsyncTask(AppChooseActivity activity, AppDatabase appDatabase) {
            this.activity = activity;
            this.appDatabase = appDatabase;
        }
        @Override
        protected Void doInBackground(Void... voids) {

            AppDAOPersistant appDAOPersistant = appDatabase.appDaoPersistant();
            activity.setData((ArrayList)appDAOPersistant.getAll());
            return null;
        }
    }

}
