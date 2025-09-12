package com.example.spring_example.entity.data;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ScalarData extends BaseData{

    @NotNull
    private double viscosity;
    @NotNull
    private double criticalRayleighNumber;
    @NotNull
    private double rayleighNumber;
    @NotNull
    private double prandtlNumber;
    @NotNull
    private double thermalDiffusivity;
}
