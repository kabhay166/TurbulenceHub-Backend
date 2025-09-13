package com.example.spring_example.entity.run;

import com.example.spring_example.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MhdRun extends BasicRun {
    @ManyToOne
    private User user;
    private Boolean completed;
    private LocalDateTime timeOfRun;
    private Boolean wasStopped;
    private LocalDateTime timeOfCompletion;

}