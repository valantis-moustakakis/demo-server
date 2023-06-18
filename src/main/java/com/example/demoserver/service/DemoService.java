package com.example.demoserver.service;

import com.example.demoserver.dtos.StreetInfoDTO;
import com.example.demoserver.dtos.UserReportDTO;
import com.example.demoserver.entities.StreetInfoEntity;
import com.example.demoserver.entities.UserAuthenticationEntity;
import com.example.demoserver.entities.UserReportEntity;
import com.example.demoserver.exceptions.UserAlreadyExistException;
import com.example.demoserver.mappers.StreetInfoMapper;
import com.example.demoserver.mappers.UserReportMapper;
import com.example.demoserver.repositories.StreetInfoRepository;
import com.example.demoserver.repositories.UserAuthenticationRepository;
import com.example.demoserver.repositories.UserReportRepository;
import com.example.demoserver.utils.ResponseMessages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DemoService {

    private final UserAuthenticationRepository userAuthenticationRepository;
    private final UserReportRepository userReportRepository;
    private final StreetInfoRepository streetInfoRepository;
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public String registration(String username, String password) throws UserAlreadyExistException {
        if (emailExists(username)) {
            throw new UserAlreadyExistException("There is an account with that email address: " + username);
        }

        UserAuthenticationEntity userAuthenticationEntity = new UserAuthenticationEntity();
        userAuthenticationEntity.setEmail(username);
        userAuthenticationEntity.setPassword(passwordEncoder.encode(password));
        userAuthenticationEntity.setEnabled(true);
        userAuthenticationRepository.save(userAuthenticationEntity);

        return ResponseMessages.OK;
    }

    public String authentication(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException ex) {
            return null;
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return jwtService.generateToken(userDetails);
    }

    public String report(String email, String severity, String description, float latitude, float longitude) {
        UserReportEntity entity = new UserReportEntity();
        entity.setUserEmail(email);
        entity.setSeverity(severity);
        entity.setDescription(description);
        entity.setLatitude(latitude);
        entity.setLongitude(longitude);
        entity.setReportDate(new Date());
        userReportRepository.save(entity);
        return ResponseMessages.OK;
    }

    public List<UserReportDTO> getReportsInsideBox(float minLat, float maxLat, float minLon, float maxLon) {
        List<UserReportEntity> reports = userReportRepository.findReportsWithinBoundingBox(minLat, maxLat, minLon, maxLon);
        return UserReportMapper.toUserReportDTOs(reports);
    }

    public List<StreetInfoDTO> getStreetInfoInsideBox(float minLat, float maxLat, float minLon, float maxLon) {
        List<StreetInfoEntity> streetInfo = streetInfoRepository.findStreetInfoWithinBoundingBox(minLat, maxLat, minLon, maxLon);
        return StreetInfoMapper.toStreetInfoDTOs(streetInfo);
    }

    private boolean emailExists(String email) {
        return userAuthenticationRepository.findById(email).isPresent();
    }

}
