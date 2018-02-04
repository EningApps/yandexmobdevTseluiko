package welcomepage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.tseluikoartem.ening.yandexmobdevproject.R;

import utils.ImageViewRounder;

public class WelcomeSettingsActivity extends AppCompatActivity implements WelcomeSettingsFragment.OnFragmentInteractionListener{


    private ViewPager mViewPager;
    private FragmentPagerAdapter mFragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_settings);


        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager, true);
        mFragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return WelcomeSettingsFragment.newInstance(position);
            }

            @Override
            public int getCount() {
                return 3;
            }
        };
        mViewPager.setAdapter(mFragmentPagerAdapter);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}