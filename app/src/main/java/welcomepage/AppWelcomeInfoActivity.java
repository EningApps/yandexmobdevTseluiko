package welcomepage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.tseluikoartem.ening.yandexmobdevproject.R;
import com.tseluikoartem.ening.yandexmobdevproject.activities.ScrollingLauncActivity;

import utils.ImageViewRounder;

public class AppWelcomeInfoActivity extends AppCompatActivity{

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
                final Intent intent = new Intent(getApplicationContext(),ScrollingLauncActivity.class);
                startActivity(intent);
            }
        });

    }
}
