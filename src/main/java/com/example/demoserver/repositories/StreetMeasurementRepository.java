package com.example.demoserver.repositories;

import com.example.demoserver.entities.StreetMeasurementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StreetMeasurementRepository extends JpaRepository<StreetMeasurementEntity, Long> {
}
