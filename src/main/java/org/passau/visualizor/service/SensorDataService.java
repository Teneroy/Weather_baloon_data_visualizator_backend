package org.passau.visualizor.service;

import com.mongodb.MongoWriteException;
import org.passau.visualizor.dao.SensorDataRepository;
import org.passau.visualizor.domain.DataFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SensorDataService {
    private final SensorDataRepository repository;

    @Autowired
    public SensorDataService(SensorDataRepository repository) {
        this.repository = repository;
    }

    public boolean addDataFrame(DataFrame frame) {
        try {
            repository.insert(frame);
        } catch (MongoWriteException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public List<DataFrame> getAllDataFrames() {
        return repository.findAll();
    }

    public List<DataFrame> getDataFramesByTmeRange(LocalDateTime from, LocalDateTime to) {
        return repository.findAllByDateTimeBetween(from, to);
    }

    public DataFrame getLastDataFrame() {
        Optional<DataFrame> lastFrame = repository.findFirstByOrderByDateTimeDesc();
        return lastFrame.orElse(null);
    }
}
