package Esfe.Sgar.dtos.operadorhorario;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OperadorHorarioGuardarDto {
    @NotNull
    private Integer operadorId;
    @NotNull
    private Integer horarioId;
}
