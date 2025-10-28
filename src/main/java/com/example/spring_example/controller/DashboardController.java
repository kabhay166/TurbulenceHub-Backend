package com.example.spring_example.controller;


import com.example.spring_example.dto.mapper.CompletedRunResponseMapper;
import com.example.spring_example.dto.mapper.DashboardRunResponseMapper;
import com.example.spring_example.dto.response.CompletedRunResponseDto;
import com.example.spring_example.dto.response.DashboardRunResponseDto;
import com.example.spring_example.entity.AppUser;
import com.example.spring_example.entity.run.HydroRun;
import com.example.spring_example.entity.run.MhdRun;
import com.example.spring_example.service.UserService;
import com.example.spring_example.service.run.HydroRunService;
import com.example.spring_example.service.run.MhdRunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    @Autowired
    private UserService userService;

    @Autowired
    private HydroRunService hydroRunService;

    @Autowired
    private MhdRunService mhdRunService;


    @GetMapping("/allRuns")
    public ResponseEntity<List<DashboardRunResponseDto>> getAllUserRuns() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<AppUser> user = userService.findByUsername(username);
        if(user.isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }

        List<HydroRun> hydroRuns = hydroRunService.getAllRunsByUser(user.get().getId());
        List<MhdRun> mhdRuns = mhdRunService.getAllRunsByUser(user.get().getId());

        List<DashboardRunResponseDto> dashboardRunResponseDtos = new ArrayList<>();

        dashboardRunResponseDtos.addAll(DashboardRunResponseMapper.convertHydroRunsToDashboardDtos(hydroRuns));
        dashboardRunResponseDtos.addAll(DashboardRunResponseMapper.convertMhdRunsToDashboardDtos(mhdRuns));

        return ResponseEntity.ok().body(dashboardRunResponseDtos);

    }

    @GetMapping("/recentRuns")
    public ResponseEntity<List<DashboardRunResponseDto>> getRecentUserRuns() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<AppUser> user = userService.findByUsername(username);
        if(user.isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }

        List<HydroRun> hydroRuns = hydroRunService.getLatestRunsByUser(user.get().getId());
        List<MhdRun> mhdRuns = mhdRunService.getLatestRunsByUser(user.get().getId());

        List<DashboardRunResponseDto> dashboardRunResponseDtos = new ArrayList<>();

        dashboardRunResponseDtos.addAll(DashboardRunResponseMapper.convertHydroRunsToDashboardDtos(hydroRuns));
        dashboardRunResponseDtos.addAll(DashboardRunResponseMapper.convertMhdRunsToDashboardDtos(mhdRuns));

        dashboardRunResponseDtos = dashboardRunResponseDtos.stream().sorted(((o1, o2) -> o2.getTimeOfRun().compareTo(o1.getTimeOfRun()))).limit(10).toList();
        return ResponseEntity.ok().body(dashboardRunResponseDtos);

    }

    @GetMapping("/completedRuns")
    public ResponseEntity<List<CompletedRunResponseDto>> getCompletedRuns() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<AppUser> user = userService.findByUsername(username);
        if(user.isEmpty()) {
            System.out.println("User is empty so returning empty list");
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
        List<HydroRun> completedHydroRuns = hydroRunService.getAllCompletedRunsByUser(user.get().getId());
        List<MhdRun> completedMhdRuns = mhdRunService.getAllCompletedRunsByUser(user.get().getId());

        List<CompletedRunResponseDto> completedRunResponseDtos = new ArrayList<>();
        completedRunResponseDtos.addAll(CompletedRunResponseMapper.convertHydroRunsToCompletedRunResponseDtos(completedHydroRuns));
        completedRunResponseDtos.addAll(CompletedRunResponseMapper.convertMhdRunsToCompletedRunResponseDtos(completedMhdRuns));
        completedRunResponseDtos = completedRunResponseDtos.stream().sorted(((o1, o2) -> o2.getTimeOfRun().compareTo(o1.getTimeOfRun()))).toList();

        return ResponseEntity.ok().body(completedRunResponseDtos);

    }
}
