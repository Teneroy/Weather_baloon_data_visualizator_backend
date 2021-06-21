package org.passau.visualizor.service;

import org.passau.visualizor.domain.Position;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class GpsService {

    public boolean addGpsPosition(Position position) {
        System.out.println(position);
        return true;
    }

    public List<Position> getAllPositions() {
        //
        return new ArrayList<Position>();
    }

    public List<Position> getPositionsByTimeRange(LocalDateTime from, LocalDateTime to) {
        //
        return new ArrayList<Position>();
    }

    public Position getLastPosition() {
        return new Position(LocalDateTime.now(), 23.23, 43.234, 20000);
    }
}
