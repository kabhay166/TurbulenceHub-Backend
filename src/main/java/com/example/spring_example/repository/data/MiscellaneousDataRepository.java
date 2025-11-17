package com.example.spring_example.repository.data;

import com.example.spring_example.entity.data.MiscellaneousData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MiscellaneousDataRepository extends JpaRepository<MiscellaneousData,Long> {
}
