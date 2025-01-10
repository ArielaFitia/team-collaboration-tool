package com.ditto.teamcollaborationtool.security.jwt;

import com.ditto.teamcollaborationtool.security.service.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    // Secret key for signing tokens - read from application.properties
    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app.jwt-expiration-milliseconds}")
    private int jwtExpirationMs;

    // Creates a new JWT token when a user logs in successfully
    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        // Create a secure key for signing the token
        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())  // User identity
                .setIssuedAt(new Date())                  // When issued
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))  // When it expires
                .signWith(key, SignatureAlgorithm.HS256)  // Sign it with our secret key
                .compact();
    }

    // Reads username from token - like reading a passport
    public String getUserNameFromJwtToken(String token) {
        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Checks if a token is valid - like checking if a passport is genuine
    public boolean validateJwtToken(String token) {
        try {
            Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parse(token);
            return true;
        } catch (Exception e) {
            return false;  // Token is invalid
        }
    }
}
