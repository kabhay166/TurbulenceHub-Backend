package com.example.spring_example.dto.mapper;

import com.example.spring_example.dto.response.DashboardRunResponseDto;
import com.example.spring_example.entity.run.HydroRun;
import com.example.spring_example.entity.run.MhdRun;

import java.util.ArrayList;
import java.util.List;

public class DashboardRunMapper {

    public static List<DashboardRunResponseDto> convertHydroRunsToDashboardDtos(List<HydroRun> hydroRuns) {
        List<DashboardRunResponseDto> dashboardRunResponseDtos = new ArrayList<>();

        for(HydroRun hydroRun : hydroRuns) {
            DashboardRunResponseDto dashboardRunResponseDto = new DashboardRunResponseDto();
            dashboardRunResponseDto.setKind("Hydro");
            dashboardRunResponseDto.setDimension(hydroRun.getDimension());
            dashboardRunResponseDto.setTimeOfRun(hydroRun.getTimeOfRun());
            dashboardRunResponseDtos.add(dashboardRunResponseDto);
        }

        return dashboardRunResponseDtos;
    }

    public static List<DashboardRunResponseDto> convertMhdRunsToDashboardDtos(List<MhdRun> mhdRuns) {
        List<DashboardRunResponseDto> dashboardRunResponseDtos = new ArrayList<>();

        for(MhdRun mhdRun : mhdRuns) {
            DashboardRunResponseDto dashboardRunResponseDto = new DashboardRunResponseDto();
            dashboardRunResponseDto.setKind("Mhd");
            dashboardRunResponseDto.setDimension(mhdRun.getDimension());
            dashboardRunResponseDto.setTimeOfRun(mhdRun.getTimeOfRun());
            dashboardRunResponseDtos.add(dashboardRunResponseDto);
        }

        return dashboardRunResponseDtos;
    }
}
