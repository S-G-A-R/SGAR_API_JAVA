package Esfe.Sgar.dtos.tipovehiculo;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class TipoVehiculoModificarDto {

    @NotNull(message = "El id es requerido")
    private Integer id;

    @NotNull(message = "El tipo es requerido")
    private Byte tipo;

    @NotBlank(message = "La descripción es requerida")
    @Size(max = 200, message = "La descripción no debe exceder los 200 caracteres")
    private String descripcion;
}
