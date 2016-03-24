package com.zhm.dictionary.friendly;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.zhm.dictionary.R;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("Zhm", "SplashActivity oncreate");
        setContentView(R.layout.layout_splash);

        // TODO db check

        startActivity(new Intent(this, MainActivity.class));

        // AppDatabaseFactory.Init(this);
        finish();
    }
}
