package welcomepage;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tseluikoartem.ening.yandexmobdevproject.R;
import com.tseluikoartem.ening.yandexmobdevproject.activities.ScrollingLauncActivity;


public class WelcomeSettingsFragment extends Fragment {

   private static final String PARAM_KEY="FRAGMENT_PARAM_KEY";

   private int mPosition;

    private OnFragmentInteractionListener mListener;

    public WelcomeSettingsFragment() {
        // Required empty public constructor
    }


    public static WelcomeSettingsFragment newInstance(int position) {
        WelcomeSettingsFragment fragment = new WelcomeSettingsFragment();
        Bundle args = new Bundle();
        args.putInt(PARAM_KEY, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPosition = getArguments().getInt(PARAM_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        switch (mPosition){
            case 0:
                return inflater.inflate(R.layout.fragment_welcome_guide, container, false);
            case 1:
                return inflater.inflate(R.layout.fragment_theme_choice, container, false);
            case 2:
                final View layout = inflater.inflate(R.layout.fragment_icons_amount_set, container, false);
                final Button finishSettingButton = layout.findViewById(R.id.finish_settings_button);
                finishSettingButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Intent intent = new Intent(getContext(), ScrollingLauncActivity.class);
                        startActivity(intent);
                    }
                });
                return layout;
            default:
                return inflater.inflate(R.layout.activity_app_info, container, false);

        }
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
