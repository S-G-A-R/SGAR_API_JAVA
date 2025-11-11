package Esfe.Sgar.dtos.horario;

import lombok.Data;
import java.time.LocalTime;

@Data
public class HorarioSalidaDto {

    private Integer id;
    private LocalTime horaEntrada;
    private LocalTime horaSalida;
    private String dia;
    private Integer idOrganizacion;
    private Byte turno;
    private String zonaId;
}
