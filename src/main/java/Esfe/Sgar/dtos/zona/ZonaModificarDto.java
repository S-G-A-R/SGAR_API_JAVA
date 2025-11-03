package Esfe.Sgar.dtos.zona;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ZonaModificarDto {
    
    @NotNull(message = "El ID de la zona es requerido")
    private Integer id;

    @NotBlank(message = "El nombre es requerido")
    @Size(max = 80, message = "El nombre no debe exceder los 80 caracteres")
    private String nombre;

    @NotNull(message = "El ID del distrito es requerido")
    private Integer idDistrito;

    @NotNull(message = "El ID de la alcaldía es requerido")
    private Integer idAlcaldia;

    @Size(max = 200, message = "La descripción no debe exceder los 200 caracteres")
    private String descripcion;
}