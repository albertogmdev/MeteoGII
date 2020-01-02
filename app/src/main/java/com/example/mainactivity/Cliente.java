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
            if(typeMessage.equals("Refresh") && idConexion.equals(""))
            {
                mensaje = "Refresh-All";
            }
            else if(typeMessage.equals("Refresh"))
            {
                mensaje = "Refresh-"+ this.idConexion;
            }
            else if(typeMessage.equals("Notify") && idConexion.equals(""))
            {
                mensaje = "Notify-All";
            }
            else        //Aviso de una única estación
            {
                mensaje = "Notify-" + this.idConexion;
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
