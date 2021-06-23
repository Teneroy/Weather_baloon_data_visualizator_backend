package org.passau.visualizor.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Document
public class DataFrame {
    @Id
    private String id;
    private final LocalDateTime dateTime;
    private final double temperature;
    private final double humidity;
    private final double pressure;

    public DataFrame(LocalDateTime dateTime, double temperature, double humidity, double pressure) {
        this.dateTime = dateTime;
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
    }


    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getPressure() {
        return pressure;
    }

}
