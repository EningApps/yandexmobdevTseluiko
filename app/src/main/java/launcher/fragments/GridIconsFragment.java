package launcher.fragments;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tseluikoartem.ening.yandexmobdevproject.R;
import com.tseluikoartem.ening.yandexmobdevproject.activities.LauncherApplication;

import backgroundimage.ImagesLoadedReciver;
import backgroundimage.LauncherBackgroundChanger;


public class GridIconsFragment extends LauncherAbstractFragment {

    private RecyclerView mRecyclerView;
    private OnFragmentsContentInteractionListener mListener;

    public GridIconsFragment() {
        // Required empty public constructor
    }

    public static GridIconsFragment newInstance() {
        GridIconsFragment fragment = new GridIconsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onResume() {
        final String[] imagesFileNames = LauncherApplication.getInstance().getImagesFileNames();
        if(imagesFileNames!=null){
            new LauncherBackgroundChanger(mRecyclerView,getActivity(),2).execute(imagesFileNames);
        }
        super.onResume();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_icons_grid_layout, container, false);
        mListener.setRecyclerViewComponents(mRecyclerView,0);
        return mRecyclerView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentsContentInteractionListener) {
            mListener = (OnFragmentsContentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentsContentInteractionListener");
        }
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }


}
