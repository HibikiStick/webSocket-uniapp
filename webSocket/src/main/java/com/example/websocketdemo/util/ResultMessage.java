package com.example.websocketdemo.util;

import lombok.Data;

@Data
public class ResultMessage {
    private boolean isSystem;
    private String fromName;
    private Object message;

}
