package auth.utils;

import java.util.Date;

import auth.exception.NotValidJwtTokenException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;


@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.token.validity}")
    private int jwtExpirationMs;

    public String generateJwtToken(String username) {

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            throw new NotValidJwtTokenException("Invalid JWT signature: " + e.getMessage());
        } catch (MalformedJwtException e) {
            throw new NotValidJwtTokenException("Invalid JWT token: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            throw new NotValidJwtTokenException("JWT token is expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            throw new NotValidJwtTokenException("WT token is unsupported: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new NotValidJwtTokenException("JWT claims string is empty: " + e.getMessage());
        }
    }
}