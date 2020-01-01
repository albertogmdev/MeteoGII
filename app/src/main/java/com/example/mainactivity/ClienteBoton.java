package com.example.mainactivity;

import android.util.Log;

import java.io.*;
import java.net.*;
import java.util.concurrent.Callable;


public class ClienteBoton implements Callable<String> {

    Socket cliente;

    DataInputStream entrada;
    DataOutputStream salida;

    String mensaje, respuesta;

    private String idConexion;

    public ClienteBoton(String id){
        this.idConexion = id;
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

            mensaje = "Refresh-"+this.idConexion;

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
