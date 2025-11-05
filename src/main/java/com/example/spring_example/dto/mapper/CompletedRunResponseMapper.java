package com.example.spring_example.dto.mapper;

import com.example.spring_example.dto.response.CompletedRunResponseDto;
import com.example.spring_example.entity.run.HydroRun;
import com.example.spring_example.entity.run.MhdRun;

import java.util.ArrayList;
import java.util.List;

public class CompletedRunResponseMapper {

    public static List<CompletedRunResponseDto> convertHydroRunsToCompletedRunResponseDtos(List<HydroRun> hydroRuns) {
        List<CompletedRunResponseDto> completedRuns = new ArrayList<>();

        for(HydroRun hydroRun : hydroRuns) {
            CompletedRunResponseDto completedRunResponseDto = new CompletedRunResponseDto();
            completedRunResponseDto.setId(hydroRun.getId());
            completedRunResponseDto.setKind("Hydro");
            completedRunResponseDto.setDimension(hydroRun.getDimension());
            completedRunResponseDto.setTimeOfRun(hydroRun.getTimeOfRun());
            completedRunResponseDto.setDt(hydroRun.getDt());
            String resolution = hydroRun.getNx() + "x" + hydroRun.getNy() + "x" + hydroRun.getNz();
            completedRunResponseDto.setResolution(resolution);
            completedRunResponseDto.setTime_scheme(hydroRun.getTimeScheme());
            completedRunResponseDto.setFIXED_DT(hydroRun.isFixedDt());
            completedRuns.add(completedRunResponseDto);
        }

        return completedRuns;
    }

    public static List<CompletedRunResponseDto> convertMhdRunsToCompletedRunResponseDtos(List<MhdRun> mhdRuns) {
        List<CompletedRunResponseDto> completedRuns = new ArrayList<>();

        for(MhdRun mhdRun : mhdRuns) {
            CompletedRunResponseDto completedRunResponseDto = new CompletedRunResponseDto();
            completedRunResponseDto.setId(mhdRun.getId());
            completedRunResponseDto.setKind("Mhd");
            completedRunResponseDto.setDimension(mhdRun.getDimension());
            completedRunResponseDto.setTimeOfRun(mhdRun.getTimeOfRun());

            String resolution = mhdRun.getNx() + "x" + mhdRun.getNy() + "x" + mhdRun.getNz();
            completedRunResponseDto.setResolution(resolution);

            completedRuns.add(completedRunResponseDto);
        }

        return completedRuns;
    }
}
