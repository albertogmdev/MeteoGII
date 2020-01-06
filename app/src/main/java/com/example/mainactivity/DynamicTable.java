package com.example.mainactivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.mainactivity.Activities.MainActivity;
import com.example.mainactivity.Activities.StationInformationActivity;
import com.example.mainactivity.Activities.WeatherGraphStationActivity;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import static java.lang.Thread.sleep;

public class DynamicTable {
    private MainActivity mainActivity;
    private WeatherGraphStationActivity weatherGraphStation;
    private TableLayout tableLayout;
    private Context context;
    private String[] header;
    private ArrayList<String[]> data;
    private TableRow tableRow;
    private TextView textCell;
    private Monitor monitor;
    //private ThreadMainActivity hiloRefresh;

    private int indexCell;
    private int indexRow;

    public DynamicTable(MainActivity mainActivity, TableLayout tableLayout, Context context, Monitor monitor)
    {
        this.mainActivity = mainActivity;
        this.tableLayout = tableLayout;
        this.context = context;
        this.monitor = monitor;
        //this.hiloRefresh = new ThreadMainActivity(String.valueOf(mainActivity.getNumberStations()), "RefreshTable", monitor,DynamicTable.this, mainActivity);
        //this.hiloRefresh.start();

        //Singleton.getInstance().resetCounterStations();
    }

    public DynamicTable(WeatherGraphStationActivity weatherGraphStation, TableLayout tableLayout, Context context, Monitor monitor)
    {
        this.weatherGraphStation = weatherGraphStation;
        this.tableLayout = tableLayout;
        this.context = context;
        this.monitor = monitor;
        //this.hiloRefresh = new ThreadMainActivity(String.valueOf(mainActivity.getNumberStations()), "RefreshTable", monitor,DynamicTable.this, mainActivity);
        //this.hiloRefresh.start();

        //Singleton.getInstance().resetCounterStations();
    }

    /**Incluir la cabecera de la tabla dinámica*/
    public void addHeaderMainActivity(String[] header)
    {
        this.header = header;
        createHeader();
        //Singleton.getInstance().resetCounterStations();
    }

    /**Incluir los datos de la tabla dinámica*/
    public void addDataMainActivity(ArrayList<String[]> data)
    {
        this.data = data;
        //Singleton.getInstance().resetCounterStations();
        createDataTable();
    }

    /**Crear una nueva fila de la tabla dinámica*/
    public void newRow()
    {

        tableRow = new TableRow(context);
        //Singleton.getInstance().addOneStation();
    }

    /**Crear una nueva celda de la tabla dinámica*/
    public void newCell()
    {
        textCell = new TextView(context);
        textCell.setGravity(Gravity.CENTER);
        textCell.setTextSize(20);
        textCell.setTextColor(Color.parseColor("#F0EBEB"));
    }

    /**Crear la cabecera de la tabla dinámica*/
    public void createHeader()
    {
        indexCell = 0;
        newRow();
        while (indexCell < header.length)
        {
            newCell();
            textCell.setText(header[indexCell++]);
            tableRow.addView(textCell, newTableRowParams());
        }
        tableLayout.addView(tableRow);
    }

