package com.example.demoserver.controllers;

import com.example.demoserver.dtos.StreetInfoDTO;
import com.example.demoserver.dtos.UserReportDTO;
import com.example.demoserver.http.*;
import com.example.demoserver.service.DemoService;
import com.example.demoserver.utils.ResponseMessages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/demo")
@RequiredArgsConstructor
@Slf4j
public class DemoController {

    private final DemoService demoService;

    @PostMapping("/registration")
    public ResponseEntity<String> registration(@RequestBody RegistrationRequest registrationRequest) {
        log.info("Registration request received for email: " + registrationRequest.getEmail());
        String message = demoService.registration(
                registrationRequest.getEmail(),
                registrationRequest.getPassword());

        return ResponseEntity.ok(message);
    }

    @PostMapping("/authentication")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        log.info("Authentication request received with email: " + authenticationRequest.getEmail());
        String message = ResponseMessages.WRONG_EMAIL_OR_PASSWORD;
        String jwt = demoService.authentication(authenticationRequest.getEmail(), authenticationRequest.getPassword());
        if (jwt != null) {
            message = ResponseMessages.OK;
            return ResponseEntity.ok(new AuthenticationResponse(jwt, message));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new AuthenticationResponse(null, message));
    }

    @PostMapping("/report")
    public ResponseEntity<String> report(@RequestBody ReportRequest reportRequest) {
        log.info("Report request received for email: " + reportRequest.getEmail());
        String message = demoService.report(
                reportRequest.getEmail(),
                reportRequest.getSeverity().name(),
                reportRequest.getDescription(),
                reportRequest.getLatitude(),
                reportRequest.getLongitude());

        return ResponseEntity.ok(message);
    }

    @GetMapping("/get-reports")
    public ResponseEntity<List<UserReportDTO>> getReports(@RequestParam String email,
                                                          @RequestParam float minLatitude,
                                                          @RequestParam float maxLatitude,
                                                          @RequestParam float minLongitude,
                                                          @RequestParam float maxLongitude) {
        log.info("Get-report request received for email: " + email);
        List<UserReportDTO> reports = demoService.getReportsInsideBox(minLatitude, maxLatitude, minLongitude, maxLongitude);

        return ResponseEntity.ok(reports);
    }

    @GetMapping("/get-street-info")
    public ResponseEntity<List<StreetInfoDTO>> getStreetInfo(@RequestParam String email,
                                                          @RequestParam float minLatitude,
                                                          @RequestParam float maxLatitude,
                                                          @RequestParam float minLongitude,
                                                          @RequestParam float maxLongitude) {
        log.info("Get-street-info request received for email: " + email);
        List<StreetInfoDTO> reports = demoService.getStreetInfoInsideBox(minLatitude, maxLatitude, minLongitude, maxLongitude);

        return ResponseEntity.ok(reports);
    }
}
