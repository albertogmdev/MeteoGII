package com.example.mainactivity;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Singleton {
    private static Singleton instance;
    private String identificadorEstacion;
    private String[] headerMainActivity = {"Identificador", "Ubicación", "Temperatura", "Tiempo", "Presión Atmosférica"};
    private ArrayList<String> stations = new ArrayList<>();

    public static synchronized Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    private Singleton()
    {
        this.identificadorEstacion = "";
    }

    public String getIdentificadorEstacion() {
        return identificadorEstacion;
    }

    public void setIdentificadorEstacion(String identificadorEstacion) {
        this.identificadorEstacion = identificadorEstacion;
    }

    public String[] getHeaderMainActivity() {
        return headerMainActivity;
    }

    public void setHeaderMainActivity(String[] headerMainActivity) {
        this.headerMainActivity = headerMainActivity;
    }

    public String[] getStations()
    {
        return (String[]) this.stations.toArray();
    }
    public void setStations(String[] stations)
    {
        for(int i = 0; i < stations.length; i++)
        {
            this.stations.add(stations[i]);
        }
    }

    public void addStation(String identificadorEstacion)
    {
        this.stations.add(identificadorEstacion);
    }


}
