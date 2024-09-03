package com.atguigu.lease.common.utils;

import com.atguigu.lease.common.exception.LeaseException;
import com.atguigu.lease.common.result.ResultCodeEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * @author xyzZero3
 * @date 2024/8/20 16:06
 */
public class JwtUtil {

    private static SecretKey secretKey = Keys.hmacShaKeyFor("qMl85MPBAeLRNaZYCSoOfKY1SP1mYFo3".getBytes());

    /**
     * 创建token
     * @param userId
     * @param username
     * @return
     */
    public static String createToken(Long userId, String username) {
        String jwt = Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + 3600000 * 24 * 7)) //过期时间:到什么时候过期(单位毫秒)
                .setSubject("LOGIN_USER")
                .claim("userId", userId) // 设置自定义参数
                .claim("username", username)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
        return jwt;
    }

    public static Claims parseToken(String token) {
        if (token == null) {
            throw new LeaseException(ResultCodeEnum.ADMIN_LOGIN_AUTH);
        }
        try {
            JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build();
            Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);
            return claimsJws.getBody();
        } catch (ExpiredJwtException e) {
            throw new LeaseException(ResultCodeEnum.TOKEN_EXPIRED);
        } catch (JwtException e) {
            throw new LeaseException(ResultCodeEnum.TOKEN_INVALID);
        }

    }

    public static void main(String[] args) {
        System.out.println(createToken(8L, "2236373829@qq.com"));
    }

}
