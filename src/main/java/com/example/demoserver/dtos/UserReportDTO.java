package com.example.demoserver.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class UserReportDTO {

    private Long reportId;
    private String userEmail;
    private String severity;
    private String description;
    private float latitude;
    private float longitude;
    private Date reportDate;
}
