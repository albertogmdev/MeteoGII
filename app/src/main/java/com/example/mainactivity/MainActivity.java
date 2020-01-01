package com.example.mainactivity;

import androidx.annotation.MainThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.concurrent.*;

public class MainActivity extends AppCompatActivity {

    private TextView texto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        texto = findViewById(R.id.textView);
    }


    public void startButton(View v) {

        setContentView(R.layout.activity_station_information);
    }

    public void refrescarButton(View v) {

        setContentView(R.layout.activity_station_information);

        TextView temperatura = findViewById(R.id.temperaturaValor);
        TextView humedad = findViewById(R.id.humedadValor);
        TextView presion = findViewById(R.id.presionValor);
        TextView lluvia= findViewById(R.id.lluviaValor);
        TextView luz= findViewById(R.id.luzValor);
        TextView anemometro = findViewById(R.id.anemometroValor);
        TextView radiacion = findViewById(R.id.radiacionValor);
        TextView oxigeno = findViewById(R.id.oxigenoValor);
        TextView amoniaco = findViewById(R.id.amoniacoValor);
        TextView sulfuro = findViewById(R.id.sulfuroValor);
        TextView benzeno = findViewById(R.id.benzenoValor);
        TextView humo = findViewById(R.id.humoValor);

        String consulta = resultadoRefresh();
        String[] datos = consulta.split("//");

        temperatura.setText(String.format(datos[2]));
        humedad.setText(datos[3]);
        presion.setText(datos[4]);
        lluvia.setText(datos[5]);
        luz.setText(datos[6]);
        radiacion.setText(datos[7]);
        anemometro.setText(datos[8]);
        oxigeno.setText(datos[9]);
        amoniaco.setText(datos[10]);
        sulfuro.setText(datos[11]);
        benzeno.setText(datos[12]);
        humo.setText(datos[13]);

        /*if(Integer.parseInt(datos[2])>=10){
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MainActivity.this)
                    .setSmallIcon(android.R.drawable.stat_sys_warning)
                    .setLargeIcon((((BitmapDrawable)getResources().getDrawable(R.drawable.nubes))).getBitmap())
                    .setContentTitle("CUIDADO")
                    .setContentText("Notificacion alerta")
                    .setContentInfo("Temperatura alta")
                    .setTicker("Alerta¡¡");

            Intent noIntent = new Intent(MainActivity.this, MainActivity.class);
            PendingIntent contIntent = PendingIntent.getActivity(MainActivity.this, 0, noIntent, 0);
            mBuilder.setContentIntent(contIntent);

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(6504, mBuilder.build());
        }*/
    }


    public String resultadoRefresh() {

        FutureTask task = new FutureTask(new ClienteBoton("1"));

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