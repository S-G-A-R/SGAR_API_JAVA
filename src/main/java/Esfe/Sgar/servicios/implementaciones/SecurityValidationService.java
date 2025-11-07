package Esfe.Sgar.servicios.implementaciones;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class SecurityValidationService {
    private final RestTemplate restTemplate;
    private final String securityApiUrl;
    
    @Autowired
    public SecurityValidationService(RestTemplate restTemplate, @Value("${external.api.security.url}") String securityApiUrl) {
        this.restTemplate = restTemplate;
        this.securityApiUrl = securityApiUrl;
    }
    
    // Helper que centraliza la llamada y el manejo de errores para comprobar si un recurso existe
    private boolean resourceExists(String url) {
        try {
            ResponseEntity<?> response = restTemplate.getForEntity(url, Map.class);
            return response != null && response.getStatusCode().is2xxSuccessful();
        } catch (RestClientException e) {
            // Si ocurre cualquier error (404, timeout, etc.) consideramos que no existe
            return false;
        }
    }

    public boolean validateOrganizacionExists(Integer organizacionId) {
        if (organizacionId == null || organizacionId <= 0) return false;
        String base = securityApiUrl != null && securityApiUrl.endsWith("/") ? securityApiUrl : securityApiUrl + "/";
        String url = base + "api/organization/" + organizacionId;
        return resourceExists(url);
    }

    public boolean validateOperadorExists(Integer operadorId) {
        if (operadorId == null || operadorId <= 0) return false;
        String base = securityApiUrl != null && securityApiUrl.endsWith("/") ? securityApiUrl : securityApiUrl + "/";
        String url = base + "api/operador/" + operadorId;
        return resourceExists(url);
    }
}