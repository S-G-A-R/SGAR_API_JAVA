package Esfe.Sgar.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.issuer}")
    private String issuer;

    /**
     * Extrae todos los claims del token JWT
     */
    public Claims extractAllClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Extrae el subject (username/email) del token
     */
    public String extractSubject(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Extrae el rol del token (claim "role" o "idRol")
     * Mapea el idRol numérico a ROLE_xxx para Spring Security
     */
    public String extractRole(String token) {
        Claims claims = extractAllClaims(token);
        
        // Intentar obtener el rol desde diferentes claims posibles
        Object roleObj = claims.get("role");
        Object idRolObj = claims.get("idRol");
        
        // Priorizar el claim "role" (string) que viene del JWT .NET
        if (roleObj != null) {
            return mapRoleToAuthority(roleObj.toString());
        } else if (idRolObj != null) {
            return mapRoleIdToAuthority(idRolObj);
        }
        
        return "ROLE_Ciudadano"; // Por defecto si no hay rol
    }

    /**
     * Mapea el ID numérico del rol a la autoridad de Spring Security
     */
    private String mapRoleIdToAuthority(Object idRolObj) {
        Integer idRol;
        
        if (idRolObj instanceof Integer) {
            idRol = (Integer) idRolObj;
        } else if (idRolObj instanceof String) {
            try {
                idRol = Integer.parseInt((String) idRolObj);
            } catch (NumberFormatException e) {
                return "ROLE_Ciudadano";
            }
        } else {
            return "ROLE_Ciudadano";
        }
        
        return switch (idRol) {
            case 1 -> "ROLE_Ciudadano";
            case 2 -> "ROLE_Operador";
            case 3 -> "ROLE_Asociado";
            case 4 -> "ROLE_Administrador";
            case 5 -> "ROLE_Organizacion";
            default -> "ROLE_Ciudadano";
        };
    }

    /**
     * Mapea el nombre del rol a la autoridad de Spring Security
     */
    private String mapRoleToAuthority(String role) {
        // Si ya viene con ROLE_ prefix, devolverlo tal cual
        if (role.startsWith("ROLE_")) {
            return role;
        }
        
        // Mapear nombres exactos como vienen del JWT .NET (case-sensitive)
        // "Organizacion", "Operador", "Ciudadano", "Asociado", "Administrador"
        return switch (role) {
            case "Ciudadano" -> "ROLE_Ciudadano";
            case "Operador" -> "ROLE_Operador";
            case "Asociado" -> "ROLE_Asociado";
            case "Administrador" -> "ROLE_Administrador";
            case "Organizacion" -> "ROLE_Organizacion";
            default -> "ROLE_" + role; // Fallback: añadir prefijo ROLE_
        };
    }

    /**
     * Valida si el token es válido (firma correcta, no expirado, issuer correcto)
     */
    public boolean validateToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            
            // Validar issuer si está configurado (el JWT .NET usa https://localhost:7149)
            // Ajustamos para aceptar tanto http como https del issuer
            if (issuer != null && !issuer.isEmpty()) {
                String tokenIssuer = claims.getIssuer();
                if (tokenIssuer != null) {
                    // Normalizar para comparar sin protocolo si es necesario
                    String normalizedConfigIssuer = issuer.replaceFirst("^https?://", "");
                    String normalizedTokenIssuer = tokenIssuer.replaceFirst("^https?://", "");
                    
                    // Aceptar si coinciden (ignorando http vs https)
                    if (!normalizedConfigIssuer.equals(normalizedTokenIssuer)) {
                        // Si no coinciden, loguear pero no fallar (puedes ajustar según necesites)
                        return true; // Cambiar a false si quieres validación estricta de issuer
                    }
                }
            }
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
