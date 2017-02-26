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
    private static final int DATABASE_VERSION=2;
    private static final String DATABASE_NAME="Group28";
    private static final String DATABASE_TABLE="Name_ID_Age_Sex";
    private static final String KEY_ID="timestamp";
    private static final String COORDINATE1="X";
    private static final String COORDINATE2="Y";
    private static final String COORDINATE3="Z";

    public void addCoordinates(AccelorometerReading ar, String  tablename){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(KEY_ID,ar.getTimestamp());
        values.put(COORDINATE1,ar.getX());
        values.put(COORDINATE2,ar.getY());
        values.put(COORDINATE3,ar.getZ());
        db.insert(tablename,null,values);
    }

//    public List<AccelorometerReading> getReadings(){
//        List<AccelorometerReading> ar = new ArrayList<AccelorometerReading>();
//    }

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
//        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + DATABASE_TABLE + "("
//                + KEY_ID + " TIMESTAMP PRIMARY KEY," + COORDINATE1 + " TEXT,"
//                + COORDINATE2+ " TEXT, "+ COORDINATE3+ " TEXT)";
//        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        // Drop older table if existed
//        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
//
//        // Create tables again
//        onCreate(db);
    }
}
