package com.example.spring_example.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Getter
@Setter
public class MhdPara extends BasicPara {

    private String kind = "MHD";

    public MhdPara(String device, int device_rank, int dimension, int Nx, int Ny, int Nz, double nu, double eta, String time_scheme, double t_initial, double t_final, double dt) {
        super(device, device_rank, dimension, Nx, Ny, Nz, nu, eta, time_scheme, t_initial, t_final, dt);
    }

    public MhdPara() {
        super();
    }


    public boolean createParaFile(String id) {

        final String basePath = "C:/Users/kabha/OneDrive/Desktop/Programming/Vayusoft_Labs/TurbulenceHUB/backend/Tarang/";

        String paraFile = Paths.get(basePath,"para.py").toString();
        System.out.println("Creating para file: " + paraFile);
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
            return "\"" + value + "\"";  // Wrap strings in quotes
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
        return String.valueOf(value);  // Leave numbers as is
    }

    private String getTimeStamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
        String timestamp = LocalDateTime.now().format(formatter);
        return Paths.get("output", timestamp).toString();
    }
}