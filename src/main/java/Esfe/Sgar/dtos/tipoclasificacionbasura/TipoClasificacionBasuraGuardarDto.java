package Esfe.Sgar.dtos.tipoclasificacionbasura;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class TipoClasificacionBasuraGuardarDto {

    @NotBlank(message = "El nombre es requerido")
    @Size(max = 50, message = "El nombre no debe exceder los 50 caracteres")
    private String nombre;
}
