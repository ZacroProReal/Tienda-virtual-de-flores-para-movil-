package com.example.virtualShop.seguridad;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "clave-secreta-segura-para-jwt-virtual-shop-123456";
    private static final long EXPIRATION_TIME_LOGIN = 1000 * 60 * 60 * 10;  // 10 horas
    private static final long EXPIRATION_TIME_RESTABLECER = 1000 * 60 * 60; // 1 hora

    private Key getKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // Token normal para login
    public String generarToken(String correo) {
        return Jwts.builder()
                .setSubject(correo)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_LOGIN))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Token especial para restaurar cuenta
    public String generarTokenRestauracion(String correo) {
        return Jwts.builder()
                .setSubject(correo)
                .claim("restauracion", true)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_RESTABLECER))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extraerCorreo(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Validar token login normal: válido y NO debe ser token de restauración
    public boolean validarToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token);

            Claims claims = claimsJws.getBody();
            Boolean esRestauracion = claims.get("restauracion", Boolean.class);

            // Si el claim "restauracion" es true, no es válido como token login normal
            if (esRestauracion != null && esRestauracion) {
                return false;
            }
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
    // Validar token restauracion: válido + debe tener claim restauracion = true
    public boolean validarTokenRestauracion(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token);

            Claims claims = claimsJws.getBody();
            Boolean esRestauracion = claims.get("restauracion", Boolean.class);
            return esRestauracion != null && esRestauracion;
        } catch (JwtException e) {
            return false;
        }
    }
}
