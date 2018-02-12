package launcher.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tseluikoartem.ening.yandexmobdevproject.R;

import backgroundimage.ImagesLoadedReciver;


public class GridIconsFragment extends LauncherAbstractFragment {

    private RecyclerView mRecyclerView;
    private View mRootView;
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
        final ImagesLoadedReciver imagesLoadedReciver = ImagesLoadedReciver.getInstance();
        imagesLoadedReciver.registerBackground(mRootView);
        super.onResume();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View layout = inflater.inflate(R.layout.fragment_icons_grid_layout, container, false);
        mRecyclerView = layout.findViewById(R.id.fragment_grid_recycler_view);
        mListener.setRecyclerViewComponents(mRecyclerView,0);
        mRootView = layout;
        return  layout;
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
