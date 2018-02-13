package welcomepage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tseluikoartem.ening.yandexmobdevproject.R;


public class WelcomeGuide2 extends Fragment {


    private OnFragmentInteractionListener mListener;

    public WelcomeGuide2() {
        // Required empty public constructor
    }


    public static WelcomeGuide2 newInstance() {
        WelcomeGuide2 fragment = new WelcomeGuide2();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome_guide2, container, false);
    }
}
