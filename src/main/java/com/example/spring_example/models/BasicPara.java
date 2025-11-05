package com.example.spring_example.models;


import com.example.spring_example.config.AppConfig;
import lombok.Getter;
import lombok.Setter;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class BasicPara {
    private String device = "GPU";
    private int device_rank = 0;
    private String complex_dtype = "complex";
    private String real_dtype = "float64";
    private int dimension;
    private String para_dir = AppConfig.getBaseParaPath();
    private int Nx;
    private int Ny;
    private int Nz;
    private double nu;
    private double eta;
    private double kappa = 0.01;
    private String time_scheme;
    private double t_initial;
    private double t_final;
    private double dt;
    private boolean FIXED_DT = true;
    private String output_dir;
    private boolean INPUT_SET_CASE = true;
    private String input_case = "custom";
    private boolean INPUT_FROM_FILE = false;
    private boolean INPUT_REAL_FIELD = false;
    private boolean INPUT_ELSASSER = false;
    private String input_file_name = "init_cond.h5";
    private String input_dir = "";
    private boolean OUTPUT_REAL_FIELD = false;
    private double[] L = {2*Math.PI,2*Math.PI,2*Math.PI};
    private boolean BOX_SIZE_DEFAULT = true;
    private double Courant_no = 0.5;
    private double t_eps = 1E-8;
    private boolean PRINT_PARAMETERS = true;
    private boolean  RUNTIME_SAVE = true;
    private boolean LIVE_PLOT = false;
    private boolean USE_BINDING = true;
    private boolean PLANAR_SPECTRA = false;
    private boolean SAVE_VORTICITY = false;
    private boolean SAVE_VECPOT = false;
    private int[][] modes_save = new int[0][0];
    private int iter_field_save_start = 0;
    private int iter_field_save_inter = 10;
    private int iter_glob_energy_print_start = 0;
    private int iter_glob_energy_print_inter = 1;
    private int iter_ekTk_save_start = 0;
    private int iter_ekTk_save_inter = 10;
    private int iter_modes_save_start = 0;
    private int iter_modes_save_inter = 100;
    private double Rac = 27.0* Math.pow(Math.PI,4)/4;
    private double Ra = 5e5*Rac;
    private double Pr = 6.8;
    private boolean HYPO_DISSIPATION = true;
    private boolean HYPER_DISSIPATION = true;
    private double nu_hypo = 1;
    private double nu_hypo_power = -2;
    private double nu_hypo_cutoff = -1;
    private double nu_hyper = 1E-4;
    private double nu_hyper_power = 2;
    private double eta_hypo = 1;
    private double eta_hypo_power = -2;
    private double eta_hypo_cutoff = -1;
    private double eta_hyper = 1E-4;
    private double eta_hyper_power = 2;
    private double kappa_hypo = 1;
    private double kappa_hypo_power = -2;
    private double kappa_hypo_cutoff = -1;
    private double kappa_hyper = 1E-4;
    private double kappa_hyper_power = 2;
    private boolean FORCING_ENABLED = false;
    private String FORCING_SCHEME = "random";
    private String RANDOM_FORCING_TYPE = "u";
    private int[] forcing_range = {4, 5};
    private double[] injections = {0, 0, 0};
    private boolean BUOYANCY_ENABLED = false;
    private double Nb = 0;
    private boolean ROTATION_ENABLED = false;
    private double[] Omega = {0, 0, 0};
    private boolean MAINTAIN_FIELD = false;
    private double maintain_mux = 1;
    private boolean VALIDATE_SOLVER = false;

    public BasicPara(String device, int device_rank, int dimension, int Nx, int Ny, int Nz, double nu, double eta, String time_scheme, double t_initial, double t_final, double dt) {
        this.device = device;
        this.device_rank = device_rank;
        this.dimension = dimension;
        this.Nx = Nx;
        this.Ny = Ny;
        this.Nz = Nz;
        this.nu = nu;
        this.eta = eta;
        this.time_scheme = time_scheme;
        this.t_initial = t_initial;
        this.t_final = t_final;
        this.dt = dt;
    }

    public BasicPara() {

    }



    public static String getTimeStamp(ZonedDateTime timeOfRun) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
        String timestamp = timeOfRun.format(formatter);
        return Paths.get("", timestamp).toString();
    }
}
