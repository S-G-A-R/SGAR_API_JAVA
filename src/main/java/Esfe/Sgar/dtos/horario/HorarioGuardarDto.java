package Esfe.Sgar.dtos.horario;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalTime;

@Data
public class HorarioGuardarDto {

    @NotNull(message = "La hora de entrada es requerida")
    private LocalTime horaEntrada;

    @NotNull(message = "La hora de salida es requerida")
    private LocalTime horaSalida;

    @NotBlank(message = "El día es requerido")
    @Size(max = 7, message = "El día no debe exceder los 7 caracteres")
    private String dia;

    @NotNull(message = "El id de organización es requerido")
    private Integer idOrganizacion;

    @NotNull(message = "El turno es requerido")
    private Byte turno;

    @NotNull(message = "La zona es requerida")
    private Integer zonaId;
}
