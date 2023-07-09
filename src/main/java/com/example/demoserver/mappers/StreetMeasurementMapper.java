package com.example.demoserver.mappers;

import com.example.demoserver.dtos.StreetMeasurementDTO;
import com.example.demoserver.entities.StreetMeasurementEntity;

public class StreetMeasurementMapper {

    public static StreetMeasurementEntity toStreetMeasurementEntity(StreetMeasurementDTO dto) {
        return new StreetMeasurementEntity(dto.getTs(), dto.getUser(), dto.getLon(), dto.getLat(), dto.getX(), dto.getY(), dto.getZ());
    }
}
