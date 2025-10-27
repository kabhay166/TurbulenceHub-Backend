package com.example.spring_example.repository.run;
import com.example.spring_example.entity.run.HydroRun;
import com.example.spring_example.entity.run.MhdRun;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MhdRunRepository extends JpaRepository<MhdRun,Long> {

    @Query("SELECT m FROM MhdRun m WHERE m.appUser.id = :userId ORDER BY m.timeOfRun DESC")
    public List<MhdRun> findByUserIdOrderByTimeOfRunDesc(Long userId, Pageable pageable);

}

