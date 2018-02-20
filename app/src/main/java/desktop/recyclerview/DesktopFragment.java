/*
 * Copyright (C) 2015 Paul Burke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package desktop.recyclerview;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.tseluikoartem.ening.yandexmobdevproject.R;

import java.util.ArrayList;
import java.util.List;

import desktop.appchooser.AppDAOPersistant;
import desktop.appchooser.AppModelPersistant;
import desktop.roomdatabase.AppDAO;
import desktop.roomdatabase.AppDatabase;
import desktop.roomdatabase.AppModel;
import launcher.fragments.LauncherAbstractFragment;


/**
 * @author Paul Burke (ipaulpro)
 */
public class DesktopFragment extends LauncherAbstractFragment implements OnStartDragListener {


    private ItemTouchHelper mItemTouchHelper;

    private ArrayList<AppModel> mAdapterData;


    private RecyclerListAdapter mAdapter;

    private AppDatabase appDatabase;

    private String addAppName;

    public DesktopFragment() {
    }

    @Override
    public void setArguments(Bundle args) {
        addAppName=args.getString("add_app_name");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View layout = inflater.inflate(R.layout.desktop_layout,container,false);
        return layout;
    }

    public static DesktopFragment newInstance() {
        DesktopFragment fragment = new DesktopFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        appDatabase =  Room.databaseBuilder(getActivity(),//todo вынести это в синглтон
                AppDatabase.class, "database").build();

        SharedPreferences preferences = getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        final boolean isInitNeeded = preferences.getBoolean("isInitNeeded",true);
        if(isInitNeeded) {
            loadDataFromSystem(getActivity().getPackageManager(), appDatabase);
            preferences.edit().putBoolean("isInitNeeded",false).apply();
        }else {
            new LoadFromDBAsyncTask(this,appDatabase,addAppName).execute();
        }

        while(mAdapterData==null){
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
            }
        }


        mAdapter = new RecyclerListAdapter(getActivity(), this,mAdapterData);

        RecyclerView recyclerView = view.findViewById(R.id.desktop_recycler);
        recyclerView.setAdapter(mAdapter);

        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 4);
        recyclerView.setLayoutManager(layoutManager);
        final int offset = 32;
        recyclerView.addItemDecoration(new OffsetItemDecoration(offset));

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    private void loadDataFromSystem(PackageManager packageManager, AppDatabase appDatabase ) {
        final ArrayList<AppModelPersistant> apps = new ArrayList<>();
        final AppDAOPersistant appDAOPersistant = appDatabase.appDaoPersistant();

        List<ApplicationInfo> avalibleActivities = packageManager.getInstalledApplications(0);
        for(ApplicationInfo appInfo: avalibleActivities){
            if( packageManager.getLaunchIntentForPackage(appInfo.packageName) != null ) {
                final AppModelPersistant appModelPersistant = new AppModelPersistant();
                String appName = appInfo.packageName;
                String appLabel = (String) appInfo.loadLabel(packageManager);

                appModelPersistant.setName(appName);
                appModelPersistant.setLabel(appLabel);
                apps.add(appModelPersistant);
            }
        }
        mAdapterData = new ArrayList<AppModel>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                appDAOPersistant.insert(apps);
            }
        }).start();

    }


    public void setmAdapterData(ArrayList<AppModel> mAdapterData) {
        this.mAdapterData = mAdapterData;
    }

    public ArrayList<AppModel> getmAdapterData() {
        return mAdapterData;
    }

    private class LoadFromDBAsyncTask extends AsyncTask<Void, Void, Void>{

        private DesktopFragment gridFragment;
        private AppDatabase appDatabase;
        private String addAppName;

        public LoadFromDBAsyncTask(DesktopFragment gridFragment, AppDatabase appDatabase, String addAppName) {
            this.gridFragment = gridFragment;
            this.appDatabase = appDatabase;
            this.addAppName = addAppName;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            AppDAOPersistant appDAOPersistant = appDatabase.appDaoPersistant();
            AppDAO appDAO = appDatabase.appDao();
            final List<AppModel> appModels = appDAO.getAll();
            if(addAppName!=null && appDAO.getByName(addAppName)==null){
                final AppModelPersistant appModelPersistant = appDAOPersistant.getByName(addAppName);
                final AppModel modelToAdd= new AppModel();
                modelToAdd.setName(appModelPersistant.getName());
                modelToAdd.setLabel(appModelPersistant.getLabel());
                appModels.add(modelToAdd);
                appDAOPersistant.delete(appModelPersistant.getName());
            }
            gridFragment.setmAdapterData((ArrayList)appModels);
            return null;
        }
    }

    @Override
    public void onResume() {
        mAdapter.notifyDataSetChanged();
        mAdapter.notifyItemRangeChanged(0,mAdapterData.size());
        super.onResume();
    }

    @Override
    public void onPause() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final AppDAO appDAO = appDatabase.appDao();
                appDAO.deleteAll();
                appDAO.insert(mAdapter.getmData());

            }
        }).start();
        super.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
