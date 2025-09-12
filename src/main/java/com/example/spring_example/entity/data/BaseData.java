package com.example.spring_example.entity.data;

import com.example.spring_example.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.time.ZonedDateTime;

@MappedSuperclass
@Getter
@Setter
public class BaseData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    private int dimension;
    @NotBlank
    private String resolution;
    @NotBlank
    private String initialCondition;
    @NotNull
    private double tInitial;
    @NotNull
    private double tFinal;
    @NotBlank
    private String downloadPath;
    private String description;
    @ManyToOne
    private User uploadedBy;
    private ZonedDateTime uploadedAt;
}
