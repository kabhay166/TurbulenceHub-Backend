package com.example.spring_example.entity;


import com.example.spring_example.entity.run.HydroRun;
import com.example.spring_example.entity.run.MhdRun;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
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
    private String otp;
    private LocalDateTime otpExpiryDate;

    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HydroRun> hydroRuns;

    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MhdRun> mhdRuns;

    public void addHydroRun(HydroRun run) {
        hydroRuns.add(run);
        run.setAppUser(this);
    }

    public void removeHydroRun(HydroRun run) {
        hydroRuns.remove(run);
        run.setAppUser(null);
    }

    public void addMhdRun(MhdRun run) {
        mhdRuns.add(run);
        run.setAppUser(this);
    }

    public void removeMhdRun(MhdRun run) {
        mhdRuns.remove(run);
        run.setAppUser(null);
    }


}
