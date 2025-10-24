package com.example.spring_example.controller;

import com.example.spring_example.dto.mapper.RunProcessInfoMapper;
import com.example.spring_example.dto.response.RunProcessInfoResponseDto;
import com.example.spring_example.entity.RunProcessInfo;
import com.example.spring_example.service.ProcessManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RunProcessInfoController {
    @Autowired
    ProcessManager processManager;


    @GetMapping("/active-runs")
    public ResponseEntity<List<RunProcessInfoResponseDto>> getActiveRuns() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        List<RunProcessInfo> allRunProcessInfo = processManager.getAllRunProcessInfoForUsername(username);
        List<RunProcessInfoResponseDto> allRunProcessInfoResponseDto = new ArrayList<>();
        for(RunProcessInfo runProcessInfo: allRunProcessInfo) {
            allRunProcessInfoResponseDto.add(RunProcessInfoMapper.covertProcessInfoToProcessInfoResponseDto(runProcessInfo));
        }

        return ResponseEntity.ok(allRunProcessInfoResponseDto);
    }

    @PostMapping("/stop-run")
    public ResponseEntity<String> stopActiveRun(@RequestBody String runProcessInfoId) {
        try {
            processManager.stopProcess(runProcessInfoId);
            return ResponseEntity.ok("");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error occurred while stopping run.");
        }

    }

}
