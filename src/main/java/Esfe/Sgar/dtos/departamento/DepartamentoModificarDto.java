package Esfe.Sgar.dtos.departamento;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class DepartamentoModificarDto {
    
    @NotNull(message = "El ID del departamento es requerido")
    private Integer id;
    
    @NotBlank(message = "El nombre del departamento es requerido")
    @Size(max = 50, message = "El nombre no debe exceder los 50 caracteres")
    private String nombre;
}