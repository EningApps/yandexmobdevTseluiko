package com.tseluikoartem.ening.yandexmobdevproject.activities;


import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;


import com.tseluikoartem.ening.yandexmobdevproject.R;

import java.util.List;

public class SettingActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return true;
    }

    public static class ProfilePrefsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

//            PreferenceManager.setDefaultValues(getActivity(),
//                    R.xml.advanced_preferences, false);
            addPreferencesFromResource(R.xml.pref_profile);
        }
    }


    /**
     * This fragment shows the preferences for the second header.
     */
    public static class GeneralPrefsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.pref_general);
        }
    }
}
