package com.tseluikoartem.ening.yandexmobdevproject;


import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView linkTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        Bitmap book = BitmapFactory.decodeResource(getResources(),R.drawable.face);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(getRoundedBitmap(book));

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

    public Bitmap getRoundedBitmap(Bitmap bitmap){
        Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(shader);
        paint.setAntiAlias(true);
        Canvas c = new Canvas(circleBitmap);
        c.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
        return circleBitmap;
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
