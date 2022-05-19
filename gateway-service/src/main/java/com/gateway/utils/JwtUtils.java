package com.gateway.utils;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;

    public String getUserNameFromToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            //logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            //logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            //logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            //logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            //logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}