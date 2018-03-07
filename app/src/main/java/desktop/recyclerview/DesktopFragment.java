
package desktop.recyclerview;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.tseluikoartem.ening.yandexmobdevproject.R;

import java.util.ArrayList;
import java.util.List;

import desktop.RoomdatabaseSingleton;
import desktop.appchooser.AppDAOPersistant;
import desktop.appchooser.AppModelChosen;
import desktop.roomdatabase.AppDAO;
import desktop.roomdatabase.AppDatabase;
import desktop.roomdatabase.AppModel;
import launcher.fragments.LauncherAbstractFragment;


public class DesktopFragment extends LauncherAbstractFragment implements OnStartDragListener {

    private View mRootLayout;

    private ItemTouchHelper mItemTouchHelper;

    private ArrayList<AppModel> mAdapterData;


    private RecyclerListAdapter mAdapter;

    private AppDatabase appDatabase;

    private Button searchButton;
    private EditText searchEditText;

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
        mRootLayout = layout.findViewById(R.id.root_desktopView);
        searchButton = layout.findViewById(R.id.buttonSearch);
        searchEditText = layout.findViewById(R.id.searchEditText);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEditText.setCursorVisible(false);
                final String url = "https://yandex.by/search/?lr=157&text="+searchEditText.getText().toString();
                if (url!=null && !searchEditText.getText().toString().equals("")) {
                    final Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                }
            }
        });
        searchEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEditText.setCursorVisible(true);
            }
        });
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                searchEditText.setCursorVisible(false);
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    final String url = "https://yandex.by/search/?lr=157&text="+searchEditText.getText().toString();
                    if (url!=null && !searchEditText.getText().toString().equals(""))  {
                        final Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));

                        startActivity(intent);
                    }
                    return true;
                }
                return false;
            }
        });
        return layout;
    }

    public static DesktopFragment newInstance() {
        DesktopFragment fragment = new DesktopFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appDatabase = RoomdatabaseSingleton.getInstance(getActivity());

        new LoadFromDBAsyncTask(this,appDatabase,addAppName).execute();

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

        mRootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mRootLayout.getWindowToken(), 0);
                searchEditText.setCursorVisible(false);
            }
        });

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
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
                final AppModelChosen appModelChosen = appDAOPersistant.getByName(addAppName);
                final AppModel modelToAdd= new AppModel();
                modelToAdd.setName(appModelChosen.getName());
                modelToAdd.setLabel(appModelChosen.getLabel());
                appModels.add(modelToAdd);
                appDAOPersistant.delete(appModelChosen.getName());

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
