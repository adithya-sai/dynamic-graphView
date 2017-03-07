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
import java.net.HttpURLConnection;
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

        InputStream input = null;
        OutputStream output = null;
        HttpsURLConnection connection = null;
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
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


            if (connection.getResponseCode() != HttpsURLConnection.HTTP_OK) {
                return "Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage();
            }

            int fileLength = connection.getContentLength();



            input = connection.getInputStream();
            File dbFile = new File("/data/data/com.example.adithyasai.graphgen/databases/Group28");
            output = new FileOutputStream(dbFile,false);

            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {


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
        if (result != null){
            Toast.makeText(context,"Download error: "+result, Toast.LENGTH_LONG).show();

        }else{
            Toast.makeText(context,"Downloaded db from web server", Toast.LENGTH_SHORT).show();
        }
    }
}