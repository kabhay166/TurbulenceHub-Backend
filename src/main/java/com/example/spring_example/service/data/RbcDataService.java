package com.example.spring_example.service.data;

import com.example.spring_example.dto.mapper.DataUploadDtoMapper;
import com.example.spring_example.dto.request.DataUploadDto;
import com.example.spring_example.entity.data.HydroData;
import com.example.spring_example.entity.data.RbcData;
import com.example.spring_example.repository.data.RbcDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RbcDataService {


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

    public boolean addData(DataUploadDto dataUploadDto) {
        try {
            RbcData rbcData = DataUploadDtoMapper.mapToRbcData(dataUploadDto);
            rbcDataRepository.save(rbcData);
            return true;
        } catch(Exception e) {
            return false;
        }

    }
}
