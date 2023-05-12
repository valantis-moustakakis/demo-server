package com.example.demoserver.repositories;

import com.example.demoserver.entities.UserAuthenticationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAuthenticationRepository extends JpaRepository<UserAuthenticationEntity, String> {
}
