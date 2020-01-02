package com.example.mainactivity;

public class Singleton {
    private static Singleton instance;
    private String identificadorEstacion;

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
}
