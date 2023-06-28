package com.example.demoserver.configuration;

import com.example.demoserver.analysis.StreetMeasurementsAnalyzer;
import com.example.demoserver.entities.StreetMeasurementEntity;
import com.example.demoserver.repositories.StreetMeasurementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Configuration
@EnableTransactionManagement
@EnableScheduling
@Slf4j
@RequiredArgsConstructor
@Transactional
public class DemoConfiguration {
    private final StreetMeasurementsAnalyzer analyzer;
    private final StreetMeasurementRepository streetMeasurementRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Scheduled(fixedDelayString = "${analysis.threshold}")
    public void analyzeStreetMeasurements() {
        log.info("Starting analysis...");
//        long currentMillis = new Date().getTime();
        analyzer.analyzeStreetMeasurements();
//        streetMeasurementRepository.deleteByTsLessThan(currentMillis);
        log.info("Analysis completed!");
    }

//    @Scheduled(fixedDelayString = "${analysis.threshold}")
    public void getData() {
        log.info("Starting analysis...");
        List<StreetMeasurementEntity> entities = streetMeasurementRepository.findByTsGreaterThanAndTsLessThan(1687883700000L, 1687883760000L);
        StringBuilder strbx = new StringBuilder();
        StringBuilder strby = new StringBuilder();
        StringBuilder strbz = new StringBuilder();
        for (StreetMeasurementEntity entity : entities) {
            String x = "{\"x\":" + entity.getTs() + ",\"y\":" + entity.getAccelerometerX() + "},\n";
            strbx.append(x);

            String y = "{\"x\":" + entity.getTs() + ",\"y\":" + entity.getAccelerometerY() + "},\n";
            strby.append(y);

            String z = "{\"x\":" + entity.getTs() + ",\"y\":" + entity.getAccelerometerZ() + "},\n";
            strbz.append(z);
        }
        log.info("X " + strbx);
        log.info("Y " + strby);
        log.info("Z " + strbz);

        log.info("Analysis completed!");
    }
}
