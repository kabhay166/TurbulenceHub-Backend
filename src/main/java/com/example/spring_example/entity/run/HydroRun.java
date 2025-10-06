package com.example.spring_example.entity.run;

import com.example.spring_example.entity.AppUser;
import jakarta.persistence.*;
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
public class HydroRun extends BasicRun {
    @ManyToOne
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;
    private Boolean completed;
    private ZonedDateTime timeOfRun;
    private Boolean wasStopped;
    private ZonedDateTime timeOfCompletion;
    private ZonedDateTime timeOfStop;

}
