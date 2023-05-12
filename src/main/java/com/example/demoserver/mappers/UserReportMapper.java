package com.example.demoserver.mappers;

import com.example.demoserver.dtos.UserReportDTO;
import com.example.demoserver.entities.UserReportEntity;

import java.util.List;
import java.util.stream.Collectors;

public class UserReportMapper {

    public static UserReportDTO toUserReportDTO(UserReportEntity entity) {
        return UserReportDTO.builder()
                .reportId(entity.getReportId())
                .userEmail(entity.getUserEmail())
                .severity(entity.getSeverity())
                .description(entity.getDescription())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .reportDate(entity.getReportDate())
                .build();
    }
    public static List<UserReportDTO> toUserReportDTOs(List<UserReportEntity> entities) {
        return entities.stream().map(UserReportMapper::toUserReportDTO).collect(Collectors.toList());
    }
}
