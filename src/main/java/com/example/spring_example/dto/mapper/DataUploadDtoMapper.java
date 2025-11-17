package com.example.spring_example.dto.mapper;

import com.example.spring_example.dto.request.DataUploadDto;
import com.example.spring_example.entity.data.*;

public class DataUploadDtoMapper {


    public static EulerData mapToEulerData(DataUploadDto dataUploadDto) {
        EulerData eulerData = new EulerData();
        eulerData.setDimension(dataUploadDto.getDimension());
        eulerData.setResolution(dataUploadDto.getResolution());
        eulerData.setInitialCondition(dataUploadDto.getInitialCondition());
        eulerData.setTInitial(dataUploadDto.getTInitial());
        eulerData.setTFinal(dataUploadDto.getTFinal());
        eulerData.setDownloadPath(dataUploadDto.getDownloadPath());
        eulerData.setDescription(dataUploadDto.getDescription());
        return eulerData;
    }

    public static HydroData mapToHydroData(DataUploadDto dataUploadDto) {
        HydroData hydroData = new HydroData();
        hydroData.setDimension(dataUploadDto.getDimension());
        hydroData.setResolution(dataUploadDto.getResolution());
        hydroData.setInitialCondition(dataUploadDto.getInitialCondition());
        hydroData.setTInitial(dataUploadDto.getTInitial());
        hydroData.setTFinal(dataUploadDto.getTFinal());
        hydroData.setDownloadPath(dataUploadDto.getDownloadPath());
        hydroData.setDescription(dataUploadDto.getDescription());
        hydroData.setViscosity(dataUploadDto.getViscosity());
        return hydroData;
    }

    public static MhdData mapToMhdData(DataUploadDto dataUploadDto) {
        MhdData mhdData = new MhdData();
        mhdData.setDimension(dataUploadDto.getDimension());
        mhdData.setResolution(dataUploadDto.getResolution());
        mhdData.setInitialCondition(dataUploadDto.getInitialCondition());
        mhdData.setTInitial(dataUploadDto.getTInitial());
        mhdData.setTFinal(dataUploadDto.getTFinal());
        mhdData.setDownloadPath(dataUploadDto.getDownloadPath());
        mhdData.setDescription(dataUploadDto.getDescription());
        mhdData.setViscosity(dataUploadDto.getViscosity());
        mhdData.setMagneticDiffusivity(dataUploadDto.getMagneticDiffusivity());
        return mhdData;
    }

    public static RbcData mapToRbcData(DataUploadDto dataUploadDto) {
        RbcData rbcData = new RbcData();
        rbcData.setDimension(dataUploadDto.getDimension());
        rbcData.setResolution(dataUploadDto.getResolution());
        rbcData.setInitialCondition(dataUploadDto.getInitialCondition());
        rbcData.setTInitial(dataUploadDto.getTInitial());
        rbcData.setTFinal(dataUploadDto.getTFinal());
        rbcData.setDownloadPath(dataUploadDto.getDownloadPath());
        rbcData.setDescription(dataUploadDto.getDescription());
        rbcData.setRayleighNumber(dataUploadDto.getRayleighNumber());
        rbcData.setPrandtlNumber(dataUploadDto.getPrandtlNumber());
        return rbcData;
    }

    public static MiscellaneousData mapToMiscellaneousData(DataUploadDto dataUploadDto) {
        MiscellaneousData miscellaneousData = new MiscellaneousData();
        miscellaneousData.setDimension(dataUploadDto.getDimension());
        miscellaneousData.setResolution(dataUploadDto.getResolution());
        miscellaneousData.setInitialCondition(dataUploadDto.getInitialCondition());
        miscellaneousData.setDownloadPath(dataUploadDto.getDownloadPath());
        miscellaneousData.setDescription(dataUploadDto.getDescription());
        return miscellaneousData;
    }
}
