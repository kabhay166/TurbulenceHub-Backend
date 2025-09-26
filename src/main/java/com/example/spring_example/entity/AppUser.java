package com.example.spring_example.entity;


import com.example.spring_example.entity.run.HydroRun;
import com.example.spring_example.entity.run.MhdRun;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Array;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "app_user")
public class AppUser extends BasicEntity {

    private String username;
    private String password;
    private String email;
    private ZonedDateTime lastPasswordResetDate;
    private boolean isLicensed;
    private List<String> roles = new ArrayList<>(List.of("USER"));
    private boolean isVerified = false;

    @OneToMany
    private List<HydroRun> hydroRuns;

    @OneToMany
    private List<MhdRun> mhdRuns;

}
