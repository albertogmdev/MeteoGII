package com.example.mainactivity.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.mainactivity.Cliente;
import com.example.mainactivity.DynamicTable;
import com.example.mainactivity.Threads.ThreadMainActivity;
import com.example.mainactivity.Monitor;
import com.example.mainactivity.R;
import com.example.mainactivity.Singleton;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class MainActivity extends AppCompatActivity {
    private TableLayout tableLayout;
    private String[] header = Singleton.getInstance().getHeaderMainActivity();
    private ArrayList<String[]> rows;
    private ThreadMainActivity hiloRefresh;
    private ThreadMainActivity hiloNotify;
    private Monitor monitor;

    private TextView notificaciones;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Singleton.getInstance().setEndConnectionThreadMainActivity(false);
        tableLayout = findViewById(R.id.tableestaciones);
        rows = new ArrayList<>();
        this.monitor = new Monitor();
        DynamicTable dynamicTable = new DynamicTable(MainActivity.this, tableLayout, getApplicationContext(), monitor);
        dynamicTable.addHeader(header);
        dynamicTable.addData(getData());
        this.hiloRefresh = new ThreadMainActivity(String.valueOf(getNumberStations()), "RefreshTable", monitor,dynamicTable, MainActivity.this);
        notificaciones = findViewById(R.id.notifyAll);
        notificarAll();
        this.hiloNotify = new ThreadMainActivity(String.valueOf(getNumberStations()), "NotifyAll", monitor, MainActivity.this);
        this.hiloRefresh.start();
        this.hiloNotify.start();
    }

    public void notificarAll(){

        String alerta = resultadoNotifyAll();

        if(!alerta.isEmpty()){
            notificaciones.setText(alerta);
        }else{
            notificaciones.setText("No hay notifiacaciones");
        }
    }

    public String resultadoNotifyAll(){

        FutureTask task = new FutureTask(new Cliente("NotifyAll"));

        ExecutorService es = Executors.newSingleThreadExecutor();
        es.submit(task);

        String result = "";

        try {

            result = task.get().toString();
        } catch (Exception e) {

            System.err.println(e);
        }

        es.shutdown();

        return result;
    }

    public  ArrayList<String[]> getData()
    {
        Singleton.getInstance().setCounterStations(getNumberStations());
        String respuesta = resultadoRefresh();

        String[] listaFilas = respuesta.split("\n");

        for(int i = 0; i < listaFilas.length; i++)
        {
            String[] columnas = listaFilas[i].split("//");
            //Array con las unidades
            String nuevo[] = new String[5];

            for(int j=0; j<columnas.length; j++){
                switch (j){
                    //id y ubicacion
                    case 0:
                    case 1:
                        if(columnas[j].equals("NULL")){
                            nuevo[j] = "0";
                        }else{
                            nuevo[j] = columnas[j];
                        }
                        break;
                    //temperatura
                    case 2:
                        if(columnas[j].equals("NULL")){
                            nuevo[j] = "0ºC";
                        }else{
                            nuevo[j] = columnas[j]+"ºC";
                        }
                        break;
                    //humedad
                    case 3:
                        if(columnas[j].equals("NULL")){
                            nuevo[j] = "0%";
                        }else{
                            nuevo[j] = columnas[j]+"%";
                        }
                        break;
                    //presion
                    case 4:
                        if(columnas[j].equals("NULL")){
                            nuevo[j] = "0Pa";
                        }else{
                            nuevo[j] = columnas[j]+"Pa";
                        }
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
