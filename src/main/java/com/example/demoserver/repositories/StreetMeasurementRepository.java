package com.example.demoserver.repositories;

import com.example.demoserver.entities.StreetMeasurementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StreetMeasurementRepository extends JpaRepository<StreetMeasurementEntity, Long> {

    void deleteByTsLessThan(long ts);

    List<StreetMeasurementEntity> findByTsGreaterThan(long ts);

    List<StreetMeasurementEntity> findByTsGreaterThanAndTsLessThan(long min, long max);
}
