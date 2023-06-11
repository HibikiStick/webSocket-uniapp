package com.example.websocketdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan("com.example.websocketdemo.filter")
public class WebSocketDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebSocketDemoApplication.class, args);
    }

}
