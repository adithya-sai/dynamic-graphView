package com.example.adithyasai.graphgen;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.hardware.SensorEvent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

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
    EditText idField;
    EditText patientNameField;
    EditText ageField;
    RadioGroup genderField;
    RadioButton genderSelected;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        idField=(EditText) findViewById(R.id.id);
        patientNameField=(EditText) findViewById(R.id.patientName);
        ageField=(EditText) findViewById(R.id.age);
        genderField=(RadioGroup) findViewById(R.id.gender);
        submit=(Button) findViewById(R.id.submit);

        for (int i = 0; i < 12; i++) {
            Random r = new Random();
            val[i] = r.nextFloat();
        }
        for (int i = 0; i < 4; i++) {
            Random r = new Random();
            random_val[i] = r.nextFloat();
        }
//        Long tsLong = System.currentTimeMillis()/1000;
//        String ts = tsLong.toString();
//        AccelorometerReading ar=new AccelorometerReading(ts,5,4,2);
//        db.addCoordinates(ar);
//        //this.deleteDatabase("Name_ID_Age_Sex");
        DatabaseHandler db =new DatabaseHandler(this);
        SQLiteDatabase sqldb=db.getWritableDatabase();

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
//Gautham's Changes beginning
    static final float NS2S = 1.0f / 1000000000.0f;
    float[] last_values = null;
    float[] velocity = null;
    float[] position = null;
    long last_timestamp = 0;

    public void onSensorChanged(SensorEvent event){
        if(last_values != null){
            float dt = (event.timestamp - last_timestamp) * NS2S;

            for(int index = 0; index < 3;++index){
                velocity[index] += (event.values[index] + last_values[index])/2 * dt;
                position[index] += velocity[index] * dt;
            }
        }
        else{
            last_values = new float[3];
            velocity = new float[3];
            position = new float[3];
            velocity[0] = velocity[1] = velocity[2] = 0f;
            position[0] = position[1] = position[2] = 0f;
        }
        System.arraycopy(event.values, 0, last_values, 0, 3);
        last_timestamp = event.timestamp;
    }
//Gautham's changes ending
    public void getFormValues(View v){
//        Toast.makeText(this, "Clicked on Button", Toast.LENGTH_LONG).show();
        String id=idField.getText().toString();
        String name=patientNameField.getText().toString();
        String age=ageField.getText().toString();
        int selectedRadio=genderField.getCheckedRadioButtonId();
        genderSelected=(RadioButton) findViewById(selectedRadio);
        String gender=genderSelected.getText().toString();
        Toast.makeText(this, id+name+age+gender, Toast.LENGTH_LONG).show();
        createTable(id,name,age,gender);
    }
    public void createTable(String id, String name,String age,String gender){
        DatabaseHandler db =new DatabaseHandler(this);
        SQLiteDatabase sqldb=db.getWritableDatabase();
        String tableName=name+'_'+id+'_'+age+'_'+gender;
        sqldb.execSQL("CREATE TABLE IF NOT EXISTS "+tableName+"(timestamp varchar(10) primary key,x float,y float,z float)");
        sqldb.close();
    }



}

