package com.example.demoserver.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "demo_street_measurements")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StreetMeasurementEntity {

    @Id
    @Column(name = "measurement_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long measurementId;
    private long ts;
    @Column(name = "user_id")
    private String userId;
    private float longitude;
    private float latitude;
    @Column(name = "accelerometer_x")
    private float accelerometerX;
    @Column(name = "accelerometer_y")
    private float accelerometerY;
    @Column(name = "accelerometer_z")
    private float accelerometerZ;

    public StreetMeasurementEntity(long ts, String userId, float longitude, float latitude, float accelerometerX, float accelerometerY, float accelerometerZ) {
        this.ts = ts;
        this.userId = userId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.accelerometerX = accelerometerX;
        this.accelerometerY = accelerometerY;
        this.accelerometerZ = accelerometerZ;
    }
}
