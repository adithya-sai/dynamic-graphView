package com.example.adithyasai.graphgen;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
public class PerformanceActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance);
        GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 14),
                new DataPoint(100, 38),
                new DataPoint(200, 43),
                new DataPoint(300, 34),
                new DataPoint(400, 36),
                new DataPoint(500, 40),
                new DataPoint(600, 26),
                new DataPoint(700, 17),
                new DataPoint(800, 28),
                new DataPoint(900, 15),
                new DataPoint(1000, 13),
                new DataPoint(1100, 14),
                new DataPoint(1200, 14)
        });
        graph.addSeries(series);
        graph.setTitle("Time vs CPU Power Consumption");
        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Time (ms)");
        gridLabel.setVerticalAxisTitle(("Power Consumption (%)"));
        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);
    }
}
