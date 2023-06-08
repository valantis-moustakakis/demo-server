package com.example.demoserver.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class StreetMeasurementDTO {
    private long ts;
    private String user;
    private float lon;
    private float lat;
    private float x;
    private float y;
    private float z;
}
