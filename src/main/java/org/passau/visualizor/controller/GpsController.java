package org.passau.visualizor.controller;

import org.passau.visualizor.domain.Position;
import org.passau.visualizor.service.GpsService;
import org.passau.visualizor.service.PathPredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/gps")
public class GpsController {
    private final GpsService gpsService;
    private final PathPredictionService pathPredictionService;

    @Autowired
    public GpsController(GpsService gpsService, PathPredictionService pathPredictionService) {
        this.gpsService = gpsService;
        this.pathPredictionService = pathPredictionService;
    }

    @PostMapping("/writeGpsData")
    public String writeGpsData(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam double altitude,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime
    ) {
        if (altitude < 0)
            return "altitude cannot be less than zero";
        if (longitude < 0)
            return "longitude cannot be less than zero";
        if (latitude < 0)
            return "latitude cannot be less than zero";

        if(!gpsService.addGpsPosition(new Position(dateTime, latitude, longitude, altitude))) {
            return "error";
        }

        return "success";
    }

    @GetMapping("/getLastGpsPosition")
    public Position getLastGpsPosition() {
        return gpsService.getLastPosition();
    }

    @GetMapping("/getLastPathPrediction")
    public LinkedHashMap<?, ?> getLastPrediction() {
        Position balloonPosition = gpsService.getLastPosition();

        return pathPredictionService.predictPath(balloonPosition);
    }
}
