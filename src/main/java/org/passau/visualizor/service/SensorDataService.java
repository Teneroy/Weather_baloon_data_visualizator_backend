package org.passau.visualizor.service;

import org.passau.visualizor.domain.DataFrame;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SensorDataService {

    public List<DataFrame> getAllDataFrames() {
        //database call
        return new ArrayList<>();
    }

    public List<DataFrame> getDataFramesByTmeRange(LocalDateTime from, LocalDateTime to) {
        //database call
        return new ArrayList<>();
    }

    public DataFrame getLastDataFrame() {
        //database call
        return new DataFrame(LocalDateTime.now(), 23.123, 234.12, 234.123);
    }
}
