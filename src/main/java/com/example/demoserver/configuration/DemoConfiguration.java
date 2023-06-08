package com.example.demoserver.configuration;

import com.example.demoserver.analysis.StreetMeasurementsAnalyzer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableScheduling
@Slf4j
@RequiredArgsConstructor
public class DemoConfiguration {

    // TODO: define this threshold
    private final int fixedDelay = 600000; // 10 minutes
    private final StreetMeasurementsAnalyzer analyzer;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Scheduled(fixedDelay = fixedDelay)
    public void analyzeStreetMeasurements() {
        log.info("Starting analysis...");
        analyzer.analyzeStreetMeasurements();
        log.info("Analysis completed!");
    }
}
