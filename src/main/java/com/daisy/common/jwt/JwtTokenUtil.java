package com.daisy.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Jwt Token 生成工具
 * @author wangyunpeng
 */
@Component
@Slf4j
public class JwtTokenUtil implements Serializable {
    private static final long serialVersionUID = -1L;

    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 从token 中获取声明
     * @param token
     * @return
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            log.error("通过token获取信息异常，token为:{}",token);
            claims = null;
        }
        return claims;
    }

    /**
     * 从 token 中获取用户名
     * @param token
     * @return
     */
    public String getUserNameFromToken(String token) {
        String username;
        try {
            final Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            log.error("通过token获取用户名异常，该Token为:{}",token);
            username = null;
        }
        return username;
    }

    /**
     * 从token中获取创建时间
     * @param token
     * @return
     */
    public Date getCreatedDateFromToken(String token) {
        Date created;
        try {
            final Claims claims = getClaimsFromToken(token);
            created = new Date((Long)claims.get(CLAIM_KEY_CREATED));
        } catch (Exception e) {
            log.error("从token中获取创建时间异常，该token为:{}",token);
            created = null;
        }
        return created;
    }

    /**
     * 从 token 中获取过期时间
     * @param token
     * @return
     */
    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            log.error("从token中获取过期时间异常，该token为:{}",token);
            expiration = null;
        }
        return expiration;
    }

    /**
     * 生成过期时间
     * @return
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    /**
     * 判断 token 是否过期
     * @param token
     * @return
     */
    private boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * token 的创建时间是否在最后一次修改密码之前
     * TODO 这里的修改密码是否可以认为是每次成功登陆都会修改密码
     * @param created
     * @param lastPasswordReset
     * @return
     */
    private boolean isCreatedBeforeLastPasswordReset(Date created,Date lastPasswordReset) {
        return (lastPasswordReset != null && lastPasswordReset.before(created));
    }

    /**
     * 生成token
     * @param userDetails
     * @return
     */
    public String generateToken(UserDetails userDetails) {
        Map<String,Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME,userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED,new Date());
        return generateToken(claims);
    }

    /**
     * 生成token
     * @param claims
     * @return
     */
    public String generateToken(Map<String,Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                // 设置过期时间
                .setExpiration(generateExpirationDate())
                // TODO 这里需要补充 signWith(..,str) 方法过期
                .signWith(Keys.secretKeyFor(SignatureAlgorithm.ES256))
                .compact();

    }

    /**
     * token 是否可以被刷新
     * @param token
     * @param lastPasswordReset 上次密码重置的时间
     * @return
     */
    public boolean canTokenBeRefreshed(String token,Date lastPasswordReset) {
        final Date created = getCreatedDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created,lastPasswordReset) && !isTokenExpired(token);
    }

    /**
     * 刷新 token
     * @param token
     * @return
     */
    public String refershToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = getClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATED,new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }


    /**
     * 校验 token 是否有效
     * @param token
     * @param userDetails
     * @return
     */
    public boolean validateToken(String token,UserDetails userDetails) {
        Assert.notNull(token,"token 不能为空");
        JwtUser jwtUser = (JwtUser)userDetails;
        final String username = getUserNameFromToken(token);
        final Date created = getCreatedDateFromToken(token);
        return (
                jwtUser != null && username.equals(jwtUser.getUsername())
                && !isTokenExpired(token)
                && !isCreatedBeforeLastPasswordReset(created,((JwtUser) userDetails).getLastPasswordResetDate())
        );
    }





}
