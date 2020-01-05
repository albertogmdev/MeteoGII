package com.example.mainactivity.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.mainactivity.R;
import com.example.mainactivity.Singleton;

import java.util.ArrayList;
import java.util.List;

public class StationGraphicsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_graphics);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Singleton.getInstance().setEndConnectionThreadStationActivity(true);
        startActivity(new Intent(StationGraphicsActivity.this, StationInformationActivity.class));
        finish();
    }

    public void temperatureButton(View view) {
        Singleton.getInstance().setTypeGraph("TemperatureGraph");
        startActivity(new Intent(StationGraphicsActivity.this, GraphStationActivity.class));
        finish();
    }

    public void humidityButton(View view) {
        Singleton.getInstance().setTypeGraph("HumidityGraph");
        startActivity(new Intent(StationGraphicsActivity.this, GraphStationActivity.class));
        finish();
    }

    public void weatherButton(View view) {
        Singleton.getInstance().setTypeGraph("WeatherGraph");
        startActivity(new Intent(StationGraphicsActivity.this, WeatherGraphStationActivity.class));
        finish();
    }

    public void radiationButton(View view) {
        Singleton.getInstance().setTypeGraph("RadiationGraph");
        startActivity(new Intent(StationGraphicsActivity.this, GraphStationActivity.class));
        finish();
    }
}
