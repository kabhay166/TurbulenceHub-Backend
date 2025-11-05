package com.example.spring_example.repository.data;

import com.example.spring_example.entity.data.EulerData;
import com.example.spring_example.entity.data.RbcData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RbcDataRepository extends JpaRepository<RbcData,Long> {
}
