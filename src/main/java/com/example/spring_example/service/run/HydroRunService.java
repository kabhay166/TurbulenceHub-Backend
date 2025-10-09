package com.example.spring_example.service.run;

import com.example.spring_example.dto.mapper.HydroRunMapper;
import com.example.spring_example.entity.AppUser;
import com.example.spring_example.entity.run.HydroRun;
import com.example.spring_example.models.HydroPara;
import com.example.spring_example.repository.UserRepository;
import com.example.spring_example.repository.run.HydroRunRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HydroRunService {
    HydroRunRepository hydroRunRepository;
    UserRepository userRepository;
    public HydroRunService(HydroRunRepository hydroRunRepository,UserRepository userRepository) {
        this.hydroRunRepository = hydroRunRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Long createNewRun(HydroPara hydroPara, String username) {
        Optional<AppUser> user = userRepository.findByUsername(username);
        if(user.isEmpty()) {
            return -1L;
        }
        HydroRun hydroRun = new HydroRun();
        HydroRunMapper.convertHydroParaToHydroRun(hydroPara,hydroRun);
        hydroRun.setAppUser(user.get());
        hydroRun.setCompleted(false);
        hydroRun.setTimeOfRun(ZonedDateTime.now());
        hydroRunRepository.save(hydroRun);
        user.get().addHydroRun(hydroRun);
        userRepository.save(user.get());
        return hydroRun.getId();
    }

    public void markRunCompleted(Long id) {
        Optional<HydroRun> run = hydroRunRepository.findById(id);
        if(run.isEmpty()) {
            return;
        }

        run.get().setCompleted(true);
        run.get().setTimeOfCompletion(ZonedDateTime.now());
        hydroRunRepository.save(run.get());
    }

    public void markRunStopped(Long id) {
        Optional<HydroRun> run = hydroRunRepository.findById(id);
        if(run.isEmpty()) {
            return;
        }

        run.get().setWasStopped(true);
        run.get().setTimeOfStop(ZonedDateTime.now());
        hydroRunRepository.save(run.get());
    }

    public List<HydroRun> getAllRunsByUser(Long userId) {
        return hydroRunRepository.findAll().stream().filter(run -> Objects.equals(run.getAppUser().getId(), userId)).toList();
    }
}
