package com.example.spring_example.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataUploadDto {

    @NotNull
    private String kind;
    @NotNull
    private int dimension;
    @NotNull
    private String resolution;
    private int tInitial;
    private int tFinal;
    @NotNull
    private String downloadPath;
    @NotNull
    private String initialCondition;
    @NotNull
    private String description;
    private double viscosity;
    private double magneticDiffusivity;
    private double rayleighNumber;
    private double prandtlNumber;

}
