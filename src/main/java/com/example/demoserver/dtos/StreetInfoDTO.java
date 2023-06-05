package com.example.demoserver.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StreetInfoDTO {

    private Long infoId;
    private String severity;
    private float latitude;
    private float longitude;
}
