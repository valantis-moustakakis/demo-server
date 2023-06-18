package com.example.demoserver.repositories;

import com.example.demoserver.entities.StreetInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StreetInfoRepository extends JpaRepository<StreetInfoEntity, Long> {

    @Query("SELECT s FROM StreetInfoEntity s WHERE s.latitude BETWEEN :minLat AND :maxLat AND s.longitude BETWEEN :minLon AND :maxLon")
    List<StreetInfoEntity> findStreetInfoWithinBoundingBox(@Param("minLat") float minLat, @Param("maxLat") float maxLat, @Param("minLon") float minLon, @Param("maxLon") float maxLon);

    StreetInfoEntity findByLatitudeAndLongitude(float latitude, float longitude);

}
