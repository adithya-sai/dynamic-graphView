package com.example.adithyasai.graphgen;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.*;

/**
 * Created by adithyasai on 2/22/17.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION=4;
    private static final String DATABASE_NAME="Group28";
    private static final String DATABASE_TABLE="Name_ID_Age_Sex";
    private static final String KEY_ID="timestamp";
    private static final String COORDINATE1="X";
    private static final String COORDINATE2="Y";
    private static final String COORDINATE3="Z";

    public void addCoordinates(String values, String  tablename){
        SQLiteDatabase db=this.getWritableDatabase();
//        System.out.println(values);
        String st= new String();
        for(int i=0;i<50;i++)
        {
            if(i!=49)
                st+="x"+(i+1)+" "+", y"+(i+1)+" "+", z"+(i+1)+" , ";
            else
                st+="x"+(i+1)+" "+", y"+(i+1)+" "+", z"+(i+1)+" ";
        }
        db.execSQL("INSERT INTO "+tablename+"("+st+",act_label) values ("+values+")");
    }

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
