package Esfe.Sgar.dtos.queja;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class QuejaModificarDto {
    
    @NotNull(message = "El ID de la queja es requerido")
    private Integer id;

    @NotBlank(message = "El título es requerido")
    @Size(max = 80, message = "El título no debe exceder los 80 caracteres")
    private String titulo;

    @Size(max = Integer.MAX_VALUE, message = "La descripción es demasiado larga")
    private String descripcion;

    @NotNull(message = "El ID del ciudadano es requerido")
    private Integer idCiudadano;

    private byte[] archivo;

    @NotBlank(message = "El tipo de situación es requerido")
    @Size(max = 20, message = "El tipo de situación no debe exceder los 20 caracteres")
    private String tipoSituacion;

    @NotNull(message = "El estado es requerido")
    @Min(value = 0, message = "El estado debe ser un valor entre 0 y 127")
    @Max(value = 127, message = "El estado debe ser un valor entre 0 y 127")
    private Byte estado;

    @Size(max = 255, message = "El motivo no debe exceder los 255 caracteres")
    private String motivo;
}