package com.example.demoserver.service;

import com.example.demoserver.entities.UserAuthenticationEntity;
import com.example.demoserver.repositories.UserAuthenticationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserAuthenticationRepository repository;

    /**
     * Retrieves user details for the given email
     *
     * @param email the email of the user
     * @return      the user details
     * @throws UsernameNotFoundException
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserAuthenticationEntity> optional = repository.findById(email);
        UserAuthenticationEntity user = optional.orElse(null);
        if (user == null || user.getEmail() == null) {
            throw new UsernameNotFoundException("No user found with username: " + email);
        }

        return new User(
                user.getEmail(),
                user.getPassword(),
                user.isEnabled(),
                user.isAccountNonExpired(),
                user.isCredentialsNonExpired(),
                user.isAccountNonLocked(),
                new ArrayList<>());
    }
}
