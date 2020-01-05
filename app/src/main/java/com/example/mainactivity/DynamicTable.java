package com.example.mainactivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.mainactivity.Activities.MainActivity;
import com.example.mainactivity.Activities.StationInformationActivity;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class DynamicTable {
    private MainActivity mainActivity;
    private TableLayout tableLayout;
    private Context context;
    private String[] header;
    private ArrayList<String[]> data;
    private TableRow tableRow;
    private TextView textCell;
    private Monitor monitor;
    //private ThreadMainActivity hiloRefresh;

    private int indexCell;
    private int indexRow;

    public DynamicTable(MainActivity mainActivity, TableLayout tableLayout, Context context, Monitor monitor)
    {
        this.mainActivity = mainActivity;
        this.tableLayout = tableLayout;
        this.context = context;
        this.monitor = monitor;
        //this.hiloRefresh = new ThreadMainActivity(String.valueOf(mainActivity.getNumberStations()), "RefreshTable", monitor,DynamicTable.this, mainActivity);
        //this.hiloRefresh.start();

        //Singleton.getInstance().resetCounterStations();
    }

    /**Incluir la cabecera de la tabla dinámica*/
    public void addHeader(String[] header)
    {
        this.header = header;
        createHeader();
        //Singleton.getInstance().resetCounterStations();
    }

    /**Incluir los datos de la tabla dinámica*/
    public void addData(ArrayList<String[]> data)
    {
        this.data = data;
        //Singleton.getInstance().resetCounterStations();
        createDataTable();
    }

    /**Crear una nueva fila de la tabla dinámica*/
    public void newRow()
    {

        tableRow = new TableRow(context);
        //Singleton.getInstance().addOneStation();
    }

    /**Crear una nueva celda de la tabla dinámica*/
    public void newCell()
    {
        textCell = new TextView(context);
        textCell.setGravity(Gravity.CENTER);
        textCell.setTextSize(20);
        textCell.setTextColor(Color.parseColor("#F0EBEB"));
    }

    /**Crear la cabecera de la tabla dinámica*/
    public void createHeader()
    {
        indexCell = 0;
        newRow();
        while (indexCell < header.length)
        {
            newCell();
            textCell.setText(header[indexCell++]);
            tableRow.addView(textCell, newTableRowParams());
        }
        tableLayout.addView(tableRow);
    }

    /**Crear los datos de la tabla de datos de la tabla dinámica*/
    private void createDataTable()
    {
        String info;
        for(indexRow = 0; indexRow < data.size(); indexRow++ )
        {
            newRow();
            for(indexCell = 0; indexCell < header.length; indexCell++)
            {
                newCell();
                String[] rows = data.get(indexRow);
                if(indexCell == 0)
                {
                    info = rows[indexCell];
                    Button buttonStation = new Button(context);
                    buttonStation.setText(info);
                    Singleton.getInstance().addStation(info);
                    //Asignamose el Listener
                    final String finalInfo = info;
                    buttonStation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(context, " Listener botón " + v.getTag(), Toast.LENGTH_SHORT).show();
                            Singleton.getInstance().setIdentificadorEstacion(finalInfo);
                            Singleton.getInstance().setEndConnectionThreadMainActivity(true);
                            mainActivity.startActivity(new Intent(mainActivity, StationInformationActivity.class));
                            mainActivity.finish();
                        }
                    });
                    buttonStation.setBackgroundColor(Color.parseColor("#333232"));
                    buttonStation.setTextColor(Color.parseColor("#F0EBEB"));
                    buttonStation.setTextSize(20);
                    tableRow.addView(buttonStation, newTableRowParams());

                }
                else
                {
                    if(indexCell < rows.length)
                    {
                        info = rows[indexCell];
                    }
                    else
                    {
                        info = "";
                    }
                    textCell.setText(info);
                    tableRow.addView(textCell, newTableRowParams());
                }

            }
            tableLayout.addView(tableRow);
        }
    }

    /**Marcar los márgenes de las celdas de la tabla dinámica*/
    private TableRow.LayoutParams newTableRowParams()
    {
        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.setMargins(5,1,5,1);

        params.weight = 1;
        return params;
    }

    public void resetTable()
    {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tableRow.removeAllViews();
                tableLayout.removeAllViewsInLayout();
                monitor.setStopThreadResetTable(false);
                monitor.activeThread();
            }
        });
    }

    public void addHeader()
    {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                addHeader(Singleton.getInstance().getHeaderMainActivity());
                monitor.setStopThreadHeader(false);
                monitor.activeThread();
            }
        });
    }

    public void addData()
    {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                data = new ArrayList<>();
                mainActivity.setRows(new ArrayList<String[]>());
                Singleton.getInstance().resetStations();
                addData(mainActivity.getData());
                monitor.setStopThreadData(false);
                monitor.activeThread();
            }
        });
    }
}
