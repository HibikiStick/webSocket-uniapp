package com.example.websocketdemo.entity;

import lombok.Data;

@Data
public class Loginlog {

    private String loginId;

    private String username;

    private String session;

    private String token;
}
