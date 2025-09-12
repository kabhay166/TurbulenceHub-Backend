package com.example.spring_example.repository.run;

import com.example.spring_example.entity.run.HydroRun;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HydroRunRepository extends JpaRepository<HydroRun,Long> {
}
