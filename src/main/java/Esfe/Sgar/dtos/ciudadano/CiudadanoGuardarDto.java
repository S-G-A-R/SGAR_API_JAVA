package Esfe.Sgar.dtos.ciudadano;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CiudadanoGuardarDto {
    
    @NotBlank(message = "El nombre es requerido")
    @Size(max = 120, message = "El nombre no debe exceder los 120 caracteres")
    private String nombre;

    @Size(max = 120, message = "El apellido no debe exceder los 120 caracteres")
    private String apellido;

    @NotBlank(message = "El DUI es requerido")
    @Pattern(regexp = "^[0-9]{9}[0-9]$", message = "El DUI debe tener 10 dígitos")
    private String dui;

    @NotBlank(message = "El correo es requerido")
    @Email(message = "El formato del correo no es válido")
    @Size(max = 255, message = "El correo no debe exceder los 255 caracteres")
    private String correo;

    @NotBlank(message = "La contraseña es requerida")
    @Size(min = 64, max = 64, message = "El hash de la contraseña debe tener exactamente 64 caracteres")
    @Pattern(regexp = "[a-fA-F0-9]{64}", message = "El hash de la contraseña debe ser hexadecimal SHA-256")
    private String password;

    @NotNull(message = "El ID de la zona es requerido")
    private Integer zonaId;
}