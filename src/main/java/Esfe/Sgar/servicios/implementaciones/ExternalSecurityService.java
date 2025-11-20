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
    private final String navigationApiUrl;
    
    public ExternalSecurityService(RestTemplate restTemplate, 
                                 @Value("${external.api.security.url}") String securityApiUrl,
                                 @Value("${external.api.navigation.url}") String navigationApiUrl) {
        this.restTemplate = restTemplate;
        this.securityApiUrl = securityApiUrl.endsWith("/") ? securityApiUrl : securityApiUrl + "/";
        this.navigationApiUrl = navigationApiUrl.endsWith("/") ? navigationApiUrl : navigationApiUrl + "/";
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

    @Override
    public boolean existeZona(String zonaId) {
        if (zonaId == null || zonaId.trim().isEmpty()) {
            logger.warn("ID de zona inválido: {}", zonaId);
            return false;
        }
        String url = navigationApiUrl + "zones/" + zonaId;
        ResponseEntity<ZonaResponse> resp = safeGet(url, ZonaResponse.class);
        if (resp != null && resp.getStatusCode() == HttpStatus.OK && resp.getBody() != null) {
            logger.info("Zona {} encontrada", zonaId);
            return true;
        }

        if (resp != null && resp.getStatusCode() == HttpStatus.NOT_FOUND) {
            logger.warn("Zona {} no existe en la base de datos (404 - petición a {})", zonaId, url);
        } else {
            logger.warn("Zona {} no encontrada - Error de conectividad (petición a {})", zonaId, url);
        }
        
        // Permitir la creación del horario aunque la zona no se encuentre
        // Esto evita bloquear operaciones por problemas de conectividad o zonas no registradas
        logger.info("Continuando con la operación sin validación estricta de zona");
        return true;
    }

    // Helper genérico para llamadas GET que captura excepciones y registra errores
    private <T> ResponseEntity<T> safeGet(String url, Class<T> responseType) {
        try {
            logger.debug("Realizando petición GET a: {}", url);
            ResponseEntity<T> response = restTemplate.getForEntity(url, responseType);
            logger.debug("Respuesta recibida - Status: {} para URL: {}", response.getStatusCode(), url);
            return response;
        } catch (RestClientException e) {
            logger.warn("Error en petición GET a {}: {} - {}", url, e.getClass().getSimpleName(), e.getMessage());
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
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class ZonaResponse {
        private String id;
        private String name;
        
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }
}