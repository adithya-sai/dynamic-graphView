package com.example.adithyasai.graphgen;

/**
 * Created by adithyasai on 2/22/17.
 */

public class AccelorometerReading {
    private String timestamp;
    private double x;
    private double y;
    private double z;
    public AccelorometerReading(){

    }

    public AccelorometerReading(String timestamp, double x, double y, double z) {
        this.timestamp = timestamp;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }
}
