package com.example.spring_example.config;

public class AppConfig {

    private static final Profile PROFILE = Profile.valueOf(
            System.getenv().getOrDefault("SPRING_PROFILES_ACTIVE", "DEV").toUpperCase()
    );

    public static String getVerificationUrl() {
        switch (PROFILE) {
            case DEV:
                return "http://localhost:8081/user/signup/verify?token=";
            case PROD:
                return "https://turbulencehub.in/user/signup/verify?token=";
            default:
                throw new IllegalStateException("Unknown profile: " + PROFILE);
        }
    }

    public static String getCompanyEmail() {
        return "vayusoftlabs@gmail.com";
    }

}
