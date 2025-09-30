package com.example.spring_example.service;

import com.example.spring_example.dto.request.UserSignupRequestDto;
import com.example.spring_example.entity.AppUser;
import com.example.spring_example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Boolean createUser(UserSignupRequestDto userSignupDetails) {
        try {
            String email = userSignupDetails.getEmail();
            String password = userSignupDetails.getPassword();
            AppUser appUser = new AppUser();
            appUser.setUsername(userSignupDetails.getUsername());
            appUser.setEmail(email);
            appUser.setPassword(password);
            appUser.setLicensed(false);
            appUser.setCreatedDate(ZonedDateTime.now());
            userRepository.save(appUser);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void updateUser(AppUser user) {
        userRepository.save(user);

    }

    public Optional<AppUser> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<AppUser> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public String generateOTP(AppUser user) {
        Random random = new Random();
        String otp = String.valueOf(100000 + random.nextInt(900000));
        user.setOtp(otp);
        user.setOtpExpiryDate(LocalDateTime.now().plusMinutes(10));
        userRepository.save(user);
        return otp;
    }

    public boolean verifyOTP(AppUser user,String otp) {
        if(user.getOtpExpiryDate().isBefore(LocalDateTime.now())) {
            return false;
        }

        String storedOtp = user.getOtp();
        if(storedOtp.equals(otp)) {
            user.setOtp(null);
            user.setOtpExpiryDate(null);
            return true;
        }

        return false;
    }
}
