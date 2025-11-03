package Esfe.Sgar.dtos.operador;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class OperadorModificarDto {
    
    @NotNull(message = "El ID del operador es requerido")
    private Integer id;

    @NotBlank(message = "El nombre es requerido")
    @Size(max = 120, message = "El nombre no debe exceder los 120 caracteres")
    private String nombre;

    @Size(max = 120, message = "El apellido no debe exceder los 120 caracteres")
    private String apellido;

    @Pattern(regexp = "^[0-9]{9}$", message = "El teléfono personal debe tener 9 dígitos")
    private String telefonoPersonal;

    @Email(message = "El formato del correo personal no es válido")
    @Size(max = 255, message = "El correo personal no debe exceder los 255 caracteres")
    private String correoPersonal;

    @NotBlank(message = "El DUI es requerido")
    @Pattern(regexp = "^[0-9]{9}[0-9]$", message = "El DUI debe tener 10 dígitos")
    private String dui;

    private byte[] foto;

    @Size(max = 500, message = "La lista de ayudantes no debe exceder los 500 caracteres")
    private String ayudantes;

    @NotBlank(message = "El código de operador es requerido")
    @Size(max = 20, message = "El código de operador no debe exceder los 20 caracteres")
    private String codigoOperador;

    @NotBlank(message = "El teléfono laboral es requerido")
    @Pattern(regexp = "^[0-9]{9}$", message = "El teléfono laboral debe tener 9 dígitos")
    private String telefonoLaboral;

    @NotBlank(message = "El correo laboral es requerido")
    @Email(message = "El formato del correo laboral no es válido")
    @Size(max = 255, message = "El correo laboral no debe exceder los 255 caracteres")
    private String correoLaboral;

    private Integer vehiculoId;

    private byte[] licenciaDoc;
    private byte[] antecedentesDoc;
    private byte[] solvenciaDoc;

    @Size(min = 64, max = 64, message = "El hash de la contraseña debe tener exactamente 64 caracteres")
    @Pattern(regexp = "[a-fA-F0-9]{64}", message = "El hash de la contraseña debe ser hexadecimal SHA-256")
    private String password; // Opcional en modificación

    @NotNull(message = "El ID de la alcaldía es requerido")
    private Integer idAlcaldia;
}