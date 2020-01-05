package com.example.mainactivity.Threads;

import com.example.mainactivity.Activities.StationInformationActivity;
import com.example.mainactivity.Monitor;
import com.example.mainactivity.Singleton;

public class ThreadStationInformationActivity extends Thread{
    private String typeMessage;
    private String idEstacion;
    private String message;
    private TimerStationInformationActivity timer;
    private Monitor monitor;
    private StationInformationActivity stationInformationActivity;



    public ThreadStationInformationActivity(String idEstacion, String typeMessage, Monitor monitor, StationInformationActivity stationInformationActivity)
    {
        this.idEstacion = idEstacion;
        this.typeMessage = typeMessage;
        this.monitor = monitor;
        this.message = "";
        this.timer = new TimerStationInformationActivity(monitor);
        this.stationInformationActivity = stationInformationActivity;

    }

    @Override
    public void run()
    {
        timer.start();
        while(!Singleton.getInstance().isEndConnectionThreadStationActivity())
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
