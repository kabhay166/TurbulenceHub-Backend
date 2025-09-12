package com.example.spring_example.service;

import com.example.spring_example.dto.request.UserSignupRequestDto;
import com.example.spring_example.entity.User;
import com.example.spring_example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Boolean createUser(UserSignupRequestDto userSignupDetails) {
        try {
            String email = userSignupDetails.getEmail();
            String password = userSignupDetails.getPassword();
            User user = new User();
            user.setUsername(userSignupDetails.getUsername());
            user.setEmail(email);
            user.setPassword(password);
            user.setLicensed(false);
            user.setCreatedDate(ZonedDateTime.now());
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
