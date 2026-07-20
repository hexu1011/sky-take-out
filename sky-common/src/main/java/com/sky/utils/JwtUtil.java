package com.sky.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Map;

public class JwtUtil {

    /**
     * 由任意长度的秘钥字符串派生出满足 HS256 要求（≥256 位）的签名密钥。
     * jjwt 0.10+ 对 HMAC 密钥长度有强制校验，而项目中的秘钥较短，
     * 故用 SHA-256 摘要得到固定 256 位密钥，秘钥配置无需改动。
     *
     * @param secretKey 秘钥字符串
     * @return HS256 签名密钥
     */
    private static SecretKey getSigningKey(String secretKey) {
        try {
            byte[] keyBytes = MessageDigest.getInstance("SHA-256")
                    .digest(secretKey.getBytes(StandardCharsets.UTF_8));
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 不可用", e);
        }
    }

    /**
     * 生成jwt
     * 使用Hs256算法, 私匙使用固定秘钥
     *
     * @param secretKey jwt秘钥
     * @param ttlMillis jwt过期时间(毫秒)
     * @param claims    设置的信息
     * @return
     */
    public static String createJWT(String secretKey, long ttlMillis, Map<String, Object> claims) {
        // 生成JWT的过期时间
        Date exp = new Date(System.currentTimeMillis() + ttlMillis);

        return Jwts.builder()
                // 如果有私有声明，一定要先设置这个自己创建的私有的声明
                .claims(claims)
                // 设置过期时间
                .expiration(exp)
                // 设置签名使用的签名算法和签名使用的秘钥
                .signWith(getSigningKey(secretKey))
                .compact();
    }

    /**
     * Token解密
     *
     * @param secretKey jwt秘钥 此秘钥一定要保留好在服务端, 不能暴露出去, 否则sign就可以被伪造, 如果对接多个客户端建议改造成多个
     * @param token     加密后的token
     * @return
     */
    public static Claims parseJWT(String secretKey, String token) {
        return Jwts.parser()
                // 设置签名的秘钥
                .verifyWith(getSigningKey(secretKey))
                .build()
                // 设置需要解析的jwt
                .parseSignedClaims(token)
                .getPayload();
    }

}
