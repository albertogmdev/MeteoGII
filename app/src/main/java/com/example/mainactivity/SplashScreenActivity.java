package com.example.mainactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.concurrent.*;

public class SplashScreenActivity extends AppCompatActivity {

    private TextView texto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        texto = findViewById(R.id.textView);

    }


    public void startButton(View v)
    {
        //Log.e("HEY", "Problem2");
        //startActivity(new Intent(SplashScreenActivity.this, StationInformationActivity.class));
        startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
        super.onBackPressed();
        finish();

        //setContentView(R.layout.activity_station_information2);
    }



}