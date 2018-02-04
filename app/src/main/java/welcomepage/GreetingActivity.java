package welcomepage;


import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.tseluikoartem.ening.yandexmobdevproject.R;

import io.fabric.sdk.android.Fabric;
import utils.ImageViewRounder;

public class GreetingActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView linkTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.greeting_activity);

        Bitmap book = BitmapFactory.decodeResource(getResources(),R.drawable.face);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(ImageViewRounder.getRoundedBitmap(book));

        linkTextView=findViewById(R.id.textViewGit);
        linkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent browser_intent=new Intent(Intent.ACTION_VIEW, Uri.parse(linkTextView.getText().toString()));
                startActivity(browser_intent);
            }
        });


        checkForUpdates();
    }

    @Override
    public void onResume() {
        super.onResume();
        // ... your own onResume implementation
        checkForCrashes();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterManagers();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterManagers();
    }

    private void checkForCrashes() {
        CrashManager.register(this);
    }

    private void checkForUpdates() {
        // Remove this for store builds!
        UpdateManager.register(this);
    }

    private void unregisterManagers() {
        UpdateManager.unregister();
    }

}
