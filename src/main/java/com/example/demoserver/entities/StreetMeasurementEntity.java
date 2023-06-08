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
    private float longitude;
    private float latitude;
    private float accelerometer_x;
    private float accelerometer_y;
    private float accelerometer_z;

    public StreetMeasurementEntity(long ts, float longitude, float latitude, float accelerometer_x, float accelerometer_y, float accelerometer_z) {
        this.ts = ts;
        this.longitude = longitude;
        this.latitude = latitude;
        this.accelerometer_x = accelerometer_x;
        this.accelerometer_y = accelerometer_y;
        this.accelerometer_z = accelerometer_z;
    }
}
