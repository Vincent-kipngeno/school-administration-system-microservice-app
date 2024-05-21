package com.sas.apigw.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;


@Service
public class JwtUtil {

    private static final String SECRET_KEY = "VgS8dEKGfUG30s5g9CwEgOo9LpaKQIagv+SVzrnkZZdhTJwlReKCAGKOnafamMEqbx9kIEe23XYLFam+74Nlozmn76oVD+FneVbyC4EqxMmccmTiaT+JpqOCNM7M/VBuJNHX3xCveL65sl+EXijFMpYz0X1oBseei/71rS6HoKsZ0OVIzA0T/8xCNLBqjr61mSkekdMVZDCzteWLAE1hyadN85Xlp8+ZBdFYQ2GYWfiYdqdThkERiwebxEyOWNQahurutcRUvR6SjCbr5urSdyRxKNAqYp2GXumSVkDA0D6bz0Ns7ADCKsCjjmXMrIGNlcnHPLgjymzeQThXvl6zZ+f6UCfzKhtZDFpE/aRM510=\r\n";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenValid(String token) {
        final String username = extractUsername(token);
        return !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
