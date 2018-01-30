package com.tseluikoartem.ening.yandexmobdevproject.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.tseluikoartem.ening.yandexmobdevproject.R;

import utils.ImageViewRounder;

public class AppInfoActivity extends AppCompatActivity {

    private ImageView imageViewAppIcon;
    private Button buttonContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.logo);
        imageViewAppIcon=(ImageView)findViewById(R.id.imageViewAppIcon);
        imageViewAppIcon.setImageBitmap(ImageViewRounder.getRoundedBitmap(bitmap));

        buttonContinue= (Button) findViewById(R.id.buttonContInfo);
        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent=new Intent(getApplicationContext(),ThemeChoiceActivity.class);
                startActivity(intent);
            }
        });
    }
}
