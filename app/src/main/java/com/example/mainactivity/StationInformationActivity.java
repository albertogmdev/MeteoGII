package com.example.mainactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import com.weiwangcn.betterspinner.library.BetterSpinner;

public class StationInformationActivity extends AppCompatActivity {
    private BetterSpinner spinnerStation;
    private TextView temperatura;
    private TextView humedad;
    private TextView presion;
    private TextView lluvia;
    private TextView luz;
    private TextView anemometro;
    private TextView radiacion;
    private TextView oxigeno;
    private TextView amoniaco;
    private TextView sulfuro;
    private TextView benzeno;
    private TextView humo;
    private static final String[] stations = {"1", "2","3","4","5"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_information);
        //Log.e("HEY", "Problem2");
        spinnerStation = findViewById(R.id.spinnerestacion);
        ArrayAdapter<String> adapterStations = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, stations);
        spinnerStation.setAdapter(adapterStations);
        spinnerStation.setText(Singleton.getInstance().getIdentificadorEstacion());
        temperatura = findViewById(R.id.temperaturaValor);
        humedad = findViewById(R.id.humedadValor);
        presion = findViewById(R.id.presionValor);
        lluvia = findViewById(R.id.lluviaValor);
        luz = findViewById(R.id.luzValor);
        anemometro = findViewById(R.id.anemometroValor);
        radiacion = findViewById(R.id.radiacionValor);
        oxigeno = findViewById(R.id.oxigenoValor);
        amoniaco = findViewById(R.id.amoniacoValor);
        sulfuro = findViewById(R.id.sulfuroValor);
        benzeno = findViewById(R.id.benzenoValor);
        humo = findViewById(R.id.humoValor);
        refresh(resultadoRefresh());

        spinnerStation.addTextChangedListener(new TextWatcher()
        {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Does not do any thing in this case
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Does not do any thing in this case
            }

            @Override
            public void afterTextChanged(Editable s) {
                Singleton.getInstance().setIdentificadorEstacion(spinnerStation.getText().toString());
                refresh(resultadoRefresh());
            }
        });
    }

    public void refrescarButton(View v) {
        Log.e("HEY", "Problem");
        //startActivity(new Intent(StationInformationActivity.this, StationInformationActivity.class));
        //super.onBackPressed();
        //finish();
        //setContentView(R.layout.activity_station_information);
        String consulta = resultadoRefresh();
        refresh(consulta);

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

    public void refresh(String consulta)
    {

        if(!consulta.equals("NULL"))
        {

            Log.e("HEY", consulta);
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
        }
        else
        {
            temperatura.setText("0");
            humedad.setText("0");
            presion.setText("0");
            lluvia.setText("0");
            luz.setText("0");
            radiacion.setText("0");
            anemometro.setText("0");
            oxigeno.setText("0");
            amoniaco.setText("0");
            sulfuro.setText("0");
            benzeno.setText("0");
            humo.setText("0");
        }
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
