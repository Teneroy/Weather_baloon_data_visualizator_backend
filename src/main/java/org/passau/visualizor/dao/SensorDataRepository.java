package org.passau.visualizor.dao;

import org.passau.visualizor.domain.DataFrame;
import org.springframework.data.mongodb.repository.MongoRepository;

import javax.xml.crypto.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SensorDataRepository extends MongoRepository<DataFrame, String> {
    Optional<DataFrame> findFirstByOrderByDateTimeDesc();

    List<DataFrame> findAllByDateTimeBetween(LocalDateTime from, LocalDateTime to);

    List<DataFrame> findAllByDateTime(LocalDateTime dateTime);
}
