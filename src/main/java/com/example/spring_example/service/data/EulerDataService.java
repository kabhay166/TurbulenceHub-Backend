package com.example.spring_example.service.data;

import com.example.spring_example.dto.mapper.DataUploadDtoMapper;
import com.example.spring_example.dto.request.DataUploadDto;
import com.example.spring_example.entity.data.EulerData;
import com.example.spring_example.repository.data.EulerDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EulerDataService {

    private final EulerDataRepository eulerDataRepository;

    public EulerDataService(EulerDataRepository eulerDataRepository) {
        this.eulerDataRepository = eulerDataRepository;
    }

    public List<EulerData> getAll() {
        return eulerDataRepository.findAll();
    }

    public Optional<EulerData> getById(Long id) {
        return eulerDataRepository.findById(id);
    }

    public boolean addData(DataUploadDto dataUploadDto) {
        try {
            EulerData eulerData = DataUploadDtoMapper.mapToEulerData(dataUploadDto);
            eulerDataRepository.save(eulerData);
            return true;
        } catch(Exception e) {
            return false;
        }

    }
}
