package com.example.spring_example.security;

import com.example.spring_example.entity.AppUser;
import com.example.spring_example.service.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = userService.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: "+ username));
        return  User.builder()
                .username( appUser.getUsername())
                .password( appUser.getPassword())
                .roles( appUser.getRoles().toArray(new String[0]))
                .build();
    }
}
