package com.example.spring_example.service.run;

import com.example.spring_example.dto.mapper.MhdRunMapper;
import com.example.spring_example.entity.AppUser;
import com.example.spring_example.entity.run.HydroRun;
import com.example.spring_example.entity.run.MhdRun;
import com.example.spring_example.models.MhdPara;
import com.example.spring_example.repository.UserRepository;
import com.example.spring_example.repository.run.MhdRunRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
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
    public Long createNewRun(MhdPara mhdPara, String username,ZonedDateTime timeOfRun,String timeOfRunPath) {
        Optional<AppUser> user = userRepository.findByUsername(username);
        if(user.isEmpty()) {
            return -1L;
        }
        MhdRun mhdRun = new MhdRun();
        MhdRunMapper.convertMhdParaToMhdRun(mhdPara,mhdRun);
        mhdRun.setAppUser(user.get());
        mhdRun.setCompleted(false);
        mhdRun.setTimeOfRun(timeOfRun);
        mhdRun.setTimeOfRunPath(timeOfRunPath);
        mhdRunRepository.save(mhdRun);
        user.get().addMhdRun(mhdRun);
        userRepository.save(user.get());
        return mhdRun.getId();
    }

    public Optional<MhdRun> findById(Long id) {
        return mhdRunRepository.findById(id);
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

    public List<MhdRun> getAllRunsByUser(Long userId) {
        return mhdRunRepository.findAll().stream().filter(run -> Objects.equals(run.getAppUser().getId(), userId)).toList();
    }

    public List<MhdRun> getAllCompletedRunsByUser(Long userId) {
        return mhdRunRepository.findAll().stream().filter(run -> Objects.equals(run.getAppUser().getId(),userId)).
                filter(run -> run.getCompleted() == true).toList();
    }

    public List<MhdRun> getLatestRunsByUser(Long userId) {
        return mhdRunRepository.findByUserIdOrderByTimeOfRunDesc(userId, PageRequest.of(0,10));
    }
}
