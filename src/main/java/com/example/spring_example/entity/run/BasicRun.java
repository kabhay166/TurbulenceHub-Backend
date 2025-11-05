package com.example.spring_example.entity.run;


import com.example.spring_example.entity.BasicEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class BasicRun {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private String device;
    @NotNull
    private int deviceRank;
    @NotNull
    private String complexDtype;
    @NotNull
    private String realDtype;
    @NotNull
    private int dimension;
    private int nx;
    private int ny;
    private int nz;

    private double nu;
    private double eta;
    private double kappa;
    private String timeScheme;
    private double tInitial;
    private double tFinal;
    private double dt;
    private boolean fixedDt;
    @NotNull
    private String timeOfRunPath;

    @NotNull
    private boolean inputSetCase;
    @NotNull
    private String inputCase;
    @NotNull
    private boolean inputFromFile;
    @NotNull
    private boolean inputRealField;
    @NotNull
    private boolean inputElsasser;
    @NotNull
    private String inputFileName;
    @NotNull
    private boolean outputRealField;
//    @NotNull
//    private String outputDir;
    @NotNull
    private double[] l;
    @NotNull
    private boolean boxSizeDefault;
    @NotNull
    private double courantNo;
    @NotNull
    private double t_eps;
    @NotNull
    private boolean printParameters;
    @NotNull
    private boolean  runtimeSave;
    @NotNull
    private boolean livePlot;
    @NotNull
    private boolean useBinding;
    @NotNull
    private boolean planarSpectra;
    @NotNull
    private boolean saveVorticity;
    @NotNull
    private boolean saveVecpot;

    @Convert(converter = IntArray2DConverter.class)
    private int[][] modesSave;
    @NotNull
    private int iterFieldSaveStart;
    @NotNull
    private int iterFieldSaveInter;
    @NotNull
    private int iterGlobalEnergyPrintStart;
    @NotNull
    private int iterGlobalEnergyPrintInter;

    @NotNull
    private int iterEkTkSaveStart;
    @NotNull
    private int iterEkTkSaveInter;
    @NotNull
    private int iterModesSaveStart;
    @NotNull
    private int iterModesSaveInter;

    private double rac;
    private double ra;
    private double pr;
    private boolean hypoDissipation;
    private boolean hyperDissipation;
    private double nuHypo;
    private double nuHypoPower;
    private double nuHypoCutoff;
    private double nuHyper;
    private double nuHyperPower;
    private double etaHypo;
    private double etaHypoPower;
    private double etaHypoCutoff;
    private double etaHyper;
    private double etaHyperPower;
    private double kappaHypo;
    private double kappaHypoPower;
    private double kappaHypoCutoff;
    private double kappaHyper;
    private double kappaHyperPower;
    private boolean forcingEnabled;
    private String forcingScheme;
    private String randomForcingType;
    private int[] forcingRange;
    private double[] injections;
    private boolean buoyancyEnabled;
    private double nb;
    private boolean rotationEnabled;
    private double[] omega;
    private boolean maintainField;
    private double maintainMux;
}

class IntArray2DConverter implements AttributeConverter<int[][], String> {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(int[][] attribute) {
        try {
            return mapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int[][] convertToEntityAttribute(String dbData) {
        try {
            return mapper.readValue(dbData, int[][].class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
