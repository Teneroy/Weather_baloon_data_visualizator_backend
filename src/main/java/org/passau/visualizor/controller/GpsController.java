package org.passau.visualizor.controller;

import org.passau.visualizor.domain.Position;
import org.passau.visualizor.service.GpsService;
import org.passau.visualizor.service.PathPredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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
    public Map<String, Object> writeGpsData(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam double altitude,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime
    ) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);

        if (altitude < 0) {
            response.put("error", "altitude cannot be less than zero");
            return response;
        }
        if (longitude < 0) {
            response.put("error", "longitude cannot be less than zero");
            return response;
        }
        if (latitude < 0) {
            response.put("error", "latitude cannot be less than zero");
            return response;
        }

        if(!gpsService.addGpsPosition(new Position(dateTime, latitude, longitude, altitude))) {
            response.put("error", "An error has occurred during the writing to the database");
            return response;
        }
        response.put("success", true);
        response.put("error", "");
        return response;
    }

    @GetMapping("/getLastGpsPosition")
    public Position getLastGpsPosition() {
        return gpsService.getLastPosition();
    }

    @GetMapping("/getLastPathPrediction")
    public LinkedHashMap<?, ?> getLastPrediction() {
        Position balloonPosition = gpsService.getLastPosition();
        if(balloonPosition == null) {
            LinkedHashMap<String, Object> response = new LinkedHashMap<>();
            response.put("error", "The database is empty");
            return response;
        }
        return pathPredictionService.predictPath(balloonPosition);
    }
}
