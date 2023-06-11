package com.example.websocketdemo.service;

import com.example.websocketdemo.entity.User;

public interface UserService {

    User login(String username, String password);
}
