package com.example.spring_example.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnalyzeRunRequestDto {

    @NotBlank
    Long id;
    @NotBlank
    String kind;
    @NotBlank
    double startTime;
    @NotBlank
    double endTime;
    @NotBlank
    boolean plotEnergy;
    @NotBlank
    boolean plotSpectrum;
    @NotBlank
    boolean plotFlux;
    @NotBlank
    boolean plotFields;

}
