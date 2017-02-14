package com.example.adithyasai.graphgen;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

//Using third-party library
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    final Handler graphHandler = new Handler();
    Thread t;
    boolean flag=false;
    LineGraphSeries<DataPoint> series;
    final float[] val = new float[50];
    final float[] random_val=new float[4];
    boolean count=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        for (int i = 0; i < 12; i++) {
            Random r = new Random();
            val[i] = r.nextFloat();
        }
        for (int i = 0; i < 4; i++) {
            Random r = new Random();
            random_val[i] = r.nextFloat();
        }

        random_val[1]+=8700;
        random_val[2]+=1500;
        random_val[3]+=19000;
        Button button = (Button) findViewById(R.id.start);
        button.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
                    flag=true;
            }
        });
        Button stop = (Button) findViewById(R.id.stop);
        stop.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
                try {
                    flag=false;
                    count=false;
                }catch(Exception e)
                {
                }
                GraphView layout = (GraphView) findViewById(R.id.graph);
                layout.removeAllSeries();
            }
        });
    }

    public void onResume() {
        super.onResume();
        final GraphView graph=(GraphView) findViewById(R.id.graph);
        graph.setTitle("Heartbeat");
        graph.removeAllSeries();
        t = new Thread(new Runnable() {
            @Override
            public void run() {

                graph.removeAllSeries();

                series = new LineGraphSeries<DataPoint>(new DataPoint[]{
                        new DataPoint(2600, val[0]),
                        new DataPoint(2650, val[1]),
                        new DataPoint(2700, val[2]),
                        new DataPoint(2750, val[3]),
                        new DataPoint(2800, val[0]),
                        new DataPoint(2850, val[1]),
                        new DataPoint(2900, val[2]),
                        new DataPoint(2950, val[3]),
                        new DataPoint(3000, val[0]),
                        new DataPoint(3050, val[1]),
                        new DataPoint(3100, val[2]),
                        new DataPoint(3150, val[3]),
                        new DataPoint(3200, val[0]),
                        new DataPoint(3250, val[1]),
                        new DataPoint(3300, val[2]),
                        new DataPoint(3350, val[3]),
                        new DataPoint(3400, val[0])
                });
                series.setColor(Color.RED);
                graph.addSeries(series);
                if(flag==true) {
                    if (count == false) {
                        for (int i = 0; i < 4; i++) {
                            val[i] = random_val[i];
                        }
                        count=true;
                    } else {
                        float temp = val[0];
                        for (int i = 0; i < 4; i++) {
                            val[i] = val[i + 1];
                        }
                        val[3] = temp;
                        graph.addSeries(series);
                    }
                }
                else if (flag==false){
                    for(int i=0;i<10;i++){
                        val[i]=0;
                    }
                    graph.removeAllSeries();
                }


                graphHandler.postDelayed(this, 600);
            }
        });
        t.start();
    }

}

