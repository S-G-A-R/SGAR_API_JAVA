package Esfe.Sgar.dtos.alcaldia;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AlcaldiaGuardarDto {
    
    @NotNull(message = "El ID del municipio es requerido")
    private Integer idMunicipio;
    
    @NotBlank(message = "El correo es requerido")
    @Email(message = "El formato del correo no es válido")
    @Size(max = 255, message = "El correo no debe exceder los 255 caracteres")
    private String correo;
    
    @NotBlank(message = "La contraseña es requerida")
    @Size(min = 64, max = 64, message = "El hash de la contraseña debe tener exactamente 64 caracteres")
    @Pattern(regexp = "[a-fA-F0-9]{64}", message = "El hash de la contraseña debe ser hexadecimal SHA-256")
    private String password;
}