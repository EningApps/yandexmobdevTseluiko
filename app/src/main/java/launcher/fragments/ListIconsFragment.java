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


public class ListIconsFragment extends LauncherAbstractFragment {


    private RecyclerView mRecyclerView;
    private OnFragmentsContentInteractionListener mListener;

    public ListIconsFragment() {
        // Required empty public constructor
    }


    public static ListIconsFragment newInstance() {
        ListIconsFragment fragment = new ListIconsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View layout = inflater.inflate(R.layout.fragment_icons_list_layout, container, false);
        mRecyclerView = layout.findViewById(R.id.fragment_list_content);
        mListener.setRecyclerViewComponents(mRecyclerView,1);
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

    @Override
    public void onResume() {

        final String[] imagesFileNames = LauncherApplication.getInstance().getImagesFileNames();
        if(imagesFileNames!=null){
            new LauncherBackgroundChanger(mRecyclerView,getActivity(),3).execute(imagesFileNames);
        }
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }
}
