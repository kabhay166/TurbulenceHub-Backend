package com.example.spring_example.service;

import com.example.spring_example.repository.run.BasicRunRepository;
import org.springframework.stereotype.Service;

@Service
public class BasicRunService {

    BasicRunRepository basicRunRepository;

    public BasicRunService(BasicRunRepository basicRunRepository) {
        this.basicRunRepository = basicRunRepository;
    }

}
