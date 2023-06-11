package com.example.websocketdemo.controller;


import com.example.websocketdemo.bpo.userBPO;
import com.example.websocketdemo.entity.Token;
import com.example.websocketdemo.entity.User;
import com.example.websocketdemo.service.UserService;
import com.example.websocketdemo.util.JwtUtils;
import com.example.websocketdemo.util.RedisUtils;
import com.example.websocketdemo.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * @author cuixi
 */
@Controller
@RequestMapping("/login")
@CrossOrigin
@Slf4j
public class UserController {

    @Autowired
    private userBPO userBPO;
    @Autowired
    private RedisUtils redisUtils;

    @PostMapping("/login")
    @ResponseBody
    public Result login(@RequestBody User user){
        Result result = new Result();
        String toKen = userBPO.userLogin(user.getName(), user.getPassword());
        if (!"0".equals(toKen)){
            result.setFlag(true);
            result.setMessage(toKen);
            log.info("用戶"+ user.getName() + "登錄");
        }else {
            result.setFlag(false);
            result.setMessage("登陸失敗");
        }
        return result;
    }

    @PostMapping("/getToken")
    @ResponseBody
    public Result getToken(@RequestBody Token token){
        Result result = new Result();
        if (StringUtils.isBlank(token.getToken())){
            result.setFlag(false);
            return result;
        }
        if (redisUtils.get(token.getToken()) == null){
            result.setFlag(false);
            return result;
        }
        String id = JwtUtils.verify(token.getToken());
        if (StringUtils.isEmpty(id) || "0".equals(id)){
            result.setFlag(false);
            return result;
        }
        //token有效确定后生成新的token
        String newToken = JwtUtils.getToken(id);
        if (!redisUtils.del(token.getToken())){
            result.setFlag(false);
            result.setMessage("token删除失败");
            return result;
        }
        if (!redisUtils.setKV(newToken,id,900)){
            result.setFlag(false);
            result.setMessage("token创建失败");
            return result;
        }
        result.setFlag(true);
        result.setMessage(newToken);
        System.out.println(result.getMessage());
        return result;
    }
}
