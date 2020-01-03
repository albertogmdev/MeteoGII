package com.example.mainactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class MainActivity extends AppCompatActivity {
    private TableLayout tableLayout;
    private String[] header = Singleton.getInstance().getHeaderMainActivity();
    private ArrayList<String[]> rows;
    private HiloMainActivity hiloRefresh;
    private HiloMainActivity hiloNotify;
    private Monitor monitor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Singleton.getInstance().setEndConnectionThread(false);
        tableLayout = findViewById(R.id.tableestaciones);
        rows = new ArrayList<>();
        this.monitor = new Monitor();
        DynamicTable dynamicTable = new DynamicTable(MainActivity.this, tableLayout, getApplicationContext(), monitor);
        dynamicTable.addHeader(header);
        dynamicTable.addData(getData());
        this.hiloRefresh = new HiloMainActivity(String.valueOf(getNumberStations()), "RefreshTable", monitor,dynamicTable, MainActivity.this);
        this.hiloNotify = new HiloMainActivity(String.valueOf(getNumberStations()), "NotifyAll", monitor, dynamicTable, MainActivity.this);
        this.hiloRefresh.start();
        //this.hiloNotify.start();
    }

    public  ArrayList<String[]> getData()
    {
        Singleton.getInstance().setCounterStations(getNumberStations());
        String respuesta = resultadoRefresh();
        //Log.e("HEY", respuesta);
        String[] listaFilas = respuesta.split("\n");
        for(int i = 0; i < listaFilas.length; i++)
        {
            rows.add (listaFilas[i].split("//"));
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

        FutureTask task = new FutureTask(new Cliente(String.valueOf(Singleton.getInstance().getCounterStations()), "RefreshTable"));

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

    public int getNumberStations() {

        FutureTask task = new FutureTask(new Cliente("Stations"));

        ExecutorService es = Executors.newSingleThreadExecutor();
        es.submit(task);

        String result = "NULL";

        try {

            result = task.get().toString();

        } catch (Exception e) {

            System.err.println(e);
        }

        es.shutdown();
        return Integer.parseInt(result);
    }


}
