package Esfe.Sgar.dtos.marca;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class MarcaGuardarDto {

    @NotBlank(message = "El nombre es requerido")
    @Size(max = 100, message = "El nombre no debe exceder los 100 caracteres")
    private String nombre;

    @NotBlank(message = "El modelo es requerido")
    @Size(max = 150, message = "El modelo no debe exceder los 150 caracteres")
    private String modelo;

    @NotBlank(message = "El año de fabricación es requerido")
    @Size(min = 4, max = 4, message = "El año de fabricación debe tener 4 caracteres")
    @Pattern(regexp = "\\d{4}", message = "El año de fabricación debe ser un número de 4 dígitos")
    private String yearOfFabrication;
}
