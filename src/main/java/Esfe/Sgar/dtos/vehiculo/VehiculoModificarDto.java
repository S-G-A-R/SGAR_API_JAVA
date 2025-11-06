package Esfe.Sgar.dtos.vehiculo;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class VehiculoModificarDto {
    
    @NotNull(message = "El ID del vehículo es requerido")
    private Integer id;
    
    @NotNull(message = "El ID de la marca es requerido")
    private Integer idMarca;

    @NotBlank(message = "La placa es requerida")
    @Size(max = 20, message = "La placa no debe exceder los 20 caracteres")
    private String placa;

    @NotBlank(message = "El código es requerido")
    @Size(max = 20, message = "El código no debe exceder los 20 caracteres")
    private String codigo;

    @NotNull(message = "El ID del tipo de vehículo es requerido")
    private Integer idTipoVehiculo;

    @Size(max = 120, message = "El nombre del mecánico no debe exceder los 120 caracteres")
    private String mecanico;

    @Size(max = 120, message = "El nombre del taller no debe exceder los 120 caracteres")
    private String taller;

    private Integer idOperador;

    private Integer idFoto;

    @NotNull(message = "El estado es requerido")
    @Min(value = 0, message = "El estado debe ser un valor entre 0 y 127")
    @Max(value = 127, message = "El estado debe ser un valor entre 0 y 127")
    private Byte estado;

    @Size(max = 500, message = "La descripción no debe exceder los 500 caracteres")
    private String descripcion;
}