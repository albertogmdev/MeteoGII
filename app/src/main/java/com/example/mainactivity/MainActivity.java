package com.example.mainactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TableLayout;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class MainActivity extends AppCompatActivity {
    private TableLayout tableLayout;
    private String[] header = Singleton.getInstance().getHeaderMainActivity();
    private ArrayList<String[]> rows;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tableLayout = findViewById(R.id.tableestaciones);
        rows = new ArrayList<>();
        DynamicTable dynamicTable = new DynamicTable(MainActivity.this, tableLayout, getApplicationContext());
        dynamicTable.addHeader(header);
        dynamicTable.addData(getData());

    }

    private ArrayList<String[]> getData()
    {
        String respuesta = resultadoRefresh();
        
        rows.add(new String[]{"1", "233'23\"45'34\"", "28ºC", "Soleado", "20Pa"});
        rows.add(new String[]{"2", "23'23\"45'34\"", "34ºC", "Nublado", "20Pa"});
        rows.add(new String[]{"3", "33'23\"45'34\"", "40ºC", "Lluvioso", "20Pa"});
        rows.add(new String[]{"4", "263'23\"45'34\"", "50ºC", "Noche", "20Pa"});
        rows.add(new String[]{"5", "133'23\"45'34\"", "20ºC", "Soleado", "20Pa"});
        return rows;
    }

    public String resultadoRefresh() {

        FutureTask task = new FutureTask(new Cliente("Refresh"));

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
}
