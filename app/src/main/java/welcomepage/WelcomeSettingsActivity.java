package welcomepage;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.tseluikoartem.ening.yandexmobdevproject.R;

import backgroundimage.ImagesLoadedReciver;

import static utils.ApplicationConstants.SharedPreferenciesConstants.*;

public class WelcomeSettingsActivity extends AppCompatActivity
                                    implements IconsAmountSettingsFragment.OnMaketTypeChangeListener,
                                               ThemeChoiceFragment.OnThemeChangeListener{



    private ViewPager mViewPager;
    private FragmentPagerAdapter mFragmentPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences themePrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String themeType = themePrefs.getString(THEME_CHOICE_KEY, THEME_LIGHT);
        if(themeType==THEME_LIGHT) {
            setTheme(R.style.AppLightTheme);
        }
        else if(themeType==THEME_DARK) {
            setTheme(R.style.AppDarkTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_settings);

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager, true);
        mFragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position){
                    case 0:
                        return WelcomeGuideFragment.newInstance();
                    case 1:
                        return ThemeChoiceFragment.newInstance();
                    case 2:
                        return IconsAmountSettingsFragment.newInstance();
                    default:
                        return WelcomeGuideFragment.newInstance();
                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        };
        mViewPager.setAdapter(mFragmentPagerAdapter);

    }

    @Override
    protected void onResume() {
        final View rootView = findViewById(R.id.root_welcome_settings_layout);
        final ImagesLoadedReciver imagesLoadedReciver = ImagesLoadedReciver.getInstance();
        imagesLoadedReciver.registerBackground(rootView);
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onMaketTypeChange(int spanCount) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.edit().putString(MAKET_TYPE_KEY, String.valueOf(spanCount)).apply();

    }

    @Override
    public void onThemeChange(String themeType) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.edit().putString(THEME_CHOICE_KEY,themeType).apply();
        recreate();
    }


}