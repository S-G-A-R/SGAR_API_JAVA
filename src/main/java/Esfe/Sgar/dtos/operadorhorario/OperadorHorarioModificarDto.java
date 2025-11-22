package Esfe.Sgar.dtos.operadorhorario;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OperadorHorarioModificarDto {
    @NotNull
    private Integer id;
    @NotNull
    private Integer operadorId;
    @NotNull
    private Integer horarioId;
}
