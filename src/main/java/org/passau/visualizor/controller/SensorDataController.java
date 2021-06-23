package org.passau.visualizor.controller;

import org.passau.visualizor.domain.DataFrame;
import org.passau.visualizor.service.SensorDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sensors")
public class SensorDataController {

    private final SensorDataService sensorDataService;

    @Autowired
    public SensorDataController(SensorDataService sensorDataService) {
        this.sensorDataService = sensorDataService;
    }

    @PostMapping("/writeSensorData")
    public Map<String, Object> writeSensorData(
            @RequestParam double temperature,
            @RequestParam double pressure,
            @RequestParam double humidity,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime
    ) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);

        if(!sensorDataService.addDataFrame(new DataFrame(dateTime, temperature, humidity, pressure))) {
            response.put("error", "An error has occurred during the writing to the database");
            return response;
        }

        response.put("success", true);
        response.put("error", "");
        return response;
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


    @GetMapping("/getLastDataFrame")
    public DataFrame getLastDataFrame() {
        return sensorDataService.getLastDataFrame();
    }
}
