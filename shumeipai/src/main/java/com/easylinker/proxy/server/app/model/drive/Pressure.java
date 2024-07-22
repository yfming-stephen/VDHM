package com.easylinker.proxy.server.app.model.drive;

/**
 * Created by ruilin on 2019/1/12.
 */
public class Pressure {
    private String low;
    private String high;

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    @Override
    public String toString() {
        return "Pressure{" +
                "low='" + low + '\'' +
                ", high='" + high + '\'' +
                '}';
    }
}
