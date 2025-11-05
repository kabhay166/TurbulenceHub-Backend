package com.example.spring_example.config;

public class SimulationConfig {

    private static final Profile PROFILE = Profile.valueOf(
            System.getenv().getOrDefault("SPRING_PROFILES_ACTIVE", "DEV").toUpperCase()
    );
    public static String getPythonPath() {
        switch (PROFILE) {
            case DEV:
                return "C:\\Users\\kabha\\OneDrive\\Desktop\\Programming\\Vayusoft_Labs\\TurbulenceHUB\\backend\\Tarang\\myvenv\\Scripts\\python.exe";
            case PROD:
                return "/home/vimal/Tarang/myvenv/bin/python";
            default:
                throw new IllegalStateException("Unknown profile: " + PROFILE);
        }
    }

    public static String getTarangScriptPath() {
        switch (PROFILE) {
            case DEV:
                return "C:\\Users\\kabha\\OneDrive\\Desktop\\Programming\\Vayusoft_Labs\\TurbulenceHUB\\backend\\Tarang\\tarang_gui.py";
            case PROD:
                return "/home/vimal/Tarang/tarang_gui.py";
            default:
                throw new IllegalStateException("Unknown profile: " + PROFILE);
        }
    }

    public static String getAnalyzeScriptPath() {
        switch(PROFILE) {
            case DEV:
                return "C:\\Users\\kabha\\OneDrive\\Desktop\\Programming\\Vayusoft_Labs\\Tarang_Full\\Tarang\\post_proc\\post_proc.py";

            case PROD:
                return "/home/vimal/Tarang/post_proc/post_proc.py";

            default:
                throw new IllegalStateException("Unknow profile: " + PROFILE);
        }
    }
}
