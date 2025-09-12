package com.example.spring_example.repository.run;

import com.example.spring_example.entity.run.BasicRun;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasicRunRepository extends JpaRepository<BasicRun, Long> {
}
