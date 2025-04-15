package com.wwx.utils;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JwtUtils {
    private static String signKey = "thisiswwxsjwtsecretkeyusedforgraduationproject";
    private static Long expire = 43200000L;

    public static String generateJwt(Map<String, Object> claims) {
        SecretKey key = Keys.hmacShaKeyFor(signKey.getBytes(StandardCharsets.UTF_8));
        String jwt = Jwts.builder()
                .claims(claims)// 添加自定义数据
                .expiration(new Date(System.currentTimeMillis() + expire))// 设置过期时间
                .signWith(key,Jwts.SIG.HS256)// 设置签名
                .compact();//字符串返回值

        return jwt;
    }
    
    public static Claims parseJWT(String jwt){
        //生成 HMAC 密钥，根据提供的字节数组长度选择适当的 HMAC 算法，并返回相应的 SecretKey 对象。
        SecretKey key = Keys.hmacShaKeyFor(signKey.getBytes(StandardCharsets.UTF_8));

        // 得到DefaultJwtParser
        Claims claims =Jwts.parser()
            .verifyWith(key) // 传递密钥
            .build()
            .parseSignedClaims(jwt) //传递jwt令牌参数
            .getPayload(); // 获取- Payload(有效载荷）
        return claims;
    }
}
