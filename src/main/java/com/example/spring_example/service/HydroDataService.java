package com.example.spring_example.service;


import com.example.spring_example.entity.data.HydroData;
import com.example.spring_example.repository.data.HydroDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HydroDataService {
    @Autowired
    private HydroDataRepository hydroDataRepository;

    public List<HydroData> getAll() {
        return hydroDataRepository.findAll();
    }



}
