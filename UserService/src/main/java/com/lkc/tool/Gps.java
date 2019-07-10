package com.lkc.tool;

public class Gps {

    private double wgLat;
    private double wgLon;

    private Gps() {
    }

    private static final Gps GPS = new Gps();

    public static Gps getIntance() {
        return GPS;
    }

    public double getWgLat() {
        return wgLat;
    }

    public void setWgLat(double wgLat) {
        this.wgLat = wgLat;
    }

    public double getWgLon() {
        return wgLon;
    }

    public void setWgLon(double wgLon) {
        this.wgLon = wgLon;
    }

}
