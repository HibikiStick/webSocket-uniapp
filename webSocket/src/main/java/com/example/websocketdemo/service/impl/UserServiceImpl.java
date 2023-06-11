package com.example.websocketdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.websocketdemo.entity.User;
import com.example.websocketdemo.mapper.UserMapper;
import com.example.websocketdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(String username, String password) {
        QueryWrapper<User> sql = new QueryWrapper<>();
        sql.eq("name",username);
        sql.eq("password",password);
        return userMapper.selectOne(sql);
    }
}
