package org.passau.visualizor.domain;

import java.time.LocalDateTime;
import java.util.Date;

public class Position {
    private final double latitude;
    private final double longitude;
    private final double altitude;
    private final LocalDateTime dateTime;

    public Position(LocalDateTime dateTime, double latitude, double longitude, double altitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.dateTime = dateTime;
    }


    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}
