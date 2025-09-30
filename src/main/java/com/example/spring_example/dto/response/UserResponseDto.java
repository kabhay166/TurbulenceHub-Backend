package com.example.spring_example.dto.response;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    @NotBlank
    public String username;
    @NotBlank
    public String email;
    @NotBlank
    public String token;
    public String error;
    public boolean showOTPPage;
}
