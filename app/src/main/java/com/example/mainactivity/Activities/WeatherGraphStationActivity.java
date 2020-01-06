package com.example.mainactivity.Activities;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mainactivity.Cliente;
import com.example.mainactivity.DynamicTable;
import com.example.mainactivity.Monitor;
import com.example.mainactivity.R;
import com.example.mainactivity.Singleton;
import com.example.mainactivity.Threads.ThreadGraph;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class WeatherGraphStationActivity extends AppCompatActivity {
    private TableLayout tableLayout;
    private String[] header = Singleton.getInstance().getHeaderWeatherGraphStationActivity();
    private ArrayList<String[]> rows;
    private ThreadGraph hiloRefresh;
    private Monitor monitor;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_graph_station);
        this.tableLayout = findViewById(R.id.tablaTiempo);
        Singleton.getInstance().setEndConnectionThreadGraphActivity(false);
        this.textView = findViewById(R.id.titulo);
        textView.setText("Gráfica Tiempo Estación " + Singleton.getInstance().getIdentificadorEstacion());
        this.rows = new ArrayList<>();
        this.monitor = new Monitor();
        DynamicTable dynamicTable = new DynamicTable(WeatherGraphStationActivity.this, tableLayout, getApplicationContext(), monitor);
        dynamicTable.addHeaderMainActivity(header);
        dynamicTable.addDataMainActivity(getData());
        this.hiloRefresh = new ThreadGraph(String.valueOf(Singleton.getInstance().getIdentificadorEstacion()), Singleton.getInstance().getTypeGraph(), monitor,dynamicTable);

        this.hiloRefresh.start();
    }

    public  ArrayList<String[]> getData()
    {
        String respuesta = resultadoRefresh();

        String[] listaFilas = respuesta.split("\n");

        for(int i = 0; i < listaFilas.length; i++)
        {
            String[] columnas = listaFilas[i].split("//");
            //Array con las unidades
            String nuevo[] = new String[5];

            for(int j=0; j<columnas.length; j++){
                switch (j){
                    //Fecha
                    case 0:
                        nuevo[j] = columnas[j];
                        break;
                    case 1:
                        nuevo[j] = columnas[j];
                        break;
                    default:
                        break;
                }
            }

            rows.add(nuevo);
        }

        return rows;
    }

    public ArrayList<String[]> getRows() {
        return rows;
    }

    public void setRows(ArrayList<String[]> rows) {
        this.rows = rows;
    }

    private String resultadoRefresh() {

        FutureTask task = new FutureTask(new Cliente(String.valueOf(Singleton.getInstance().getIdentificadorEstacion()), "Tiempo","Graph"));

        ExecutorService es = Executors.newSingleThreadExecutor();
        es.submit(task);

        String result = "NULL";

        try {

            result = task.get().toString();
        } catch (Exception e) {

            System.err.println(e);
        }

        es.shutdown();

        return result;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Singleton.getInstance().setEndConnectionThreadGraphActivity(true);
        startActivity(new Intent(WeatherGraphStationActivity.this, GraphStationActivity.class));
        finish();
    }
}


