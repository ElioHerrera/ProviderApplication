package com.provider.other;

import com.provider.entities.Rol;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JwtUsuario {

    // Clave secreta más larga y segura
    private SecretKey claveSecreta = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private long duracion = 86400000;  // 24 horas en milisegundos

    public String crearToken(String usernameUsuario, Set<Rol> roles) {

        Consola.verde("Crear Token");
        // Extraer los nombres de los roles (RolUsuario.rol) como una lista de cadenas
        List<String> listaRoles = roles.stream()
                .map(rol -> rol.getRol().name())  // extrae el nombre del rol
                .collect(Collectors.toList());

        // Crear los claims (datos del token)
        Claims claims = Jwts.claims().setSubject(usernameUsuario);
        claims.put("roles", listaRoles);

        Date ahora = new Date();
        Date expiracion = new Date(ahora.getTime() + duracion);

        Consola.aletaVerde("Token", "username - claveSecreta: ", usernameUsuario + " - " + claveSecreta);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(ahora)
                .setExpiration(expiracion)
                .signWith(claveSecreta)
                .compact();
    }

    // Método para validar el token
    public boolean validarToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(claveSecreta).build().parseClaimsJws(token);
            Consola.verde("Token valido");
            return true;
        } catch (Exception e) {
            Consola.rojo("Token invalido");
            return false;
        }
    }

    // Método para obtener el nombre de usuario del token
// Método para obtener el nombre de usuario del token
    public String obtenerNombreUsuario(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(claveSecreta)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}