    /**Crear los datos de la tabla de datos de la tabla dinámica*/
    private void createDataTable()
    {
        String info;
        for(indexRow = 0; indexRow < data.size(); indexRow++ )
        {
            newRow();
            for(indexCell = 0; indexCell < header.length; indexCell++)
            {
                newCell();
                String[] rows = data.get(indexRow);
                if(indexCell == 0 && !Singleton.getInstance().getTypeGraph().equals("WeatherGraph"))
                {
                    info = rows[indexCell];
                    Button buttonStation = new Button(context);
                    buttonStation.setText(info);
                    Singleton.getInstance().addStation(info);
                    //Asignamose el Listener
                    final String finalInfo = info;
                    buttonStation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(context, " Listener botón " + v.getTag(), Toast.LENGTH_SHORT).show();
                            Singleton.getInstance().setIdentificadorEstacion(finalInfo);
                            Singleton.getInstance().setEndConnectionThreadMainActivity(true);
                            mainActivity.startActivity(new Intent(mainActivity, StationInformationActivity.class));
                            mainActivity.finish();
                        }
                    });
                    buttonStation.setBackgroundColor(Color.parseColor("#333232"));
                    buttonStation.setTextColor(Color.parseColor("#F0EBEB"));
                    buttonStation.setTextSize(20);
                    tableRow.addView(buttonStation, newTableRowParams());

                }
                else
                {
                    if(Singleton.getInstance().getTypeGraph().equals("WeatherGraph") && indexCell==1)
                    {
                        info = rows[indexCell];
                        ImageView tiempoImagen = new ImageView(context);
                        if(!info.isEmpty() && !info.equals("NULL") && !info.equals("null.png"))
                        {
                            //Habria que cambiar este TextView por un ImageView
                            switch(info){
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

                        scaleImage(tiempoImagen);
                        tableRow.addView(tiempoImagen, newTableRowParams());


                    }
                    else
                    {
                        if(indexCell < rows.length)
                        {
                            info = rows[indexCell];
                        }
                        else
                        {
                            info = "";
                        }
                        textCell.setText(info);
                        tableRow.addView(textCell, newTableRowParams());
                    }
                }

            }
            tableLayout.addView(tableRow);
        }
    }

    private void scaleImage(ImageView view) throws NoSuchElementException  {
        // Get bitmap from the the ImageView.
        Bitmap bitmap = null;

        try {
            Drawable drawing = view.getDrawable();
            bitmap = ((BitmapDrawable) drawing).getBitmap();
        } catch (NullPointerException e) {
            throw new NoSuchElementException("No drawable on given view");
        } catch (ClassCastException e) {
            // Check bitmap is Ion drawable
        }

        // Get current dimensions AND the desired bounding box
        int width = 0;

        try {
            width = bitmap.getWidth();
        } catch (NullPointerException e) {
            throw new NoSuchElementException("Can't find bitmap on given view/drawable");
        }

        int height = bitmap.getHeight();
        int bounding = dpToPx(250);

        // Determine how much to scale: the dimension requiring less scaling is
        // closer to the its side. This way the image always stays inside your
        // bounding box AND either x/y axis touches it.
        float xScale = ((float) bounding) / width;
        float yScale = ((float) bounding) / height;
        float scale = (xScale <= yScale) ? xScale : yScale;

        // Create a matrix for the scaling and add the scaling data
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // Create a new bitmap and convert it to a format understood by the ImageView
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        width = scaledBitmap.getWidth(); // re-use
        height = scaledBitmap.getHeight(); // re-use
        BitmapDrawable result = new BitmapDrawable(scaledBitmap);

        // Apply the scaled bitmap
        view.setImageDrawable(result);

        // Now change ImageView's dimensions to match the scaled image
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        view.setLayoutParams(params);

    }

    private int dpToPx(int dp) {
        float density = weatherGraphStation.getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round((float)dp * density);
    }


    /**Marcar los márgenes de las celdas de la tabla dinámica*/
    private TableRow.LayoutParams newTableRowParams()
    {
        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.setMargins(5,1,5,1);

        params.weight = 1;
        return params;
    }

    public void resetTableMainActivity()
    {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tableRow.removeAllViews();
                tableLayout.removeAllViewsInLayout();
                monitor.setStopThreadResetTable(false);
                monitor.activeThread();
            }
        });
    }

    public void addHeaderMainActivity()
    {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                addHeaderMainActivity(Singleton.getInstance().getHeaderMainActivity());
                monitor.setStopThreadHeader(false);
                monitor.activeThread();
            }
        });
    }

    public void addDataMainActivity()
    {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                data = new ArrayList<>();
                mainActivity.setRows(new ArrayList<String[]>());
                Singleton.getInstance().resetStations();
                addDataMainActivity(mainActivity.getData());
                monitor.setStopThreadData(false);
                monitor.activeThread();
            }
        });
    }

    public void resetTableWeatherGraphStationActivity()
    {
        weatherGraphStation.runOnUiThread(new Runnable() {
            @Override
            public void run() {
            tableRow.removeAllViews();
            tableLayout.removeAllViewsInLayout();
            monitor.setStopThreadResetTable(false);
            monitor.activeThread();
            }
        });
    }

    public void addHeaderWeatherGraphStationActivity()
    {
        weatherGraphStation.runOnUiThread(new Runnable() {
            @Override
            public void run() {
            addHeaderMainActivity(Singleton.getInstance().getHeaderWeatherGraphStationActivity());
            monitor.setStopThreadHeader(false);
            monitor.activeThread();
            }
        });
    }

    public void addDataWeatherGraphStationActivity()
    {
        weatherGraphStation.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                data = new ArrayList<>();
                weatherGraphStation.setRows(new ArrayList<String[]>());
                Singleton.getInstance().resetStations();
                addDataMainActivity(weatherGraphStation.getData());
                monitor.setStopThreadData(false);
                monitor.activeThread();
            }
        });
    }
}
