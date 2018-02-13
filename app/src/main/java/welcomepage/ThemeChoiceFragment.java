package welcomepage;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.tseluikoartem.ening.yandexmobdevproject.R;


import static utils.ApplicationConstants.SharedPreferenciesConstants.*;


public class ThemeChoiceFragment extends Fragment {


    private OnThemeChangeListener mListener;
    private RadioButton mLightThemeButton, mDarkThemeButton;

    public ThemeChoiceFragment() {
        // Required empty public constructor
    }


    public static ThemeChoiceFragment newInstance() {
        ThemeChoiceFragment fragment = new ThemeChoiceFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View layout = inflater.inflate(R.layout.fragment_theme_choice, container, false);
        mLightThemeButton = layout.findViewById(R.id.radioButtonLightTheme);
        mDarkThemeButton = layout.findViewById(R.id.radioButtonDarkTheme);

        mDarkThemeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onThemeChange(THEME_DARK);
            }
        });
        mLightThemeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onThemeChange(THEME_LIGHT);
            }
        });

        final String themeType = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(THEME_CHOICE_KEY,THEME_LIGHT);
        if(themeType.equals(THEME_LIGHT)){
            mLightThemeButton.setChecked(true);
        }else {
            mDarkThemeButton.setChecked(true);
        }

        return layout;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnThemeChangeListener) {
            mListener = (OnThemeChangeListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnThemeChangeListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    interface OnThemeChangeListener{
        void onThemeChange(String themeType);

    }


}
