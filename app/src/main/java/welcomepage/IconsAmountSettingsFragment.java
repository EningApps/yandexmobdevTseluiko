package welcomepage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import com.tseluikoartem.ening.yandexmobdevproject.R;
import launcher.MainLauncherActivity;

import static utils.ApplicationConstants.SharedPreferenciesConstants.MAKET_TYPE_KEY;
import static utils.ApplicationConstants.SharedPreferenciesConstants.SHOW_WELCOMEPAGE_KEY;


public class IconsAmountSettingsFragment extends Fragment {


    private OnMaketTypeChangeListener mListener;
    private Button mFinishButton;
    private View mStandartMaketField;
    private View mLargeMaketField;
    private RadioButton mLargeMaketButton;
    private RadioButton mStandartMaketButton;


    public IconsAmountSettingsFragment() {
        // Required empty public constructor
    }


    public static IconsAmountSettingsFragment newInstance() {
        IconsAmountSettingsFragment fragment = new IconsAmountSettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View layout = inflater.inflate(R.layout.fragment_icons_amount_set, container, false);
        mStandartMaketField = layout.findViewById(R.id.standartMaketField);
        mLargeMaketField = layout.findViewById(R.id.largeMaketField);

        mStandartMaketButton = layout.findViewById(R.id.radioButtonStandartMaket);
        mLargeMaketButton = layout.findViewById(R.id.radioButtonLargeMaket);

        mFinishButton = layout.findViewById(R.id.finish_settings_button);

        final int spanCount = PreferenceManager.getDefaultSharedPreferences(getContext()).getInt(MAKET_TYPE_KEY,4);
        if(spanCount==4){
            mStandartMaketButton.setChecked(true);
        }else {
            mLargeMaketButton.setChecked(true);
        }

        setViewsOnClickListeners();

        return layout;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMaketTypeChangeListener) {
            mListener = (OnMaketTypeChangeListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMaketTypeChangeListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void setViewsOnClickListeners(){

        mFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                sp.edit().putBoolean(SHOW_WELCOMEPAGE_KEY, false).apply();
                final Intent intent = new Intent(getContext(), MainLauncherActivity.class);
                startActivity(intent);
            }
        });

        mStandartMaketField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStandartMaketButton.setChecked(true);
                mLargeMaketButton.setChecked(false);
                mListener.onMaketTypeChange(4);
            }
        });
        mLargeMaketField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLargeMaketButton.setChecked(true);
                mStandartMaketButton.setChecked(false);
                mListener.onMaketTypeChange(5);
            }
        });

        mStandartMaketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStandartMaketButton.setChecked(true);
                mLargeMaketButton.setChecked(false);
                mListener.onMaketTypeChange(4);
            }
        });

        mLargeMaketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLargeMaketButton.setChecked(true);
                mStandartMaketButton.setChecked(false);
                mListener.onMaketTypeChange(5);
            }
        });

    }

    interface OnMaketTypeChangeListener {
        void onMaketTypeChange(int spanCount);
    }

}
