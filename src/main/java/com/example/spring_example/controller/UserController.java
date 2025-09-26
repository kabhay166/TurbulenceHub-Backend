package com.example.spring_example.controller;

import com.example.spring_example.dto.request.UserSignupRequestDto;
import com.example.spring_example.dto.response.UserResponseDto;
import com.example.spring_example.entity.AppUser;
import com.example.spring_example.security.CustomUserDetailsService;
import com.example.spring_example.security.JwtUtil;
import com.example.spring_example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username,password)
            );
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            String token = jwtUtil.generateToken(username);
            UserResponseDto userResponseDto = new UserResponseDto(username,"",token,"");
            return new ResponseEntity<>(userResponseDto,HttpStatus.OK);
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(new UserResponseDto("","","","Incorrect credentials provided" + username));
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
            return ResponseEntity.badRequest().body(new UserResponseDto(username,email,"","The passwords do not match"));
        }

        Optional<AppUser> existingUser = userService.findByUsername(username);

        if(existingUser.isPresent()) {
            return ResponseEntity.badRequest().body(new UserResponseDto(username,email,"","Username " + username + " is already taken."));
        }

        existingUser = userService.findByEmail(email);
        if(existingUser.isPresent()) {
            return ResponseEntity.badRequest().body(new UserResponseDto(username,email,"","User with email: " + email + " already exists."));
        }


        password = passwordEncoder.encode(password);
        userSignupDetails.setPassword(password);
        Boolean userCreated = userService.createUser(userSignupDetails);




        UserResponseDto userSignupResponseDto = new UserResponseDto(username,email,"","");
        return new ResponseEntity<>(userSignupResponseDto, HttpStatus.CREATED);
    }
}
