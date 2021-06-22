package org.passau.visualizor.controller;

import org.passau.visualizor.domain.DataFrame;
import org.passau.visualizor.service.SensorDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/sensors")
public class SensorDataController {

    private final SensorDataService sensorDataService;

    @Autowired
    public SensorDataController(SensorDataService sensorDataService) {
        this.sensorDataService = sensorDataService;
    }

    @GetMapping("/getAllSensorData")
    public List<DataFrame> getAllSensorData() {
        return sensorDataService.getAllDataFrames();
    }

    @GetMapping("/getSensorDataByTimeRange")
    public List<DataFrame> getSensorDataByTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
            ) {
        return sensorDataService.getDataFramesByTmeRange(from, to);
    }

}
