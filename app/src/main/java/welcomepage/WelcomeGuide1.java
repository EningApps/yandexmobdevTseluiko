package welcomepage;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tseluikoartem.ening.yandexmobdevproject.R;

public class WelcomeGuide1 extends Fragment {


    public WelcomeGuide1() {
        // Required empty public constructor
    }


    public static WelcomeGuide1 newInstance() {
        WelcomeGuide1 fragment = new WelcomeGuide1();

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
        return inflater.inflate(R.layout.fragment_welcome_guide1, container, false);
    }

}
