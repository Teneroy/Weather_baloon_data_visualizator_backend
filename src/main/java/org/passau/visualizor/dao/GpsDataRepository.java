package org.passau.visualizor.dao;

import org.passau.visualizor.domain.Position;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface GpsDataRepository extends MongoRepository<Position, String> {
    List<Position> findAllByDateTimeBetween(LocalDateTime from, LocalDateTime to);

    Optional<Position> findFirstByOrderByDateTimeDesc();
}
