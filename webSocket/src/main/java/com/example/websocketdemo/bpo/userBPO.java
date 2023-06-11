package com.example.websocketdemo.bpo;

import com.example.websocketdemo.entity.Loginlog;
import com.example.websocketdemo.entity.User;
import com.example.websocketdemo.mapper.LoginlogMapper;
import com.example.websocketdemo.service.UserService;
import com.example.websocketdemo.util.JwtUtils;
import com.example.websocketdemo.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.UUID;

@Component
@Slf4j
@CrossOrigin
public class userBPO {
    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private UserService userService;
    @Autowired
    private LoginlogMapper loginlogMap;

    public String userLogin(String name,String password){
        User user = userService.login(name,password);
        if (user == null){
            return "0";
        }
        String token = JwtUtils.getToken(user.getId());
        if (redisUtils.setKV(token,user.getId(),900)){
            Loginlog loginlog = new Loginlog();
            loginlog.setUsername(name);
            loginlog.setLoginId(UUID.randomUUID().toString());
            loginlog.setToken(token);
            loginlogMap.insert(loginlog);
            return token;
        }else {
            log.info("緩存錯誤，可能沒有連接上redis");
            return "-1";
        }
    }
}
