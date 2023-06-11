package com.example.websocketdemo.util;

import com.example.websocketdemo.entity.Token;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    /**
     * 獲取緩存失效時間
     * @param key
     * @param time
     * @return
     */
    public Boolean expire(final String key, final long time){
        try {
            if (time > 0) {
                redisTemplate.expire(key,time, TimeUnit.SECONDS);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根據key獲取value
     */
    public Object get(final String key){
        return key == null ? null: redisTemplate.opsForValue().get(key);
    }

    /**
     * 將KV鍵值對存入redis
     * @param key
     * @param value
     * @return
     */
    public Boolean setKV(final String key,final String value){
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param key
     * @param value
     * @param time
     * @return
     */
    public Boolean setKV(final String key,final String value,final long time) {
        try {
            redisTemplate.opsForValue().set(key,value,time);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean del(final String key){
        try {
            redisTemplate.opsForValue().getAndDelete(key);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



























}
