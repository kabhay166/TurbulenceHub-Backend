package com.example.spring_example.enums;


public enum JwtTokenType {
    ACCESS_TOKEN("access_token"),
    REFRESH_TOKEN("refresh_token");

    private String type;

    JwtTokenType(String type) {
        this.type = type;
    }
}