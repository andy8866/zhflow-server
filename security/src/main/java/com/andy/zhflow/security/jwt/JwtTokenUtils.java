package com.andy.zhflow.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;

@Slf4j
public class JwtTokenUtils {
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    private static  String SECRET = "NgEQqhe9";
    private static final String ISS = "zhflow";

    // 角色的key
    private static final String ROLE_CLAIMS = "role";

    // 过期时间是3600秒，既是1个小时
    private static final long EXPIRATION = 60 * 60L;

    // 选择了记住我之后的过期时间为7天
    private static final long EXPIRATION_REMEMBER = 7 * 24 * 60 * 60L;

    /**
     * 创建token
     *
     * @param username
     * @param role
     * @param isRememberMe
     * @return
     */
    public static String createToken(String username, String role, boolean isRememberMe) {
        long expiration = isRememberMe ? EXPIRATION_REMEMBER : EXPIRATION;
        HashMap<String, Object> map = new HashMap<>();
        map.put(ROLE_CLAIMS, role);
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .setClaims(map)
                .setIssuer(ISS)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .compact();
    }

    /**
     * 从token中获取用户名
     *
     * @param token
     * @return
     */
    public static String getUsername(String token) {
        return getTokenBody(token).getSubject();
    }

    /**
     * 获取用户角色
     *
     * @param token
     * @return
     */
    public static String getUserRole(String token) {
        return (String) getTokenBody(token).get(ROLE_CLAIMS);
    }

    /**
     * 是否已过期
     *
     * @param token
     * @return
     */
    public static boolean isExpiration(String token) {
        return getTokenBody(token).getExpiration().before(new Date());
    }

    /**
     * 获取令牌体
     *
     * @param token
     * @return
     */
    private static Claims getTokenBody(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }
}