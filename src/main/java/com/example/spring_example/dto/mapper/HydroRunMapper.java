package com.example.spring_example.dto.mapper;

import com.example.spring_example.entity.run.HydroRun;
import com.example.spring_example.models.HydroPara;

public class HydroRunMapper {

   public static HydroRun convertHydroParaToHydroRun(HydroPara hydroPara,HydroRun hydroRun) {
       hydroRun.setDevice(hydroPara.getDevice());
       hydroRun.setDeviceRank(hydroPara.getDevice_rank());
       hydroRun.setComplexDtype(hydroPara.getComplex_dtype());
       hydroRun.setRealDtype(hydroPara.getReal_dtype());
       hydroRun.setDimension(hydroPara.getDimension());
       hydroRun.setNx(hydroPara.getNx());
       hydroRun.setNy(hydroPara.getNy());
       hydroRun.setNz(hydroPara.getNz());
       hydroRun.setNu(hydroPara.getNu());
       hydroRun.setEta(hydroPara.getEta());
       hydroRun.setKappa(hydroPara.getKappa());
       hydroRun.setTimeScheme(hydroPara.getTime_scheme());
       hydroRun.setTInitial(hydroPara.getT_initial());
       hydroRun.setTFinal(hydroPara.getT_final());
       hydroRun.setDt(hydroPara.getDt());
       hydroRun.setFixedDt(hydroPara.isFIXED_DT());
       hydroRun.setInputSetCase(hydroPara.isINPUT_SET_CASE());
       hydroRun.setInputCase(hydroPara.getInput_case());
       hydroRun.setInputFromFile(hydroPara.isINPUT_FROM_FILE());
       hydroRun.setInputRealField(hydroPara.isINPUT_REAL_FIELD());
       hydroRun.setInputElsasser(hydroPara.isINPUT_ELSASSER());
       hydroRun.setInputFileName(hydroPara.getInput_file_name());
       hydroRun.setOutputRealField(hydroPara.isOUTPUT_REAL_FIELD());
       hydroRun.setL(hydroPara.getL());
       hydroRun.setBoxSizeDefault(hydroPara.isBOX_SIZE_DEFAULT());
       hydroRun.setCourantNo(hydroPara.getCourant_no());
       hydroRun.setT_eps(hydroPara.getT_eps());
       hydroRun.setPrintParameters(hydroPara.isPRINT_PARAMETERS());
       hydroRun.setRuntimeSave(hydroPara.isRUNTIME_SAVE());
       hydroRun.setLivePlot(hydroPara.isLIVE_PLOT());
       hydroRun.setUseBinding(hydroPara.isUSE_BINDING());
       hydroRun.setPlanarSpectra(hydroPara.isPLANAR_SPECTRA());
       hydroRun.setSaveVorticity(hydroPara.isSAVE_VORTICITY());
       hydroRun.setSaveVecpot(hydroPara.isSAVE_VECPOT());
       hydroRun.setModesSave(hydroPara.getModes_save());
       hydroRun.setIterFieldSaveStart(hydroPara.getIter_field_save_start());
       hydroRun.setIterFieldSaveStart(hydroPara.getIter_field_save_inter());
       hydroRun.setIterGlobalEnergyPrintStart(hydroPara.getIter_glob_energy_print_start());
       hydroRun.setIterGlobalEnergyPrintInter(hydroPara.getIter_glob_energy_print_inter());
       hydroRun.setIterEkTkSaveStart(hydroPara.getIter_ekTk_save_start());
       hydroRun.setIterEkTkSaveInter(hydroPara.getIter_ekTk_save_inter());
       hydroRun.setIterModesSaveStart(hydroPara.getIter_modes_save_start());
       hydroRun.setIterModesSaveInter(hydroPara.getIter_modes_save_inter());
       hydroRun.setRac(hydroPara.getRac());
       hydroRun.setRa(hydroPara.getRa());
       hydroRun.setPr(hydroPara.getPr());
       hydroRun.setHyperDissipation(hydroPara.isHYPER_DISSIPATION());
       hydroRun.setNuHypo(hydroPara.getNu_hypo());
       hydroRun.setNuHypoPower(hydroPara.getNu_hypo_power());
       hydroRun.setNuHypoCutoff(hydroPara.getNu_hypo_cutoff());
       hydroRun.setNuHyper(hydroPara.getNu_hyper());
       hydroRun.setNuHyperPower(hydroPara.getNu_hyper_power());
       hydroRun.setEtaHypo(hydroPara.getEta_hypo());
       hydroRun.setEtaHypoPower(hydroPara.getEta_hypo_power());
       hydroRun.setEtaHypoCutoff(hydroPara.getEta_hypo_cutoff());
       hydroRun.setEtaHyper(hydroPara.getEta_hyper());
       hydroRun.setEtaHyperPower(hydroPara.getEta_hyper_power());
       hydroRun.setKappaHypo(hydroPara.getKappa_hypo());
       hydroRun.setKappaHypoPower(hydroPara.getKappa_hypo_power());
       hydroRun.setKappaHypoCutoff(hydroPara.getKappa_hypo_cutoff());
       hydroRun.setKappaHyper(hydroPara.getKappa_hyper());
       hydroRun.setKappaHyperPower(hydroPara.getKappa_hyper_power());
       hydroRun.setForcingEnabled(hydroPara.isFORCING_ENABLED());
       hydroRun.setForcingScheme(hydroPara.getFORCING_SCHEME());
       hydroRun.setRandomForcingType(hydroPara.getRANDOM_FORCING_TYPE());
       hydroRun.setForcingRange(hydroPara.getForcing_range());
       hydroRun.setInjections(hydroPara.getInjections());
       hydroRun.setBuoyancyEnabled(hydroPara.isBUOYANCY_ENABLED());
       hydroRun.setNb(hydroPara.getNb());
       hydroRun.setRotationEnabled(hydroPara.isROTATION_ENABLED());
       hydroRun.setOmega(hydroPara.getOmega());
       hydroRun.setMaintainField(hydroPara.isMAINTAIN_FIELD());
       hydroRun.setMaintainMux(hydroPara.getMaintain_mux());

       return hydroRun;
   }
}
