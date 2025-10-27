package com.example.spring_example.repository.run;

import com.example.spring_example.entity.run.HydroRun;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HydroRunRepository extends JpaRepository<HydroRun,Long> {

    @Query("SELECT h FROM HydroRun h WHERE h.appUser.id = :userId ORDER BY h.timeOfRun DESC")
    public List<HydroRun> findByUserIdOrderByTimeOfRunDesc(@Param("userId") Long userId, Pageable pageable);
}
