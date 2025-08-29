package com.ashish.splitwise.UserService.Security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.antlr.v4.runtime.Token;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long Expiration = 1000*60*60;

    public static String generateToken(String email){
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+Expiration))
                .signWith(key)
                .compact();
    }

    public static String extractEmail(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    public static boolean isTokenValid(String token, String email){
        return email.equals(extractEmail(token)) && !isTokenExpired(token);
    }

    public static boolean isTokenExpired(String token){
        Date exp = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return exp.before(new Date());
    }
}
