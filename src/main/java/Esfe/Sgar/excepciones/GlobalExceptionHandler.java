package Esfe.Sgar.excepciones;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestClientException;
import jakarta.persistence.EntityNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(RestClientException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ResponseEntity<ErrorResponse> handleRestClientException(RestClientException ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.SERVICE_UNAVAILABLE.value(),
            "No se pudo conectar con el servicio de seguridad"
        );
        return new ResponseEntity<>(error, HttpStatus.SERVICE_UNAVAILABLE);
    }
}