package com.example.spring_example.service.data;


import com.example.spring_example.entity.data.MhdData;
import com.example.spring_example.repository.data.MhdDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MhdDataService {

    @Autowired
    private MhdDataRepository mhdDataRepository;

    public List<MhdData> getAll() {
        return mhdDataRepository.findAll();
    }
}
