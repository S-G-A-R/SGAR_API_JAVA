package Esfe.Sgar.dtos.foto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class FotoGuardarDto {
    
    @NotNull(message = "La imagen es requerida")
    private byte[] imagen;

    @Size(max = 100, message = "El tipo MIME no debe exceder los 100 caracteres")
    private String tipoMime;

    private Long tamano;
}
