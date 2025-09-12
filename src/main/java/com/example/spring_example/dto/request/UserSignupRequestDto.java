package com.example.spring_example.dto.request;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignupRequestDto {
    @NotBlank
    public String username;
    @NotBlank
    public String password;
    @NotBlank
    public String confirmPassword;
    @NotBlank
    @Email
    public String email;
}
