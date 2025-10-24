package com.example.spring_example.dto.mapper;

import com.example.spring_example.dto.response.RunProcessInfoResponseDto;
import com.example.spring_example.entity.RunProcessInfo;

public class RunProcessInfoMapper {
    public static RunProcessInfoResponseDto covertProcessInfoToProcessInfoResponseDto(RunProcessInfo runProcessInfo) {
        RunProcessInfoResponseDto runProcessInfoResponseDto = new RunProcessInfoResponseDto();
        runProcessInfoResponseDto.setTimeOfRun(runProcessInfo.getTimeOfRun());
        runProcessInfoResponseDto.setKind(runProcessInfo.getKind());
        runProcessInfoResponseDto.setDimension(runProcessInfo.getDimension());
        runProcessInfoResponseDto.setResolution(runProcessInfo.getResolution());
        runProcessInfoResponseDto.setProcessInfoId(runProcessInfo.getProcessInfoId());

        return runProcessInfoResponseDto;
    }
}
