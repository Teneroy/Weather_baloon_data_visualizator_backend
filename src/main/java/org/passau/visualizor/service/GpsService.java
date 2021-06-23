package org.passau.visualizor.service;

import com.mongodb.MongoWriteException;
import org.passau.visualizor.dao.GpsDataRepository;
import org.passau.visualizor.domain.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GpsService {

    private final GpsDataRepository repository;

    @Autowired
    public GpsService(GpsDataRepository repository) {
        this.repository = repository;
    }

    public boolean addGpsPosition(Position position) {
        try {
            repository.insert(position);
        } catch (MongoWriteException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public List<Position> getAllPositions() {
        return repository.findAll();
    }

    public List<Position> getPositionsByTimeRange(LocalDateTime from, LocalDateTime to) {
        return repository.findAllByDateTimeBetween(from, to);
    }

    public Position getLastPosition() {
        return repository.findFirstByOrderByDateTimeDesc().orElse(null);
    }
}
