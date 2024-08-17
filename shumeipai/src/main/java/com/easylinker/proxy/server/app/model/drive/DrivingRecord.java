package com.easylinker.proxy.server.app.model.drive;

/**
 * Created by ruilin on 2019/1/12.
 */
public class DrivingRecord {
    private GPS gps;
    private LBS lbs;
    private String speed;
    private String bodyTemp;
    private String hash;

    public GPS getGps() {
        return gps;
    }

    public void setGps(GPS gps) {
        this.gps = gps;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public LBS getLbs() {
        return lbs;
    }

    public void setLbs(LBS lbs) {
        this.lbs = lbs;
    }

    public String getBodyTemp() {
        return bodyTemp;
    }

    public void setBodyTemp(String bodyTemp) {
        this.bodyTemp = bodyTemp;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @Override
    public String toString() {
        return "DrivingRecord{" +
                "  gps=" + gps +
                ", speed='" + speed + '\'' +
                ", bodyTemp='" + bodyTemp + '\'' +
                ", hash='" + hash + '\'' +
                '}';
    }
}
