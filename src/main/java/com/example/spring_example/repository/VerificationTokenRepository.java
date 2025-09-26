package com.example.spring_example.repository;

import com.example.spring_example.entity.AppUser;
import com.example.spring_example.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken,Long> {

    Optional<VerificationToken> findByToken(String token);
    Optional<VerificationToken> findByUser(AppUser user);
}
