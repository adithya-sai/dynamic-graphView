package com.example.adithyasai.graphgen;


import android.content.Context;
import android.os.AsyncTask;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by adithyasai on 3/7/17.
 */

public class DownloadOperation extends AsyncTask<String, Integer, String> {

    private Context context;

    public DownloadOperation(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... sUrl) {

        return this.getDatabaseFromServer();

    }

    public String getDatabaseFromServer(){

        String surl = "https://impact.asu.edu/CSE535Spring17Folder/Group28";


        //searchButton = (Button) findViewById(R.id.button1);
        InputStream input = null;
        OutputStream output = null;
        HttpsURLConnection connection = null;
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                // Not implemented
            }

            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                // Not implemented
            }
        } };

        try {
            SSLContext sc = SSLContext.getInstance("TLS");

            sc.init(null, trustAllCerts, new java.security.SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (KeyManagementException e) {
            return e.toString();
        } catch (NoSuchAlgorithmException e) {
            return e.toString();
        }
        try {
            URL url = new URL(surl);
            connection = (HttpsURLConnection) url.openConnection();
            connection.connect();

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode() != HttpsURLConnection.HTTP_OK) {
                return "Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage();
            }

            // this will be useful to display download percentage
            // might be -1: server did not report the length
            int fileLength = connection.getContentLength();

            //downloadButton.setText(Integer.toString(fileLength));
            // download the file

            input = connection.getInputStream();
            File dbFile = new File("/data/data/com.example.adithyasai.graphgen/databases/Group28");
            output = new FileOutputStream(dbFile);

            //downloadButton.setText("Connecting .....");
            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                // allow canceling with back button

                total += count;

                output.write(data, 0, count);
            }
        } catch (Exception e) {
            return e.toString();
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
                return ignored.toString();
            }

            if (connection != null)
                connection.disconnect();
        }
        return null;

    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
    }

    @Override
    protected void onPostExecute(String result) {
//        RadioButton rb3 = (RadioButton) findViewById(R.id.female_button);
//        RadioButton rb4 = (RadioButton) findViewById(R.id.male_button);
//        rb3.setSelected(false);
//        rb4.setSelected(false);
        if (result != null){
            Toast.makeText(context,"Download error: "+result, Toast.LENGTH_LONG).show();

        }else{
            Toast.makeText(context,"File downloaded; Select patient details to start graph with downloaded data", Toast.LENGTH_SHORT).show();
        }
    }
}