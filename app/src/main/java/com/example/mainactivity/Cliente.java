package com.example.mainactivity;

import android.util.Log;

import java.io.*;
import java.net.*;
import java.util.concurrent.Callable;


public class Cliente implements Callable<String> {

    Socket cliente;

    DataInputStream entrada;
    DataOutputStream salida;

    String mensaje, respuesta, typeMessage;

    private String idConexion;

    public Cliente(String id, String typeMesage){
        this.idConexion = id;
        this.typeMessage = typeMesage;
        this.respuesta = "NULL";
    }

    public Cliente(String typeMesage){
        this.idConexion = "";
        this.typeMessage = typeMesage;
        this.respuesta = "NULL";
    }

    @Override
    public String call(){

        try{
            Log.e("HEY","CONNECTION");
            cliente = new Socket("weatherubicuastation.duckdns.org", 8080);
            Log.e("HEY","CONNECTION2");

            entrada = new DataInputStream(cliente.getInputStream());
            salida = new DataOutputStream(cliente.getOutputStream());

            if(typeMessage.equals("Stations")) {
                mensaje = "Stations";
            }

            //Refresh-idEstacion se especifica el id de la estacion que se quiere consultar y devuelve TODOS los datos
            //idConexion tiene que ser idEstacion
            else if(typeMessage.equals("Refresh")){

                mensaje = "Refresh-"+idConexion;
            }
            //Refresh-numeroEstaciones se especifica cuantas estaciones tenemos siendo el id de las estaciones de 1 a n, es decir
            //si hay 5 estaciones hara las consultas para la estacion 1,2,3,4,5 devolviendo TODOS los datos
            //idConexion tiene que ser numero de estaciones que tenemos
            else if(typeMessage.equals("RefreshAll")){
                mensaje = "RefreshAll-"+idConexion;

            }
            //RefreshTable-numeroEstaciones se especifica cuantas estaciones tenemos siendo el id de las estaciones de 1 a n, es decir
            //si hay 5 estaciones hara las consultas para la estacion 1,2,3,4,5 devolviendo unicamente ID, ubicacion, temperatura, humedad y presion
            //idConexion tiene que ser numero de estaciones que tenemos
            else if(typeMessage.equals("RefreshTable")){
                mensaje = "RefreshTable-"+idConexion;

            }
            //Notify-idEstacion se especifica el id de la estacion y nos devuelve un string con las alertas de esa estacion
            //idConexion tiene que ser idEstacion
            else if(typeMessage.equals("Notify")){
                mensaje = "Notify-"+idConexion;

            }

            salida.writeUTF(mensaje);

            respuesta = entrada.readUTF();
            System.out.println(respuesta);

            entrada.close();
            salida.close();
            cliente.close();

        }catch(IOException e){

            System.out.println("Error: "+e.getMessage());
        }

        return respuesta;
    }
}