package com.example.mainactivity.Threads;

import com.example.mainactivity.Activities.StationInformationActivity;
import com.example.mainactivity.Monitor;
import com.example.mainactivity.Singleton;
import com.example.mainactivity.Timer;

public class ThreadStationInformationActivity extends Thread{
    private String typeMessage;
    private String idEstacion;
    private String message;
    private Timer timer;
    private Monitor monitor;
    private StationInformationActivity stationInformationActivity;



    public ThreadStationInformationActivity(String idEstacion, String typeMessage, Monitor monitor, StationInformationActivity stationInformationActivity)
    {
        this.idEstacion = idEstacion;
        this.typeMessage = typeMessage;
        this.monitor = monitor;
        this.message = "";
        this.timer = new Timer(monitor);
        this.stationInformationActivity = stationInformationActivity;

    }

    @Override
    public void run()
    {
        timer.start();
        while(!Singleton.getInstance().isEndConnectionThread())
        {
            monitor.setStopThread(true);
            monitor.makeThreadWait();
            this.idEstacion = Singleton.getInstance().getIdentificadorEstacion();
            if(typeMessage.equals("Refresh"))
            {
                stationInformationActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stationInformationActivity.refresh(stationInformationActivity.resultadoRefresh());
                    }
                });
            }
            else //Notify from a station
            {
                stationInformationActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stationInformationActivity.notifyStation();
                    }
                });
            }

        }
    }
}
