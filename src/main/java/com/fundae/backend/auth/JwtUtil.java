package com.fundae.backend.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    // Clave secreta de 256 bits
    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Tiempo de expiración: 1 día (en milisegundos)
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;

    // Genera el token JWT
    public String generateToken(String correo, String rol, Integer userId) {
        return Jwts.builder()
                .setSubject(correo)
                .claim("role", rol)
                .claim("userId", userId)  // Añadimos el userId al token
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }


    // Obtiene correo desde el token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Obtiene rol desde el token
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    // Validación de token
    public boolean validateToken(String token, String username) {
        return extractUsername(token).equals(username) && !isTokenExpired(token);
    }

    // Helpers internos
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
