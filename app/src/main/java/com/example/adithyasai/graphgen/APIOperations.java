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

/**
 * Created by p4nd0r45b0x on 3/5/17.
 */

public class APIOperations extends AsyncTask<String,Integer,String> {

    private Context context;

    public APIOperations(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... s) {

        String surl = "https://impact.asu.edu/CSE535Spring17Folder/UploadToServer.php";
        String source_file_loc = "/data/data/com.example.adithyasai.graphgen/databases/Group28";

        try {
            HttpsURLConnection connection = null;
            DataOutputStream dos = null;
            FileInputStream fileInputStream = null;
            File sourceFile = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
            int serverResponseCode = 0;

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

                sourceFile = new File(source_file_loc);
                fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(surl);
                connection = (HttpsURLConnection) url.openConnection();

                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                dos = new DataOutputStream(connection.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + source_file_loc + "\"" + lineEnd);

                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = connection.getResponseCode();
                if (serverResponseCode != 200) {
                    return "server returned: " + connection.getResponseCode() + " message: " + connection.getResponseMessage();
                } else {
                    System.out.print("\n\n\n\n\n\nRESPONSE CODE: "+serverResponseCode);
                    System.out.println("RESPONSE MESSAGE: "+connection.getResponseMessage());
                }

            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    fileInputStream.close();
                    dos.flush();
                    dos.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
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
