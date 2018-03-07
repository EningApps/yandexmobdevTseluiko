package desktop.appchooser;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


import com.tseluikoartem.ening.yandexmobdevproject.R;

import java.util.ArrayList;
import java.util.List;

import desktop.roomdatabase.AppDatabase;


public class AppChooseActivity extends Activity {

    private List<AppModelChosen> mData;
    private IconRecycleAdapterPersistant mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_app_choose);

        EditText searchEditText = findViewById(R.id.search_app_edit_text);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

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
        mAdapter = new IconRecycleAdapterPersistant(mData,getPackageManager(),this);

        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void filter(String text) {
        ArrayList<AppModelChosen> filterdData = new ArrayList<>();

        for (AppModelChosen modelPersistant : mData) {
            if (modelPersistant.getLabel().toLowerCase().contains(text.toLowerCase())) {
                filterdData.add(modelPersistant);
            }
        }
        mAdapter.filterList(filterdData);
    }

    public void setData(List<AppModelChosen> data) {
        this.mData = data;
    }

    public List<AppModelChosen> getmData() {
        return mData;
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

    @Override
    protected void onPause() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(findViewById(R.id.app_choose_recycler_view).getWindowToken(), 0);
        super.onPause();
    }
}
