package com.tseluikoartem.ening.yandexmobdevproject.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tseluikoartem.ening.yandexmobdevproject.R;

public class IconsAmountSetActivity extends AppCompatActivity {

    private Button buttonContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icons_amount_set);

        buttonContinue= (Button) findViewById(R.id.buttonContIconSet);
        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent=new Intent(getApplicationContext(),ScrollingLauncActivity.class);
                startActivity(intent);
            }
        });
    }
}
