package com.example.spring_example.service.data;

import com.example.spring_example.entity.data.RbcData;
import com.example.spring_example.repository.data.RbcDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RbcDataService {

    @Autowired
    public RbcDataRepository rbcDataRepository;

    public RbcDataService(RbcDataRepository rbcDataRepository) {
        this.rbcDataRepository = rbcDataRepository;
    }

    public List<RbcData> getAll() {
        return rbcDataRepository.findAll();
    }

    public Optional<RbcData> getById(Long id) {
        return rbcDataRepository.findById(id);
    }
}
