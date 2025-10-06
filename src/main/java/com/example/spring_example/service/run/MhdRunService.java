package com.example.spring_example.service.run;

import com.example.spring_example.dto.mapper.MhdRunMapper;
import com.example.spring_example.entity.AppUser;
import com.example.spring_example.entity.run.MhdRun;
import com.example.spring_example.models.MhdPara;
import com.example.spring_example.repository.UserRepository;
import com.example.spring_example.repository.run.MhdRunRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.time.ZonedDateTime;
import java.util.Optional;

@Service
public class MhdRunService {
    MhdRunRepository mhdRunRepository;
    UserRepository userRepository;
    public MhdRunService(MhdRunRepository hydroRunRepository, UserRepository userRepository) {
        this.mhdRunRepository = hydroRunRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Long createNewRun(MhdPara mhdPara, String username) {
        Optional<AppUser> user = userRepository.findByUsername(username);
        if(user.isEmpty()) {
            return -1L;
        }
        MhdRun mhdRun = new MhdRun();
        MhdRunMapper.convertMhdParaToMhdRun(mhdPara,mhdRun);
        mhdRun.setAppUser(user.get());
        mhdRun.setCompleted(false);
        mhdRun.setTimeOfRun(ZonedDateTime.now());
        mhdRun.setCreatedDate(ZonedDateTime.now());
        mhdRunRepository.save(mhdRun);
        user.get().addMhdRun(mhdRun);
        userRepository.save(user.get());
        return mhdRun.getId();
    }

    public void markRunCompleted(Long id) {
        Optional<MhdRun> run = mhdRunRepository.findById(id);
        if(run.isEmpty()) {
            return;
        }

        run.get().setCompleted(true);
        run.get().setTimeOfCompletion(ZonedDateTime.now());
        mhdRunRepository.save(run.get());
    }

    public void markRunStopped(Long id) {
        Optional<MhdRun> run = mhdRunRepository.findById(id);
        if(run.isEmpty()) {
            return;
        }

        run.get().setWasStopped(true);
        run.get().setTimeOfStop(ZonedDateTime.now());
        mhdRunRepository.save(run.get());
    }
}
