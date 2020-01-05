package com.example.mainactivity.Threads;

import com.example.mainactivity.Activities.GraphStationActivity;
import com.example.mainactivity.Activities.WeatherGraphStationActivity;
import com.example.mainactivity.DynamicTable;
import com.example.mainactivity.Monitor;
import com.example.mainactivity.Singleton;

public class ThreadGraph extends Thread{
    private String idEstacion;
    private String typeGraph;
    private String message;
    private DynamicTable dynamicTable;
    private TimerGraph timerGraph;
    private Monitor monitor;
    private GraphStationActivity graphStationActivity;

    public ThreadGraph(String idEstacion, String typeGraph, Monitor monitor, DynamicTable dynamicTable)
    {
        this.idEstacion = idEstacion;
        this.typeGraph = typeGraph;
        this.dynamicTable = dynamicTable;
        this.monitor = monitor;
        this.timerGraph = new TimerGraph(monitor);
        this.message = "";
    }

    public ThreadGraph(String idEstacion, String typeGraph, Monitor monitor, GraphStationActivity graphStationActivity)
    {
        this.idEstacion = idEstacion;
        this.typeGraph = typeGraph;
        this.monitor = monitor;
        this.timerGraph = new TimerGraph(monitor);
        this.message = "";
        this.graphStationActivity = graphStationActivity;
    }

    @Override
    public void run()
    {
        timerGraph.start();
        while(!Singleton.getInstance().isEndConnectionThreadGraphActivity())
        {
            monitor.setStopThread(true);
            monitor.makeThreadWait();
            this.idEstacion = Singleton.getInstance().getIdentificadorEstacion();
            if(typeGraph.equals("WeatherGraph"))
            {
                dynamicTable.resetTableWeatherGraphStationActivity();
                monitor.makeThreadWaitResetTable();
                monitor.setStopThreadResetTable(true);
                dynamicTable.addHeaderWeatherGraphStationActivity();
                monitor.makeThreadWaitHeader();
                monitor.setStopThreadHeader(true);
                dynamicTable.addDataWeatherGraphStationActivity();
                monitor.makeThreadWaitData();
                monitor.setStopThreadData(true);
            }
            else
            {
                this.graphStationActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        graphStationActivity.generateLineChart();
                    }
                });
            }
        }
    }


}
