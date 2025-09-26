package com.example.spring_example.repository;

import com.example.spring_example.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {

    public Optional<AppUser> findByUsername(String username);

    public Optional<AppUser> findByEmail(String email);
}
