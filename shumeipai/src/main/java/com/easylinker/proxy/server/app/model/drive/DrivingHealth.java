package com.easylinker.proxy.server.app.model.drive;

/**
 * Created by ruilin on 2019/1/15.
 */
public class DrivingHealth {
    private Pressure pressure;
    private String heartRate;
    private String hash;

    public Pressure getPressure() {
        return pressure;
    }

    public void setPressure(Pressure pressure) {
        this.pressure = pressure;
    }

    public String getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(String heartRate) {
        this.heartRate = heartRate;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hsah) {
        this.hash = hsah;
    }

    @Override
    public String toString() {
        return "DrivingHealth{" +
                "pressure=" + pressure +
                ", heartRate='" + heartRate + '\'' +
                ", hash='" + hash + '\'' +
                '}';
    }
}
