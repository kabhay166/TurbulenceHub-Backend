package com.example.spring_example.repository.data;

import com.example.spring_example.entity.data.ScalarData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScalarDataRepository extends JpaRepository<ScalarData,Long> {
}
