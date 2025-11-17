package com.example.spring_example.service.data;


import com.example.spring_example.dto.mapper.DataUploadDtoMapper;
import com.example.spring_example.dto.request.DataUploadDto;
import com.example.spring_example.entity.data.EulerData;
import com.example.spring_example.entity.data.HydroData;
import com.example.spring_example.entity.data.MhdData;
import com.example.spring_example.repository.data.MhdDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MhdDataService {

    @Autowired
    private MhdDataRepository mhdDataRepository;

    public List<MhdData> getAll() {
        return mhdDataRepository.findAll();
    }

    public Optional<MhdData> getById(Long id) {
        return mhdDataRepository.findById(id);
    }

    public boolean addData(DataUploadDto dataUploadDto) {
        try {
            MhdData mhdData = DataUploadDtoMapper.mapToMhdData(dataUploadDto);
            mhdDataRepository.save(mhdData);
            return true;
        } catch(Exception e) {
            return false;
        }

    }
}
