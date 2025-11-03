package Esfe.Sgar.dtos.municipio;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class MunicipioGuardarDto {
    
    @NotBlank(message = "El nombre del municipio es requerido")
    @Size(max = 80, message = "El nombre no debe exceder los 80 caracteres")
    private String nombre;
    
    @NotNull(message = "El ID del departamento es requerido")
    private Integer idDepartamento;
}