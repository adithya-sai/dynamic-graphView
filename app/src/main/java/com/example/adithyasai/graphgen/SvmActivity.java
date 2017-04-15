package com.example.adithyasai.graphgen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.StringTokenizer;
import java.util.Vector;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;

public class SvmActivity extends AppCompatActivity {

    private svm_parameter param;
    private svm_problem prob;
    private int cross_validation;
    private int nr_fold;
    private double accuracy_ans=0;

    private static double atof(String s)
    {
        double d = Double.valueOf(s).doubleValue();
        if (Double.isNaN(d) || Double.isInfinite(d))
        {
            System.err.print("NaN or Infinity in input\n");
            System.exit(1);
        }
        return(d);
    }

    private static int atoi(String s)
    {
        return Integer.parseInt(s);
    }

    public void set_parameters()
    {

        param = new svm_parameter();
        param.svm_type = svm_parameter.C_SVC;
        param.kernel_type = svm_parameter.POLY;
        param.degree = 2;
        param.gamma = 0.007;
        param.coef0 = 0;
        param.nu = 0.5;
        param.cache_size = 100;
        param.C = 10000;
        param.eps = 1e-2;
        param.p = 0.1;
        param.shrinking = 1;
        param.probability = 0;
        param.nr_weight = 0;
        param.weight_label = new int[0];
        param.weight = new double[0];
        cross_validation = 1;
        nr_fold =3;
    }

    public void read_problem() throws IOException
    {
        Reader is = new InputStreamReader(getAssets().open("training_set.txt"));
        BufferedReader br = new BufferedReader(is);
        Vector<Double> vy = new Vector<Double>();
        Vector<svm_node[]> vx = new Vector<svm_node[]>();
        int max_index = 0;

        while(true)
        {
            String line = br.readLine();
            if(line == null) break;

            StringTokenizer st = new StringTokenizer(line," \t\n\r\f:");

            vy.addElement(atof(st.nextToken()));
            int m = st.countTokens()/2;
            svm_node[] x = new svm_node[m];
            for(int j=0;j<m;j++)
            {
                svm_node sn = new svm_node();
                sn.index=atoi(st.nextToken());;
                sn.value=atof(st.nextToken());
                x[j] = sn;
            }
            if(m>0) max_index = Math.max(max_index, x[m-1].index);
            vx.addElement(x);
        }

        prob = new svm_problem();
        prob.l = vy.size();
        prob.x = new svm_node[prob.l][];
        for(int i=0;i<prob.l;i++)
            prob.x[i] = vx.elementAt(i);
        prob.y = new double[prob.l];
        for(int i=0;i<prob.l;i++)
            prob.y[i] = vy.elementAt(i);

        if(param.gamma == 0 && max_index > 0)
            param.gamma = 1.0/max_index;

        if(param.kernel_type == svm_parameter.PRECOMPUTED)
            for(int i=0;i<prob.l;i++)
            {
                if (prob.x[i][0].index != 0)
                {
                    System.err.print("Wrong kernel matrix: first column must be 0:sample_serial_number\n");
                    System.exit(1);
                }
                if ((int)prob.x[i][0].value <= 0 || (int)prob.x[i][0].value > max_index)
                {
                    System.err.print("Wrong input format: sample_serial_number out of range\n");
                    System.exit(1);
                }
            }

        br.close();
    }

    private void do_cross_validation()
    {
        int i;
        int total_correct = 0;
        double total_error = 0;
        double sumv = 0, sumy = 0, sumvv = 0, sumyy = 0, sumvy = 0;
        double[] target = new double[prob.l];

        svm.svm_cross_validation(prob,param,nr_fold,target);
        if(param.svm_type == svm_parameter.EPSILON_SVR ||
                param.svm_type == svm_parameter.NU_SVR)
        {
            for(i=0;i<prob.l;i++)
            {
                double y = prob.y[i];
                double v = target[i];
                total_error += (v-y)*(v-y);
                sumv += v;
                sumy += y;
                sumvv += v*v;
                sumyy += y*y;
                sumvy += v*y;
            }
            System.out.print("Cross Validation Mean squared error = "+total_error/prob.l+"\n");
            System.out.print("Cross Validation Squared correlation coefficient = "+
                    ((prob.l*sumvy-sumv*sumy)*(prob.l*sumvy-sumv*sumy))/
                            ((prob.l*sumvv-sumv*sumv)*(prob.l*sumyy-sumy*sumy))+"\n"
            );
        }
        else
        {
            for(i=0;i<prob.l;i++)
                if(target[i] == prob.y[i])
                    ++total_correct;
            accuracy_ans = 100.0*total_correct/prob.l;
            Toast.makeText(getBaseContext(), "Cross Validation Accuracy = "+100.0*total_correct/prob.l+"%\n", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_svm);

        float accuracy = 100.0f;
        TextView tv = (TextView)findViewById(R.id.accuracy);

        try {

            set_parameters();
            read_problem();

            String error_msg = svm.svm_check_parameter(prob,param);
            System.out.println(error_msg);

            if(error_msg != null)
            {

                Toast.makeText(getBaseContext(), error_msg, Toast.LENGTH_LONG).show();

            }

            if(cross_validation != 0)
            {
                do_cross_validation();
            }
            else
            {
                svm_model model = svm.svm_train(prob,param);
            }

            tv.setText("Cross-validation accuracy: "+accuracy_ans+"\nSVM Type: SVC\nKernel Type: Polynomial\nDegree: 2\nGamma: 0.007\nCache Size: 100");


        }catch(Exception ex) {
            System.out.println("Catch:"+ex.getMessage());
            Toast.makeText(getBaseContext(), ex.getMessage(), Toast.LENGTH_LONG).show();

        }
    }
}
