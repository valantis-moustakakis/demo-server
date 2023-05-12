package com.example.demoserver.repositories;

import com.example.demoserver.entities.UserReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserReportRepository extends JpaRepository<UserReportEntity, Long> {

    @Query("SELECT u FROM UserReportEntity u WHERE u.latitude BETWEEN :minLat AND :maxLat AND u.longitude BETWEEN :minLon AND :maxLon")
    List<UserReportEntity> findReportsWithinBoundingBox(@Param("minLat") float minLat, @Param("maxLat") float maxLat, @Param("minLon") float minLon, @Param("maxLon") float maxLon);

}

