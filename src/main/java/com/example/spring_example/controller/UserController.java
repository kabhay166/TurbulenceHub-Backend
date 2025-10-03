package com.example.spring_example.controller;

import com.example.spring_example.dto.request.UserSignupRequestDto;
import com.example.spring_example.dto.response.UserResponseDto;
import com.example.spring_example.entity.AppUser;
import com.example.spring_example.entity.PasswordResetToken;
import com.example.spring_example.entity.VerificationToken;
import com.example.spring_example.repository.PasswordResetTokenRepository;
import com.example.spring_example.repository.VerificationTokenRepository;
import com.example.spring_example.security.CustomUserDetailsService;
import com.example.spring_example.security.JwtUtil;
import com.example.spring_example.service.EmailService;
import com.example.spring_example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmailService emailService;

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private PasswordResetTokenRepository passwordRestTokenRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String getUserLogin() {
        return "forward:/index.html";
    }

    @GetMapping("/signup")
    public String userSignup() {
        return "forward:/index.html";
    }

    @ResponseBody
    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> postUserLogin(@RequestBody Map<String,String> userLoginDetails) {
        String username = userLoginDetails.get("username");
        String password = userLoginDetails.get("password");
        String incomingOtp = userLoginDetails.get("otp");

        Optional<AppUser> user = userService.findByUsername(username);



        if(user.isEmpty()) {
            return ResponseEntity.badRequest().body(new UserResponseDto("","","","Incorrect credentials provided. No user with username: " + username,false));
        }

        if (!user.get().isVerified()) {
            Optional<VerificationToken> token = tokenRepository.findByUser(user.get());
            if (token.isPresent()) {
                if (!token.get().getExpiryDate().isBefore(LocalDateTime.now())) {
                    tokenRepository.delete(token.get());
                }

                VerificationToken newToken = new VerificationToken(user.get());
                emailService.sendVerificationEmail(user.get().getEmail(), newToken.getToken());

            }

            return ResponseEntity.badRequest().body(new UserResponseDto("", "", "", "Please Verify your email first. A verification link has been sent to your mail.",false));
        }

        if(incomingOtp == null) {
            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(username,password)
                );
                UserResponseDto userResponseDto = new UserResponseDto(username,"","","",true);
                String otp = userService.generateOTP(user.get());
                emailService.sendOTPMail(user.get().getEmail(),otp);
                return new ResponseEntity<>(userResponseDto,HttpStatus.OK);
            } catch(Exception e) {
                return ResponseEntity.badRequest().body(new UserResponseDto("","","","Invalid Credentials provided",false));
            }

        } else {
            boolean isOTPCorrect =  userService.verifyOTP(user.get(),incomingOtp);

            if(isOTPCorrect) {
                try {
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(username,password)
                    );
                    String token = jwtUtil.generateToken(username);
                    UserResponseDto userResponseDto = new UserResponseDto(username,"",token,"",false);
                    return new ResponseEntity<>(userResponseDto,HttpStatus.OK);
                } catch(Exception e) {
                    return ResponseEntity.badRequest().body(new UserResponseDto("","","","Incorrect credentials provided.",false));
                }
            } else {
                return ResponseEntity.badRequest().body(new UserResponseDto("","","","Incorrect otp or expired",true));

            }
        }



    }



    @ResponseBody
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserSignupRequestDto userSignupDetails) {

        String username = userSignupDetails.getUsername();
        String email = userSignupDetails.getEmail();
        String password = userSignupDetails.getPassword();
        String confirmPassword = userSignupDetails.getConfirmPassword();
        if(!password.equals(confirmPassword)) {
            return ResponseEntity.badRequest().body(new UserResponseDto(username,email,"","The passwords do not match",false));
        }

        Optional<AppUser> existingUser = userService.findByUsername(username);

        if(existingUser.isPresent()) {
            return ResponseEntity.badRequest().body(new UserResponseDto(username,email,"","Username " + username + " is already taken.",false));
        }

        existingUser = userService.findByEmail(email);
        if(existingUser.isPresent()) {
            return ResponseEntity.badRequest().body(new UserResponseDto(username,email,"","User with email: " + email + " already exists.",false));
        }


        password = passwordEncoder.encode(password);
        userSignupDetails.setPassword(password);
        Boolean userCreated = userService.createUser(userSignupDetails);
        Optional<AppUser>  user = userService.findByUsername(username);

        if(user.isEmpty()) {
            return ResponseEntity.badRequest().body(new UserResponseDto(username,email,"","Error creating the user.",false));
        }

        VerificationToken verificationToken = new VerificationToken(user.get());
        tokenRepository.save(verificationToken);

        emailService.sendVerificationEmail(email,verificationToken.getToken());
        UserResponseDto userSignupResponseDto = new UserResponseDto(username,email,"","",false);

        return new ResponseEntity<>(userSignupResponseDto, HttpStatus.CREATED);
    }


    @GetMapping("/signup/verify")
    public ResponseEntity<String> verifyUserEmail(@RequestParam String token) {
        Optional<VerificationToken> verificationToken = tokenRepository.findByToken(token);

        if(verificationToken.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid verification link.");
        }

        if(verificationToken.get().getExpiryDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("Verification link has expired");
        }

        AppUser user = verificationToken.get().getUser();
        user.setVerified(true);
        userService.updateUser(user);
        tokenRepository.delete(verificationToken.get());
        return ResponseEntity.ok().body("Your mail has been verified!. You can now log in.");
    }

    @GetMapping("/reset-password")
    public String getResetPassword() {
        return "forward:/index.html";
    }


    @PostMapping("/reset-password")
    public ResponseEntity<Map<String,Object>> resetPassword(@RequestBody Map<String,String> passwordResetRequestBody) {
        Map<String,Object> responseKvp = new HashMap<>();
        responseKvp.put("success",false);
        responseKvp.put("error","Internal server error");
        String email = passwordResetRequestBody.get("email");


        if(email != null) {
            Optional<AppUser> user = userService.findByEmail(email);

            if(user.isEmpty()) {
                responseKvp.put("error","The user with this email does not exist.");
                return ResponseEntity.badRequest().body(responseKvp);
            }

            Optional<PasswordResetToken> existingToken = passwordRestTokenRepository.findByUser(user.get());

            existingToken.ifPresent(passwordResetToken -> passwordRestTokenRepository.delete(passwordResetToken));

            PasswordResetToken newPasswordResetToken = new PasswordResetToken(user.get());

            passwordRestTokenRepository.save(newPasswordResetToken);
            String token = newPasswordResetToken.getToken();

            emailService.sendPasswordResetEmail(user.get().getEmail(),token);

            responseKvp.put("success",true);
            responseKvp.put("error","");
            return ResponseEntity.ok().body(responseKvp);
        } else {
            String token = passwordResetRequestBody.get("token");
            String password = passwordResetRequestBody.get("password");
            String confirmPassword = passwordResetRequestBody.get("confirmPassword");

            if(token == null || password == null || confirmPassword == null || token.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                responseKvp.put("success",false);
                responseKvp.put("error","Some values are empty");
                return ResponseEntity.badRequest().body(responseKvp);
            }

            if(!password.equals(confirmPassword)) {
                responseKvp.put("success",false);
                responseKvp.put("error","The passwords do not match");
                return ResponseEntity.badRequest().body(responseKvp);
            }


            Optional<PasswordResetToken> resetToken = passwordRestTokenRepository.findByToken(token);
            if(resetToken.isEmpty()) {
                responseKvp.put("success",false);
                responseKvp.put("error","An error occurred.");
                return ResponseEntity.badRequest().body(responseKvp);
            }

            if(resetToken.get().getExpiryDate().isBefore(LocalDateTime.now())) {
                responseKvp.put("success",false);
                responseKvp.put("error","The reset password link is expired.");
                return ResponseEntity.badRequest().body(responseKvp);
            }

            AppUser user = resetToken.get().getUser();
            user.setPassword(passwordEncoder.encode(password));
            passwordRestTokenRepository.delete(resetToken.get());
            responseKvp.put("success",true);
            responseKvp.put("error","");
            return ResponseEntity.ok().body(responseKvp);

        }


    }
}
