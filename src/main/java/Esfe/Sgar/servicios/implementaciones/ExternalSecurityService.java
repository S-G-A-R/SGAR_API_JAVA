package Esfe.Sgar.servicios.implementaciones;

import Esfe.Sgar.servicios.Interfaces.IExternalSecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Service
public class ExternalSecurityService implements IExternalSecurityService {
    
    private static final Logger logger = LoggerFactory.getLogger(ExternalSecurityService.class);
    
    private final RestTemplate restTemplate;
    private final String securityApiUrl;
    
    public ExternalSecurityService(RestTemplate restTemplate, 
                                 @Value("${external.api.security.url}") String securityApiUrl) {
        this.restTemplate = restTemplate;
        this.securityApiUrl = securityApiUrl.endsWith("/") ? securityApiUrl : securityApiUrl + "/";
    }
    
    @Override
    public boolean existeOrganizacion(Integer organizacionId) {
        if (organizacionId == null || organizacionId <= 0) {
            logger.warn("ID de organización inválido: {}", organizacionId);
            return false;
        }
        String url = securityApiUrl + "api/organization/" + organizacionId;
        // Intentamos obtener la organización por su ID (menos carga que traer toda la lista)
        ResponseEntity<OrganizacionResponse> resp = safeGet(url, OrganizacionResponse.class);
        if (resp != null && resp.getStatusCode() == HttpStatus.OK && resp.getBody() != null) {
            logger.info("Organización {} encontrada", organizacionId);
            return true;
        }

        logger.warn("Organización {} no encontrada (petición a {})", organizacionId, url);
        return false;
    }
    
    @Override
    public boolean existeOperador(Integer operadorId) {
        if (operadorId == null || operadorId <= 0) {
            logger.warn("ID de operador inválido: {}", operadorId);
            return false;
        }
        String url = securityApiUrl + "api/operador/" + operadorId;
        ResponseEntity<OperadorResponse> resp = safeGet(url, OperadorResponse.class);
        if (resp != null && resp.getStatusCode() == HttpStatus.OK && resp.getBody() != null) {
            logger.info("Operador {} encontrado", operadorId);
            return true;
        }

        logger.warn("Operador {} no encontrado (petición a {})", operadorId, url);
        return false;
    }

    // Helper genérico para llamadas GET que captura excepciones y registra errores
    private <T> ResponseEntity<T> safeGet(String url, Class<T> responseType) {
        try {
            return restTemplate.getForEntity(url, responseType);
        } catch (RestClientException e) {
            logger.debug("Error en GET {}: {}", url, e.getMessage());
            return null;
        }
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class OrganizacionResponse {
        private Integer id;
        private String nombre;
        
        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class OperadorResponse {
        private Integer id;
        private String nombre;
        
        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
    }
}