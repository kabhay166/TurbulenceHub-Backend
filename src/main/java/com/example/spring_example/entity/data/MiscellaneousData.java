package com.example.spring_example.entity.data;

import com.example.spring_example.entity.AppUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MiscellaneousData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    private int dimension;
    @NotBlank
    private String resolution;
    @NotBlank
    private String initialCondition;
    @NotBlank
    private String downloadPath;
    private String description;
    @ManyToOne
    private AppUser uploadedBy;
    private ZonedDateTime uploadedAt;
}
