package com.example.mainactivity.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.mainactivity.R;
import com.example.mainactivity.Singleton;

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

    //*************************************************************
    //CODIGO DE LAS GRAFICAS ver Pagina web: https://platzi.com/tutoriales/1049-android/2662-como-crear-graficos-en-android/
    //*************************************************************
    /*private LineChart lineChart;
    private LineDataSet lineDataSet;

    // Enlazamos al XML
    lineChart = view.findViewById(R.id.lineChart);

    // Creamos un set de datos
    ArrayList<Entry> lineEntries = new ArrayList<Entry>();
     for (int i = 0; i<11; i++){
                float y = (int) (Math.random() * 8) + 1;
                lineEntries.add(new Entry((float) i,(float)y));
            }

    // Unimos los datos al data set
    lineDataSet = new LineDataSet(lineEntries, "Platzi");

    // Asociamos al gráfico
    LineData lineData = new LineData();
    lineData.addDataSet(lineDataSet);
    lineChart.setData(lineData);*/

}
