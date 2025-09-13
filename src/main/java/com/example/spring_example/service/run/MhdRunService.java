package com.example.spring_example.service.run;

import com.example.spring_example.dto.mapper.MhdRunMapper;
import com.example.spring_example.entity.run.MhdRun;
import com.example.spring_example.models.MhdPara;
import com.example.spring_example.repository.run.MhdRunRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MhdRunService {
    MhdRunRepository mhdRunRepository;

    public MhdRunService(MhdRunRepository hydroRunRepository) {
        this.mhdRunRepository = hydroRunRepository;
    }

    public void createNewRun(MhdPara mhdPara) {
        MhdRun mhdRun = new MhdRun();
        MhdRunMapper.convertMhdParaToMhdRun(mhdPara,mhdRun);
        mhdRun.setUser(null);
        mhdRun.setCompleted(false);
        mhdRun.setTimeOfRun(LocalDateTime.now());
        mhdRunRepository.save(mhdRun);
    }
}
