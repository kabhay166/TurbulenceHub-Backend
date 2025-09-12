package com.example.spring_example.repository.data;

import com.example.spring_example.entity.data.EulerData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EulerDataRepository extends JpaRepository<EulerData,Long> {
}
