package com.example.spring_example.models;

import com.example.spring_example.config.AppConfig;
import lombok.Getter;
import lombok.Setter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Paths;

@Getter
@Setter
public class HydroPara extends BasicPara {

    private String kind = "HYDRO";

    public HydroPara(String device, int device_rank, int dimension, int Nx, int Ny, int Nz, double nu, double eta, String time_scheme, double t_initial, double t_final, double dt) {
        super(device, device_rank, dimension, Nx, Ny, Nz, nu, eta, time_scheme, t_initial, t_final, dt);
    }

    public HydroPara() {
        super();
    }


    public boolean createParaFile(String id) {

//        this.setOutput_dir(Paths.get(AppConfig.getBaseOutputPath(),getTimeStamp()).toString().replace("\\","/"));
        String paraFile = Paths.get(AppConfig.getBaseParaPath(),"para.py").toString();
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(paraFile,true))) {

            Class<?> current = this.getClass();
            while (current != null) {
                for (Field field : current.getDeclaredFields()) {
                    field.setAccessible(true);
                    Object value = field.get(this);

                    writer.write(field.getName() + " = " + formatValue(value));
                    writer.newLine();
                }
                current = current.getSuperclass();
            }

            return true;
        } catch (IOException | IllegalAccessException e) {
            System.out.println(e.getMessage());
            return false;
        }

    }


    private String formatValue(Object value) {
        if (value instanceof String) {
            return "\"" + value + "\"";
        } else if(value instanceof Boolean) {
            return Boolean.parseBoolean(String.valueOf((Boolean) value)) ? "True" : "False";
        } else if((value instanceof int[])) {
            int[] arrayValue = (int[]) value;
            int length = arrayValue.length;
            StringBuilder stringRepr = new StringBuilder("[");
            for(int i = 0; i < length; i ++) {
                stringRepr.append(arrayValue[i]);
                if(i != length-1) {
                    stringRepr.append(",");
                }
            }
            stringRepr.append("]");
            return stringRepr.toString();
        } else if(value instanceof double[]) {
            double[] arrayValue = (double[]) value;
            int length = arrayValue.length;
            StringBuilder stringRepr = new StringBuilder("[");
            for(int i = 0; i < length; i ++) {
                stringRepr.append(arrayValue[i]);
                if(i != length-1) {
                    stringRepr.append(",");
                }
            }
            stringRepr.append("]");
            return stringRepr.toString();
        } else if(value instanceof int[][] arrayValue) {
            int outerLength = arrayValue.length;
            if(outerLength == 0) {
                return "[]";
            }
            int innerLength = arrayValue[0].length;
            StringBuilder stringRepr = new StringBuilder("[");
            for(int i = 0; i < outerLength; i ++) {
                stringRepr.append("[");
                for(int j = 0; j < innerLength; j++) {
                    stringRepr.append(arrayValue[i][j]);
                    if(j != innerLength-1) {
                        stringRepr.append(",");
                    }
                }

                stringRepr.append("]");

                if(i != outerLength - 1) {
                    stringRepr.append(",");
                }

            }
            stringRepr.append("]");
            return stringRepr.toString();
        }
        return String.valueOf(value);
    }

}