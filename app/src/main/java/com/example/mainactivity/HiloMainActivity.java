package com.example.mainactivity;

import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class HiloMainActivity extends Thread{
    private String typeMessage;
    private String idEstacion;
    private String message;
    private DynamicTable dynamicTable;
    private Timer timer;
    private Monitor monitor;
    private MainActivity mainActivity;

    public HiloMainActivity(String idEstacion, String typeMessage, Monitor monitor, DynamicTable dynamicTable, MainActivity mainActivity)
    {
        this.idEstacion = idEstacion;
        this.typeMessage = typeMessage;
        this.mainActivity = mainActivity;
        this.dynamicTable = dynamicTable;
        this.monitor = monitor;
        this.timer = new Timer(monitor);
        this.message = "";
    }

    @Override
    public void run()
    {
        timer.start();
        while(!Singleton.getInstance().isEndConnectionThread())
        {
            monitor.makeThreadWait();
            monitor.setStopThread(true);
            this.idEstacion = getNumberStations();
            if(typeMessage.equals("RefreshTable"))
            {
                dynamicTable.resetTable();
                monitor.makeThreadWaitResetTable();
                monitor.setStopThreadResetTable(true);
                dynamicTable.addHeader();
                monitor.makeThreadWaitHeader();
                monitor.setStopThreadHeader(true);
                dynamicTable.addData();
                monitor.makeThreadWaitData();
                monitor.setStopThreadData(true);

            }
            else    //Notify-All, TODO
            {
                message = executeCommand();
            }

        }
    }

    public String executeCommand()
    {
        FutureTask task = new FutureTask(new Cliente(this.idEstacion, this.typeMessage));

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
