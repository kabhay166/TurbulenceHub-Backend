package com.example.spring_example.controller;


import com.example.spring_example.config.AppConfig;
import com.example.spring_example.config.SimulationConfig;
import com.example.spring_example.dto.mapper.CompletedRunResponseMapper;
import com.example.spring_example.dto.mapper.DashboardRunResponseMapper;
import com.example.spring_example.dto.request.AnalyzeRunRequestDto;
import com.example.spring_example.dto.response.CompletedRunResponseDto;
import com.example.spring_example.dto.response.DashboardRunResponseDto;
import com.example.spring_example.entity.AppUser;
import com.example.spring_example.entity.run.HydroRun;
import com.example.spring_example.entity.run.MhdRun;
import com.example.spring_example.models.BasicPara;
import com.example.spring_example.service.UserService;
import com.example.spring_example.service.run.HydroRunService;
import com.example.spring_example.service.run.MhdRunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin("*")
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

    @PostMapping("/analyzeRun")
    public ResponseEntity<Map<String, Object>> analyzeRun(@RequestBody AnalyzeRunRequestDto analyzeRunRequestDto) {
        StringBuilder outputBuilder = new StringBuilder();

        try {

            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            String kind = analyzeRunRequestDto.getKind();
            Long id = analyzeRunRequestDto.getId();
            double startTime = analyzeRunRequestDto.getStartTime();
            double endTime = analyzeRunRequestDto.getEndTime();
            boolean plotEnergy = analyzeRunRequestDto.isPlotEnergy();
            boolean plotSpectrum = analyzeRunRequestDto.isPlotSpectrum();
            boolean plotFlux = analyzeRunRequestDto.isPlotFlux();
            boolean plotFields = analyzeRunRequestDto.isPlotFields();

            System.out.println("kind = " + kind + " id = " + id + " startTime = " + startTime + " endTime = " + endTime + " plotEnergy = " + plotEnergy);

            StringBuilder analyzeRunArgs = new StringBuilder();
            String timeValueArg = "--time_values \"";
            timeValueArg += startTime + " " + endTime + "\" ";

            String plotEnergyArg = plotEnergy ?  "--energy" : " ";
            String plotSpectrumArg = plotSpectrum ? "--spectrum" : " ";
            String plotFluxArg = plotFlux ? "--flux" : " ";
            String plotFieldArg = plotFields ? "--field" : " ";

            String outputPath = "";

            String timeOfRunPath = "";

            if (kind.equalsIgnoreCase("hydro")) {
                Optional<HydroRun> hydroRun = hydroRunService.findById(id);
                if (hydroRun.isEmpty()) {
                    return ResponseEntity.badRequest().body(new HashMap<>());
                }
                LocalDateTime timeOfRun = hydroRun.get().getTimeOfRun().toLocalDateTime();
                System.out.println("Time of run is: " + timeOfRun.toString());
                timeOfRunPath = hydroRun.get().getTimeOfRunPath();
            } else if (kind.equalsIgnoreCase("mhd")) {
                Optional<MhdRun> mhdRun = mhdRunService.findById(id);
                if (mhdRun.isEmpty()) {
                    return ResponseEntity.badRequest().body(new HashMap<>());
                }

                timeOfRunPath = mhdRun.get().getTimeOfRunPath();
            }

            outputPath = Paths.get(AppConfig.getBaseOutputPath(), username, "Hydro Runs", timeOfRunPath).toString().replace("\\", "/");

            analyzeRunArgs.append(" --output_path ").append(outputPath);


            List<String> command = new ArrayList<>();
            command.addAll( List.of(timeValueArg, "--output_path  \"" + outputPath + "\" ", plotEnergyArg,plotSpectrumArg,plotFluxArg,plotFieldArg));

            System.out.println("full command is: " + command);
            ProcessBuilder builder = new ProcessBuilder(
                    SimulationConfig.getPythonPath(),
                    SimulationConfig.getAnalyzeScriptPath(),
                    "--time_values",
                    startTime + " " + endTime,
                    "--output_path",
                    outputPath,
                    plotEnergyArg,
                    plotSpectrumArg,
                    plotFluxArg,
                    plotFieldArg
                    );

            try {
                builder.redirectErrorStream(true);
                Process newProcess = builder.start();

                try(BufferedReader reader = new BufferedReader(new InputStreamReader(newProcess.getInputStream()))) {
                    String line;
                    while( (line = reader.readLine()) != null ) {
                        outputBuilder.append(line.trim());

                    }

                } catch (IOException e) {
                    outputBuilder.append("Error occurred while capturing output: ").append(e.getMessage());
                }
                newProcess.waitFor();


            } catch (IOException e) {
                System.out.println("Exception occurred: " + e.getMessage());
                return ResponseEntity.internalServerError().body(new HashMap<>());
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted");
                return ResponseEntity.internalServerError().body(new HashMap<>());
            }
            Map<String,Object> responseMap = new HashMap<>();
            responseMap.put("output",outputBuilder.toString());



            return ResponseEntity.ok().body(responseMap);

        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
            Map<String,Object> responseMap = new HashMap<>();
            responseMap.put("output",outputBuilder.toString());
            return ResponseEntity.internalServerError().body(responseMap);
        }
    }

}
