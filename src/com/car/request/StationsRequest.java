package com.car.request;

import java.io.Serializable;

/**
 * @author jhyang
 * @create 2022-03-30 9:53
 */
public class StationsRequest implements Serializable {
    private static final long serialVersionUID = -2982449064342053945L;

    private double longitude;
    private double latitude;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
