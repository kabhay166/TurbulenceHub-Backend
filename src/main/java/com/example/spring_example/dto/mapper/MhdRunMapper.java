package com.example.spring_example.dto.mapper;

import com.example.spring_example.entity.run.MhdRun;
import com.example.spring_example.models.MhdPara;

public class MhdRunMapper {

    public static MhdRun convertMhdParaToMhdRun(MhdPara mhdPara, MhdRun mhdRun) {
        mhdRun.setDevice(mhdPara.getDevice());
        mhdRun.setDeviceRank(mhdPara.getDevice_rank());
        mhdRun.setComplexDtype(mhdPara.getComplex_dtype());
        mhdRun.setRealDtype(mhdPara.getReal_dtype());
        mhdRun.setDimension(mhdPara.getDimension());
        mhdRun.setNx(mhdPara.getNx());
        mhdRun.setNy(mhdPara.getNy());
        mhdRun.setNz(mhdPara.getNz());
        mhdRun.setNu(mhdPara.getNu());
        mhdRun.setEta(mhdPara.getEta());
        mhdRun.setKappa(mhdPara.getKappa());
        mhdRun.setTimeScheme(mhdPara.getTime_scheme());
        mhdRun.setTInitial(mhdPara.getT_initial());
        mhdRun.setTFinal(mhdPara.getT_final());
        mhdRun.setDt(mhdPara.getDt());
        mhdRun.setFixedDt(mhdPara.isFIXED_DT());
        mhdRun.setInputSetCase(mhdPara.isINPUT_SET_CASE());
        mhdRun.setInputCase(mhdPara.getInput_case());
        mhdRun.setInputFromFile(mhdPara.isINPUT_FROM_FILE());
        mhdRun.setInputRealField(mhdPara.isINPUT_REAL_FIELD());
        mhdRun.setInputElsasser(mhdPara.isINPUT_ELSASSER());
        mhdRun.setInputFileName(mhdPara.getInput_file_name());
        mhdRun.setOutputRealField(mhdPara.isOUTPUT_REAL_FIELD());
        mhdRun.setL(mhdPara.getL());
        mhdRun.setBoxSizeDefault(mhdPara.isBOX_SIZE_DEFAULT());
        mhdRun.setCourantNo(mhdPara.getCourant_no());
        mhdRun.setT_eps(mhdPara.getT_eps());
        mhdRun.setPrintParameters(mhdPara.isPRINT_PARAMETERS());
        mhdRun.setRuntimeSave(mhdPara.isRUNTIME_SAVE());
        mhdRun.setLivePlot(mhdPara.isLIVE_PLOT());
        mhdRun.setUseBinding(mhdPara.isUSE_BINDING());
        mhdRun.setPlanarSpectra(mhdPara.isPLANAR_SPECTRA());
        mhdRun.setSaveVorticity(mhdPara.isSAVE_VORTICITY());
        mhdRun.setSaveVecpot(mhdPara.isSAVE_VECPOT());
        mhdRun.setModesSave(mhdPara.getModes_save());
        mhdRun.setIterFieldSaveStart(mhdPara.getIter_field_save_start());
        mhdRun.setIterFieldSaveStart(mhdPara.getIter_field_save_inter());
        mhdRun.setIterGlobalEnergyPrintStart(mhdPara.getIter_glob_energy_print_start());
        mhdRun.setIterGlobalEnergyPrintInter(mhdPara.getIter_glob_energy_print_inter());
        mhdRun.setIterEkTkSaveStart(mhdPara.getIter_ekTk_save_start());
        mhdRun.setIterEkTkSaveInter(mhdPara.getIter_ekTk_save_inter());
        mhdRun.setIterModesSaveStart(mhdPara.getIter_modes_save_start());
        mhdRun.setIterModesSaveInter(mhdPara.getIter_modes_save_inter());
        mhdRun.setRac(mhdPara.getRac());
        mhdRun.setRa(mhdPara.getRa());
        mhdRun.setPr(mhdPara.getPr());
        mhdRun.setHyperDissipation(mhdPara.isHYPER_DISSIPATION());
        mhdRun.setNuHypo(mhdPara.getNu_hypo());
        mhdRun.setNuHypoPower(mhdPara.getNu_hypo_power());
        mhdRun.setNuHypoCutoff(mhdPara.getNu_hypo_cutoff());
        mhdRun.setNuHyper(mhdPara.getNu_hyper());
        mhdRun.setNuHyperPower(mhdPara.getNu_hyper_power());
        mhdRun.setEtaHypo(mhdPara.getEta_hypo());
        mhdRun.setEtaHypoPower(mhdPara.getEta_hypo_power());
        mhdRun.setEtaHypoCutoff(mhdPara.getEta_hypo_cutoff());
        mhdRun.setEtaHyper(mhdPara.getEta_hyper());
        mhdRun.setEtaHyperPower(mhdPara.getEta_hyper_power());
        mhdRun.setKappaHypo(mhdPara.getKappa_hypo());
        mhdRun.setKappaHypoPower(mhdPara.getKappa_hypo_power());
        mhdRun.setKappaHypoCutoff(mhdPara.getKappa_hypo_cutoff());
        mhdRun.setKappaHyper(mhdPara.getKappa_hyper());
        mhdRun.setKappaHyperPower(mhdPara.getKappa_hyper_power());
        mhdRun.setForcingEnabled(mhdPara.isFORCING_ENABLED());
        mhdRun.setForcingScheme(mhdPara.getFORCING_SCHEME());
        mhdRun.setRandomForcingType(mhdPara.getRANDOM_FORCING_TYPE());
        mhdRun.setForcingRange(mhdPara.getForcing_range());
        mhdRun.setInjections(mhdPara.getInjections());
        mhdRun.setBuoyancyEnabled(mhdPara.isBUOYANCY_ENABLED());
        mhdRun.setNb(mhdPara.getNb());
        mhdRun.setRotationEnabled(mhdPara.isROTATION_ENABLED());
        mhdRun.setOmega(mhdPara.getOmega());
        mhdRun.setMaintainField(mhdPara.isMAINTAIN_FIELD());
        mhdRun.setMaintainMux(mhdPara.getMaintain_mux());

        return mhdRun;
    }
}
