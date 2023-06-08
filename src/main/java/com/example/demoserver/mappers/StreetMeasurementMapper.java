package com.example.demoserver.mappers;

import com.example.demoserver.dtos.StreetMeasurementDTO;
import com.example.demoserver.entities.StreetMeasurementEntity;

import java.util.List;
import java.util.stream.Collectors;

public class StreetMeasurementMapper {

    public static StreetMeasurementEntity toStreetMeasurementEntity(StreetMeasurementDTO dto) {
        return new StreetMeasurementEntity(dto.getTs(), dto.getUser(), dto.getLon(), dto.getLat(), dto.getX(), dto.getY(), dto.getZ());
    }

    public static List<StreetMeasurementEntity> toStreetMeasurementEntities(List<StreetMeasurementDTO> dtos) {
        return dtos.stream().map(StreetMeasurementMapper::toStreetMeasurementEntity).collect(Collectors.toList());
    }
}
