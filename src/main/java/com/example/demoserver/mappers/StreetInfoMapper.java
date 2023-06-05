package com.example.demoserver.mappers;

import com.example.demoserver.dtos.StreetInfoDTO;
import com.example.demoserver.entities.StreetInfoEntity;

import java.util.List;
import java.util.stream.Collectors;

public class StreetInfoMapper {

    public static StreetInfoDTO toStreetInfoDTO(StreetInfoEntity entity) {
        return StreetInfoDTO.builder()
                .infoId(entity.getInfoId())
                .severity(entity.getSeverity())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .build();
    }
    public static List<StreetInfoDTO> toStreetInfoDTOs(List<StreetInfoEntity> entities) {
        return entities.stream().map(StreetInfoMapper::toStreetInfoDTO).collect(Collectors.toList());
    }
}
