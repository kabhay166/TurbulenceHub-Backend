package com.example.spring_example.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CompletedRunResponseDto {

    @NotBlank
    public Long id;
    @NotBlank
    public String kind;
    @NotBlank
    public int dimension;
    @NotBlank
    public String resolution;
    @NotBlank
    public ZonedDateTime timeOfRun;


}
