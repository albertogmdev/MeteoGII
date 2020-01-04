package com.example.mainactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class graficasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.graficas_information);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Singleton.getInstance().setEndConnectionThread(true);
        startActivity(new Intent(graficasActivity.this, StationInformationActivity.class));
        finish();
    }
}
