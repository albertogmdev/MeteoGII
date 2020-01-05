package com.example.mainactivity.Threads;

import com.example.mainactivity.Activities.MainActivity;
import com.example.mainactivity.Cliente;
import com.example.mainactivity.DynamicTable;
import com.example.mainactivity.Monitor;
import com.example.mainactivity.Singleton;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class ThreadMainActivity extends Thread{
    private String typeMessage;
    private String idEstacion;
    private String message;
    private DynamicTable dynamicTable;
    private TimerMainActivity timerMainActivity;
    private Monitor monitor;
    private MainActivity mainActivity;
    //private TextView notificaciones;

    public ThreadMainActivity(String idEstacion, String typeMessage, Monitor monitor, DynamicTable dynamicTable, MainActivity mainActivity)
    {
        this.idEstacion = idEstacion;
        this.typeMessage = typeMessage;
        this.mainActivity = mainActivity;
        this.dynamicTable = dynamicTable;
        this.monitor = monitor;
        this.timerMainActivity = new TimerMainActivity(monitor);
        this.message = "";
    }

    public ThreadMainActivity(String idEstacion, String typeMessage, Monitor monitor, MainActivity mainActivity)
    {
        this.idEstacion = idEstacion;
        this.typeMessage = typeMessage;
        this.mainActivity = mainActivity;
        this.monitor = monitor;
        this.timerMainActivity = new TimerMainActivity(monitor);
        this.message = "";
        //this.notificaciones = notificaciones;
    }

    @Override
    public void run()
    {
        timerMainActivity.start();
        while(!Singleton.getInstance().isEndConnectionThreadMainActivity())
        {
            monitor.setStopThread(true);
            monitor.makeThreadWait();
            this.idEstacion = getNumberStations();
            if(typeMessage.equals("RefreshTable"))
            {
                dynamicTable.resetTableMainActivity();
                monitor.makeThreadWaitResetTable();
                monitor.setStopThreadResetTable(true);
                dynamicTable.addHeaderMainActivity();
                monitor.makeThreadWaitHeader();
                monitor.setStopThreadHeader(true);
                dynamicTable.addDataMainActivity();
                monitor.makeThreadWaitData();
                monitor.setStopThreadData(true);

            }
            else
            {
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mainActivity.notificarAll();
                    }
                });
            }

        }
    }

    public String getNumberStations() {

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
        return result;
    }

}
