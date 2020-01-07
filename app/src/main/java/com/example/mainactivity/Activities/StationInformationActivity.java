package com.example.mainactivity.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import com.example.mainactivity.Cliente;
import com.example.mainactivity.Monitor;
import com.example.mainactivity.R;
import com.example.mainactivity.Singleton;
import com.example.mainactivity.Threads.ThreadStationInformationActivity;
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
    private TextView sensacion;
    private TextView calidadAire;
    private ImageView tiempoImagen;
    private ImageButton graficasButton;
    private static final String[] stations = Singleton.getInstance().getStations();
    private Monitor monitor = new Monitor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_information);
        //Log.e("HEY", "Problem2");
        Singleton.getInstance().setEndConnectionThreadStationActivity(false);
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
        sensacion = findViewById(R.id.sensacionValor);
        radiacion = findViewById(R.id.radiacionValor);
        calidadAire = findViewById(R.id.calidadValor);
        tiempoImagen = findViewById(R.id.tiempoValor);
        graficasButton = findViewById(R.id.graficas);
        monitor = new Monitor();

        notifyStation();
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
                notifyStation();
                refresh(resultadoRefresh());
            }
        });

        ThreadStationInformationActivity hiloEstacionRefresh = new ThreadStationInformationActivity(spinnerStation.getText().toString(), "Refresh", monitor, StationInformationActivity.this);
        ThreadStationInformationActivity hiloEstacionNotify = new ThreadStationInformationActivity(spinnerStation.getText().toString(), "Notify", monitor, StationInformationActivity.this);
        hiloEstacionRefresh.start();
        hiloEstacionNotify.start();
    }

    //No se como hacer que vaya a la siguiente pestaña
    public void graficasBoton(View v)
    {
        Singleton.getInstance().setIdentificadorEstacion(spinnerStation.getText().toString());
        //Singleton.getInstance().setEndConnectionThreadStationActivity(true);
        startActivity(new Intent(StationInformationActivity.this, StationGraphicsActivity.class));
        finish();
    }

    public void refrescarButton(View v) {

        String consulta = resultadoRefresh();
        refresh(consulta);
    }

    public void refresh(String consulta)
    {
        Log.e("HEY", consulta);

        //La consulta tiene 0-ID_Estacion 1-Fecha_Hora 2-Temperatura 3-Humedad 4-Presion_Atmosferica 5-Cantidad_Lluvia
        // 6-Nivel_Luz 7-Nivel_Radiacion 8-Valor_Sensor_Efecto 9-Sensacion_termica 10-Calidad_aire
        String[] datos = consulta.split("//");

        //Habria que llamar al servidor con el token 6 y 5, luz y lluvia
        String rutaTiempo = resultadoImage(String.format(datos[6]), String.format(datos[5]));

        if(!rutaTiempo.isEmpty() && !rutaTiempo.toLowerCase().equals("null"))
        {
            //Habria que cambiar este TextView por un ImageView
            switch(rutaTiempo){
                case "diaLluvioso.png":
                    tiempoImagen.setImageResource(R.drawable.dialluvioso);
                    break;
                case "soleado.png":
                    tiempoImagen.setImageResource(R.drawable.soleado);
                    break;
                case "nocheLluviosa.png":
                    tiempoImagen.setImageResource(R.drawable.nochelluviosa);
                    break;
                case "noche.png":
                    tiempoImagen.setImageResource(R.drawable.noche);
                    break;
                case "nublado.png":
                    tiempoImagen.setImageResource(R.drawable.nublado);
                    break;
                default :
                    break;
            }
        }
        else
        {
            //Aqui poner lo que se quiera se puede poner una imagen de error o lo que sea
            tiempoImagen.setImageResource(R.drawable.icon_no_image);
        }

        if(!datos[2].equals("NULL"))
        {
            temperatura.setText(String.format(datos[2])+"ºC");
        }
        else
        {
            temperatura.setText("0");
        }

        if(!datos[3].equals("NULL"))
        {
            humedad.setText(String.format(datos[3])+"%");
        }
        else
        {
            humedad.setText("0");
        }

        if(!datos[4].equals("NULL"))
        {
            presion.setText(datos[4]+"Pa");
        }
        else
        {
            presion.setText("0");
        }

        if(!datos[5].equals("NULL"))
        {
            lluvia.setText(String.format(datos[5]));
        }
        else
        {
            lluvia.setText("0");
        }

        if(!datos[6].equals("NULL"))
        {
            luz.setText(String.format(datos[6]));
        }
        else
        {
            luz.setText("0");
        }

        if(!datos[7].equals("NULL"))
        {
            radiacion.setText(String.format(datos[7]));
        }
        else
        {
            radiacion.setText("0");
        }

        if(!datos[8].equals("NULL"))
        {
            anemometro.setText(String.format(datos[8]));
        }
        else
        {
            anemometro.setText("0");
        }

        if(!datos[9].equals("NULL"))
        {
            sensacion.setText(String.format(datos[9])+"ºC");
        }
        else
        {
            sensacion.setText("0");
        }

        if(!datos[10].equals("NULL"))
        {
            calidadAire.setText(String.format(datos[10]));
        }
        else
        {
            calidadAire.setText("0");
        }

    }


    public String resultadoRefresh() {
        Singleton.getInstance().setIdentificadorEstacion(spinnerStation.getText().toString());
        FutureTask task = new FutureTask(new Cliente(spinnerStation.getText().toString(), "Refresh"));

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

    public String resultadoImage(String idConexion, String idConexion2){

        FutureTask task = new FutureTask(new Cliente(idConexion, idConexion2, "Weather"));

        ExecutorService es = Executors.newSingleThreadExecutor();
        es.submit(task);

        String result = "";

        try {

            result = task.get().toString();
        } catch (Exception e) {

            System.err.println(e);
        }

        es.shutdown();

        return result;
    }

    public void notifyStation(){

        String alertaEstacion = resultadoNotify();

        if(!alertaEstacion.isEmpty()){
            crearNotificacion("¡ALERTA! Estación "+ spinnerStation.getText().toString(), alertaEstacion, getApplicationContext());
        }

    }

    public String resultadoNotify(){

        FutureTask task = new FutureTask(new Cliente(spinnerStation.getText().toString(), "Notify"));

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


    public void crearNotificacion(String title, String message, Context context) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = createID();
        String channelId = "channel-id";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.notificacion)
                .setContentTitle(title)
                .setContentText(message)
                .setVibrate(new long[]{100, 250})
                .setLights(Color.YELLOW, 500, 5000)
                .setAutoCancel(true)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary));

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(new Intent(context, StationInformationActivity.class));
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(notificationId, mBuilder.build());
    }

    public int createID() {
        Date now = new Date();
        int id = Integer.parseInt(new SimpleDateFormat("ddHHmmss", Locale.FRENCH).format(now));
        return id;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(StationInformationActivity.this, MainActivity.class));
        Singleton.getInstance().setEndConnectionThreadStationActivity(true);
        finish();
    }


}
