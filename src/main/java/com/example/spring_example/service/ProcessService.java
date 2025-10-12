package com.example.spring_example.service;
import com.example.spring_example.repository.ProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProcessService {

    @Autowired
    private ProcessRepository processRepository;


    public boolean createProcess() {
        return true;
    }


}
