package com.example.mainactivity;

import java.io.*;
import java.net.*;
import java.util.concurrent.Callable;

import static java.lang.Thread.sleep;


public class ClienteAutomatico implements Callable<String> {

    Socket cliente;

    DataInputStream entrada;
    DataOutputStream salida;

    String mensaje, respuesta, typeMessage;

    private String idConexion;

    public ClienteAutomatico(String id,String typeMesage){
        this.typeMessage = typeMesage;
        this.idConexion = id;
        this.respuesta = "NULL";
    }

    @Override
    public String call(){

        try{

            try {
                sleep(180000);
            } catch (InterruptedException e) {
                System.out.println("ErrorIE: "+ e.getMessage());
            }

            cliente = new Socket("weatherubicuastation.duckdns.org", 8080);

            entrada = new DataInputStream(cliente.getInputStream());
            salida = new DataOutputStream(cliente.getOutputStream());

            //Refresh-idEstacion se especifica el id de la estacion que se quiere consultar y devuelve TODOS los datos
            //idConexion tiene que ser idEstacion
            if(typeMessage.equals("Refresh")){

                mensaje += "Refresh-"+idConexion;
            }
            //Refresh-numeroEstaciones se especifica cuantas estaciones tenemos siendo el id de las estaciones de 1 a n, es decir
            //si hay 5 estaciones hara las consultas para la estacion 1,2,3,4,5 devolviendo TODOS los datos
            //idConexion tiene que ser numero de estaciones que tenemos
            else if(typeMessage.equals("RefreshAll")){
                mensaje += "RefreshAll-"+idConexion;

            }
            //RefreshTable-numeroEstaciones se especifica cuantas estaciones tenemos siendo el id de las estaciones de 1 a n, es decir
            //si hay 5 estaciones hara las consultas para la estacion 1,2,3,4,5 devolviendo unicamente ID, ubicacion, temperatura, humedad y presion
            //idConexion tiene que ser numero de estaciones que tenemos
            else if(typeMessage.equals("RefreshTable")){
                mensaje += "RefreshTable-"+idConexion;

            }
            //Notify-idEstacion se especifica el id de la estacion y nos devuelve un string con las alertas de esa estacion
            //idConexion tiene que ser idEstacion
            else if(typeMessage.equals("Notify")){
                mensaje += "Notify-"+idConexion;

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