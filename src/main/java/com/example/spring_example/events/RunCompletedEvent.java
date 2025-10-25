package com.example.spring_example.events;

import com.example.spring_example.dto.response.RunProcessInfoResponseDto;
import com.example.spring_example.entity.RunProcessInfo;
import lombok.*;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RunCompletedEvent {
    private String username;
    private String kind;
    private String dimension;
    private String resolution;
    private String timeOfRun;

}
