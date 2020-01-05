package com.example.mainactivity.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.mainactivity.Cliente;
import com.example.mainactivity.Monitor;
import com.example.mainactivity.R;
import com.example.mainactivity.Singleton;
import com.example.mainactivity.Threads.ThreadGraph;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class GraphStationActivity extends AppCompatActivity {
    private ThreadGraph hiloRefresh;
    private Monitor monitor;
    private AnyChartView lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_station);
        Singleton.getInstance().setEndConnectionThreadGraphActivity(false);

        lineChart = (AnyChartView)findViewById(R.id.genericalGraph);
        //lineChart.setProgressBar(findViewById(R.id.progress_bar));
        generateLineChart();


        this.monitor = new Monitor();


        this.hiloRefresh = new ThreadGraph(String.valueOf(Singleton.getInstance().getIdentificadorEstacion()), Singleton.getInstance().getTypeGraph(), monitor, GraphStationActivity.this);

        this.hiloRefresh.start();
    }

    public void generateLineChart()
    {
        Cartesian cartesian = AnyChart.line();
        cartesian.animation(true);
        cartesian.padding(10d, 20d, 5d, 20d);
        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                .yStroke((Stroke) null, null, null, (String) null, (String) null);
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.title(Singleton.getInstance().getTypeGraph() + " Station " + Singleton.getInstance().getIdentificadorEstacion());
        if(Singleton.getInstance().getTypeGraph().equals("HumidityGraph"))
        {
            cartesian.yAxis(0).title("%");
        }
        else
        {
            cartesian.yAxis(0).title("Value");
        }

        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

        Set set = Set.instantiate();
        set.data(getData());
        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
        Line series1 = cartesian.line(series1Mapping);
        if(Singleton.getInstance().getTypeGraph().equals("TemperatureGraph"))
        {
            series1.name("Temperature");
        }
        else if(Singleton.getInstance().getTypeGraph().equals("HumidityGraph"))
        {
            series1.name("Humidity");
        }
        else
        {
            series1.name("Radiation");
        }
        series1.hovered().markers().enabled(true);
        series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series1.tooltip()
                .position("center")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);
        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);
        lineChart.setChart(cartesian);
    }

    public  ArrayList getData()
    {
        ArrayList<String[]> rows = new ArrayList<>();
        String respuesta = resultadoRefresh();

        String[] listaFilas = respuesta.split("\n");

        for(int i = 0; i < listaFilas.length; i++)
        {
            String[] columnas = listaFilas[i].split("//");
            //Array con las unidades
            String nuevo[] = new String[5];

            for(int j=0; j<columnas.length; j++){
                switch (j){
                    //Fecha
                    case 0:
                        nuevo[j] = columnas[j];
                    //Dato
                    case 1:
                        nuevo[j] = columnas[j];
                        break;
                    default:
                        break;
                }
            }

            rows.add(nuevo);
        }

        return rows;
    }

    private String resultadoRefresh() {
        FutureTask task;
        if(Singleton.getInstance().getTypeGraph().equals("TemperatureGraph"))
        {
            task = new FutureTask(new Cliente(String.valueOf(Singleton.getInstance().getIdentificadorEstacion()), "RefreshTemperature"));
        }
        else if(Singleton.getInstance().getTypeGraph().equals("HumidityGraph"))
        {
            task = new FutureTask(new Cliente(String.valueOf(Singleton.getInstance().getIdentificadorEstacion()), "RefreshHumidity"));
        }
        else
        {
            task = new FutureTask(new Cliente(String.valueOf(Singleton.getInstance().getIdentificadorEstacion()), "RefreshRadiation"));
        }

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Singleton.getInstance().setEndConnectionThreadGraphActivity(true);
        startActivity(new Intent(GraphStationActivity.this, GraphStationActivity.class));
        finish();
    }
}