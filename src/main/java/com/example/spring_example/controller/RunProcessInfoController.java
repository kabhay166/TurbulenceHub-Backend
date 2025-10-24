package com.example.spring_example.controller;

import com.example.spring_example.dto.mapper.RunProcessInfoMapper;
import com.example.spring_example.dto.response.RunProcessInfoResponseDto;
import com.example.spring_example.entity.RunProcessInfo;
import com.example.spring_example.service.ProcessManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RunProcessInfoController {
    @Autowired
    ProcessManager processManager;


    @GetMapping("/active-runs")
    public ResponseEntity<List<RunProcessInfoResponseDto>> getActiveRuns() {
        List<RunProcessInfo> allRunProcessInfo = processManager.getAllRunProcessInfo();
        List<RunProcessInfoResponseDto> allRunProcessInfoResponseDto = new ArrayList<>();
        for(RunProcessInfo runProcessInfo: allRunProcessInfo) {
            allRunProcessInfoResponseDto.add(RunProcessInfoMapper.covertProcessInfoToProcessInfoResponseDto(runProcessInfo));
        }

        return ResponseEntity.ok(allRunProcessInfoResponseDto);
    }

}
