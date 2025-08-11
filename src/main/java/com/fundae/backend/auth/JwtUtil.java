package com.fundae.backend.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    // Clave secreta de 256 bits
    @Value("${jwt.secret.key}")
    private String SECRET_KEY;


    // Tiempo de expiración: 1 día (en milisegundos)
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Genera el token JWT
    public String generateToken(String correo, String rol, Integer userId) {
        return Jwts.builder()
                .setSubject(correo)
                .claim("role", rol)
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // 👈 Usa la clave correcta
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
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey()) // 👈 Usa la clave correcta
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
