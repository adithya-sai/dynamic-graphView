package com.example.adithyasai.graphgen;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.*;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.*;
import javax.xml.transform.Result;



public class APIOperations extends AsyncTask<String,Integer,String> {

    private Context context;

    public APIOperations(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... s) {

        String surl = "https://impact.asu.edu/CSE535Spring17Folder/UploadToServer.php";
        String sfile = "/data/data/com.example.adithyasai.graphgen/databases/Group28";

        try {
            HttpsURLConnection conn = null;
            DataOutputStream dataOutputStream = null;
            FileInputStream fileInputStream = null;
            File sourceFile = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1048576;
            int serverResponseCode = 0;
            //Source:- http://stackoverflow.com/questions/7443235/getting-java-to-accept-all-certs-over-https
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
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
            }};

            try {
                SSLContext sc = SSLContext.getInstance("TLS");

                sc.init(null, trustAllCerts, new java.security.SecureRandom());

                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            } catch (KeyManagementException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            try {
                //Source:-http://androidexample.com/Upload_File_To_Server_-_Android_Example/index.php?view=article_discription&aid=83
                sourceFile = new File(sfile);
                fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(surl);
                conn = (HttpsURLConnection) url.openConnection();

                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                dataOutputStream = new DataOutputStream(conn.getOutputStream());

                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + sfile + "\"" + lineEnd);

                dataOutputStream.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dataOutputStream.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                if (serverResponseCode != 200) {
                    return "server returned: " + conn.getResponseCode() + " message: " + conn.getResponseMessage();
                } else {
                    System.out.print("\n\n\n\n\n\nRESPONSE CODE: "+serverResponseCode);
                    System.out.println("RESPONSE MESSAGE: "+conn.getResponseMessage());
                }

            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    fileInputStream.close();
                    dataOutputStream.flush();
                    dataOutputStream.close();
                } catch (IOException ignored) {
                }

                if (conn != null)
                    conn.disconnect();
            }
            return null;
        }catch(Exception ex) {
            //Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
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
            Toast.makeText(context,"Upload error: "+result, Toast.LENGTH_LONG).show();


        }else{
            Toast.makeText(context,"File uploaded", Toast.LENGTH_SHORT).show();

        }
    }
}
