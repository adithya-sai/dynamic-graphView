package com.example.adithyasai.graphgen;

import android.os.Environment;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import java.io.File;
import java.io.FileInputStream;

/**
 * Created by p4nd0r45b0x on 3/5/17.
 */

public class APIOperations {

    public static void pushDatabaseToServer() {
        String url = "http://yourserver";
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "Group28");
        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            InputStreamEntity reqEntity = new InputStreamEntity(
                    new FileInputStream(file), -1);
            reqEntity.setContentType("binary/octet-stream");
            reqEntity.setChunked(true);
            httppost.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(httppost);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
