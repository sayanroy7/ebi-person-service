package com.ebi.app;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.List;

public class JwtSecurityTestUtils {

    public static String createToken(String username, List<String> scopes) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(username)
                .setIssuer("ebi-auth-service")
                .claim("scope", scopes)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + 180000))
                .signWith(SignatureAlgorithm.HS256, "ebi-auth-service-signing-key-256")
                .compact();
    }


    public static String createToken(String username, List<String> scopes, String issuer) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(username)
                .setIssuer(issuer)
                .claim("scope", scopes)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + 180000))
                .signWith(SignatureAlgorithm.HS256, "ebi-auth-service-signing-key-256")
                .compact();
    }
}
