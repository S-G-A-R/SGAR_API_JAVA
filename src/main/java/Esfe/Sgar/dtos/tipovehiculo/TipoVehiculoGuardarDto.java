package Esfe.Sgar.dtos.tipovehiculo;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class TipoVehiculoGuardarDto {

    @NotNull(message = "El tipo es requerido")
    private Byte tipo;

    @NotBlank(message = "La descripción es requerida")
    @Size(max = 200, message = "La descripción no debe exceder los 200 caracteres")
    private String descripcion;
}
