package com.tseluikoartem.ening.yandexmobdevproject.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tseluikoartem.ening.yandexmobdevproject.R;

public class ThemeChoiceActivity extends AppCompatActivity {

    Button buttonContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_choice);

        buttonContinue= (Button) findViewById(R.id.buttonContTheme);
        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent=new Intent(getApplicationContext(),IconsAmountSetActivity.class);
                startActivity(intent);
            }
        });
    }
}
