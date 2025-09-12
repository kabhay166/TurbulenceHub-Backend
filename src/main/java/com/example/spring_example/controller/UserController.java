package com.example.spring_example.controller;

import com.example.spring_example.dto.request.UserSignupRequestDto;
import com.example.spring_example.dto.response.UserResponseDto;
import com.example.spring_example.entity.User;
import com.example.spring_example.security.JwtUtil;
import com.example.spring_example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/user")
@CrossOrigin(origins = "*")//"http://localhost:5173")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;


    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

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
        Optional<User> user = userService.findByUsername(username);

        if(user.isEmpty()){
            return ResponseEntity.badRequest().body(new UserResponseDto("","","","No user found with username " + username));
        } else {
            password = bCryptPasswordEncoder.encode(password);
            if(!user.get().getPassword().equals(password)) {
                return ResponseEntity.badRequest().body(new UserResponseDto("","","","Incorrect credentials provided" + username));
            }


        }

        System.out.println("Username: " + username + " password: " + password);
        UserResponseDto userResponseDto = new UserResponseDto(username,"","","");
        return new ResponseEntity<>(userResponseDto,HttpStatus.OK);
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

        Optional<User> existingUser = userService.findByUsername(username);

        if(existingUser.isPresent()) {
            return ResponseEntity.badRequest().body(new UserResponseDto(username,email,"","Username " + username + " is already taken."));
        }

        existingUser = userService.findByEmail(email);
        if(existingUser.isPresent()) {
            return ResponseEntity.badRequest().body(new UserResponseDto(username,email,"","User with email: " + email + " already exists."));
        }


        password = bCryptPasswordEncoder.encode(password);
        userSignupDetails.setPassword(password);
        Boolean userCreated = userService.createUser(userSignupDetails);




        UserResponseDto userSignupResponseDto = new UserResponseDto(username,email,"","");
        return new ResponseEntity<>(userSignupResponseDto, HttpStatus.CREATED);
    }
}
