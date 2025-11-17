package com.example.spring_example.service.data;


import com.example.spring_example.dto.mapper.DataUploadDtoMapper;
import com.example.spring_example.dto.request.DataUploadDto;
import com.example.spring_example.entity.data.EulerData;
import com.example.spring_example.entity.data.HydroData;
import com.example.spring_example.entity.data.MhdData;
import com.example.spring_example.repository.data.HydroDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HydroDataService {
    @Autowired
    private HydroDataRepository hydroDataRepository;

    public List<HydroData> getAll() {
        return hydroDataRepository.findAll();
    }

    public Optional<HydroData> getById(Long id) {
        return hydroDataRepository.findById(id);
    }

    public boolean addData(DataUploadDto dataUploadDto) {
        try {
            HydroData hydroData = DataUploadDtoMapper.mapToHydroData(dataUploadDto);
            hydroDataRepository.save(hydroData);
            return true;
        } catch(Exception e) {
            return false;
        }

    }

}
