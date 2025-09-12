package com.example.spring_example.repository.data;

import com.example.spring_example.entity.data.MhdData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MhdDataRepository extends JpaRepository<MhdData, Long> {
}
