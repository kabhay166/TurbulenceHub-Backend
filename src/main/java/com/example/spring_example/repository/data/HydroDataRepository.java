package com.example.spring_example.repository.data;

import com.example.spring_example.entity.data.HydroData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HydroDataRepository extends JpaRepository<HydroData, Long> {
}
