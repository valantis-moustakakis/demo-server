package com.example.demoserver.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "demo_street_info")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StreetInfoEntity {

    @Id
    @Column(name = "info_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long infoId;
    private String severity;
    private float latitude;
    private float longitude;

    public StreetInfoEntity(String severity, float latitude, float longitude) {
        this.severity = severity;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
