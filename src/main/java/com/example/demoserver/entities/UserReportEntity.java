package com.example.demoserver.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "demo_user_reports")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserReportEntity {

    @Id
    @Column(name = "report_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    @Column(name = "user_email")
    private String userEmail;

    private String severity;

    private String description;

    private float latitude;

    private float longitude;

    @Column(name = "report_date")
    private Date reportDate;
}
