package welcomepage;

import android.content.Intent;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.tseluikoartem.ening.yandexmobdevproject.R;
import com.tseluikoartem.ening.yandexmobdevproject.activities.LauncherApplication;

import backgroundimage.BackgroundImageAsyncChanger;
import launcher.MainLauncherActivity;

import utils.ImageViewRounder;

import static utils.ApplicationConstants.SharedPreferenciesConstants.SHOW_WELCOMEPAGE_KEY;


public class AppWelcomeInfoActivity extends AppCompatActivity {

    private ImageView mImageViewAppIcon;
    private Button mGoToSettingsButton , mGoToAppButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);


        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.logo);
        mImageViewAppIcon =(ImageView)findViewById(R.id.imageViewAppIcon);
        mImageViewAppIcon.setImageBitmap(ImageViewRounder.getRoundedBitmap(bitmap));

        mGoToAppButton = findViewById(R.id.buttonToApp);
        mGoToSettingsButton = findViewById(R.id.buttonToSettings);
        mGoToSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(getApplicationContext(),WelcomeSettingsActivity.class);
                startActivity(intent);
            }
        });
        mGoToAppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                sp.edit().putBoolean(SHOW_WELCOMEPAGE_KEY, false).apply();
                final Intent intent = new Intent(getApplicationContext(),MainLauncherActivity.class);
                startActivity(intent);
            }
        });


    }


    @Override
    protected void onResume() {
        final View rootView = findViewById(R.id.root_app_info_layout);
        final String[] imagesFileNames = LauncherApplication.getInstance().getImagesFileNames();
        if(imagesFileNames!=null){
            new BackgroundImageAsyncChanger(rootView,this,0).execute(imagesFileNames);
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


}
