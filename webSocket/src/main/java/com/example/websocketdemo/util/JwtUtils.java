package com.example.websocketdemo.util;


import io.jsonwebtoken.*;

import java.util.Date;

public class JwtUtils {

    //過期時間3天
    private static final long EXPECTED_TIMEOUT = 72 * 60 * 60 * 1000;

    //密鑰
    private static final String TOKEN_SECRET = "privateKey";

    //獲取token
    public static String getToken(String id){
        try {
            Date date = new Date(System.currentTimeMillis() + EXPECTED_TIMEOUT);
            String jwtBuilder = Jwts.builder()
                    //類型
                    .setHeaderParam("Type","JWT")
                    .setHeaderParam("alg","HS256")
                    //過期時間
                    .setExpiration(date)
                    .setId(id)
                    .signWith(SignatureAlgorithm.HS256,TOKEN_SECRET)
                    .compact();
            return jwtBuilder;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String verify(String token){
        try {
            JwtParser jwtParser = Jwts.parser();
            Jws<Claims> claims = jwtParser.setSigningKey(TOKEN_SECRET).parseClaimsJws(token);
            Claims claim = claims.getBody();
            return claim.getId();
        }catch (Exception e){
            e.printStackTrace();
            return "0";
        }
    }
}
