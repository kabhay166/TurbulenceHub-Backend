package com.example.spring_example.entity.data;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompressibleConvectionData extends BaseData {
    @NotNull
    private double aspectRatio;
    @NotNull
    private double epsilon;
    @NotNull
    private double dissipationNumber;
    @NotNull
    private double gamma;
    @NotNull
    private double prandtlNumber;
    @NotNull
    private double rayleighNumber;
}
