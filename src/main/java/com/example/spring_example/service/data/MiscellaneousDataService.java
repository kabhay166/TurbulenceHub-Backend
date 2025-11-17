package com.example.spring_example.service.data;

import com.example.spring_example.dto.mapper.DataUploadDtoMapper;
import com.example.spring_example.dto.request.DataUploadDto;
import com.example.spring_example.entity.data.EulerData;
import com.example.spring_example.entity.data.MiscellaneousData;
import com.example.spring_example.entity.data.RbcData;
import com.example.spring_example.repository.data.EulerDataRepository;
import com.example.spring_example.repository.data.MiscellaneousDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MiscellaneousDataService {

    @Autowired
    public MiscellaneousDataRepository miscellaneousDataRepository;

    public MiscellaneousDataService(MiscellaneousDataRepository miscellaneousDataRepository) {
        this.miscellaneousDataRepository = miscellaneousDataRepository;
    }

    public List<MiscellaneousData> getAll() {
        return miscellaneousDataRepository.findAll();
    }

    public Optional<MiscellaneousData> getById(Long id) {
        return miscellaneousDataRepository.findById(id);
    }

    public boolean addData(DataUploadDto dataUploadDto) {
        try {
            MiscellaneousData miscellaneousData = DataUploadDtoMapper.mapToMiscellaneousData(dataUploadDto);
            miscellaneousDataRepository.save(miscellaneousData);
            return true;
        } catch(Exception e) {
            return false;
        }

    }
}
