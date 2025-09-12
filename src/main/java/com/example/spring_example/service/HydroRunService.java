package com.example.spring_example.service;

import com.example.spring_example.dto.mapper.HydroRunMapper;
import com.example.spring_example.entity.run.HydroRun;
import com.example.spring_example.models.HydroPara;
import com.example.spring_example.repository.run.HydroRunRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class HydroRunService {
    HydroRunRepository hydroRunRepository;

    public HydroRunService(HydroRunRepository hydroRunRepository) {
        this.hydroRunRepository = hydroRunRepository;
    }

    public void createNewRun(HydroPara hydroPara) {
        HydroRun hydroRun = new HydroRun();
        HydroRunMapper.convertHydroParaToHydroRun(hydroPara,hydroRun);
        hydroRun.setUser(null);
        hydroRun.setCompleted(false);
        hydroRun.setTimeOfRun(LocalDateTime.now());
        hydroRunRepository.save(hydroRun);
    }
}
