package com.example.mainactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TableLayout;

import java.util.ArrayList;

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
        rows.add(new String[]{"1", "233'23\"45'34\"", "28ºC", "Soleado", "20Pa"});
        rows.add(new String[]{"2", "23'23\"45'34\"", "34ºC", "Nublado", "20Pa"});
        rows.add(new String[]{"3", "33'23\"45'34\"", "40ºC", "Lluvioso", "20Pa"});
        rows.add(new String[]{"4", "263'23\"45'34\"", "50ºC", "Noche", "20Pa"});
        rows.add(new String[]{"5", "133'23\"45'34\"", "20ºC", "Soleado", "20Pa"});
        return rows;
    }
}
