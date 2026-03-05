package com.smzs.mall.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smzs.mall.config.JwtConfig;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    
    @Autowired
    private JwtConfig jwtConfig;
    
    /**
     * 生成JWT token
     */
    public String generateToken(Long userId, String phone) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("phone", phone);
        
        // 获取过期时间，如果为空则使用默认值24小时
        Long expiration = jwtConfig.getExpiration();
        if (expiration == null) {
            expiration = 86400000L; // 24小时默认值
        }
        
        // 获取密钥，如果为空则使用默认值
        String secret = jwtConfig.getSecret();
        if (secret == null || secret.isEmpty()) {
            secret = "smzs-mall-default-secret-key";
        }
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(phone)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
    
    /**
     * 解析JWT token
     */
    public Claims parseToken(String token) {
        // 获取密钥，如果为空则使用默认值
        String secret = jwtConfig.getSecret();
        if (secret == null || secret.isEmpty()) {
            secret = "smzs-mall-default-secret-key";
        }
        
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
    
    /**
     * 验证token是否有效
     */
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 从token中获取用户ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        return ((Number) claims.get("userId")).longValue();
    }
    
    /**
     * 从token中获取手机号
     */
    public String getPhoneFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }
}