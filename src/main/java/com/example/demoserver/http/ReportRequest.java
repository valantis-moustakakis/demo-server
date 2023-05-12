package com.example.demoserver.http;

import com.example.demoserver.utils.IncidentSeverity;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportRequest {

    @NotNull
    private String email;
    @NotNull
    private IncidentSeverity severity;
    @NotNull
    private String description;
    @NotNull
    private float latitude;
    @NotNull
    private float longitude;
}
