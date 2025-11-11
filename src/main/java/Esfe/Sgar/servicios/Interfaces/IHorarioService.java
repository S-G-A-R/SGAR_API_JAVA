package Esfe.Sgar.servicios.Interfaces;

import Esfe.Sgar.dtos.horario.HorarioGuardarDto;
import Esfe.Sgar.dtos.horario.HorarioModificarDto;
import Esfe.Sgar.dtos.horario.HorarioSalidaDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalTime;

public interface IHorarioService {

    Page<HorarioSalidaDto> filtrarHorarios(Integer organizacionId,
                                           String zonaId,
                                           Byte turno,
                                           String dia,
                                           LocalTime inicio,
                                           LocalTime fin,
                                           Pageable pageable);

    HorarioSalidaDto obtenerPorId(Integer id);

    HorarioSalidaDto crear(HorarioGuardarDto dto);

    HorarioSalidaDto actualizar(Integer id, HorarioModificarDto dto);

    void eliminar(Integer id);

    boolean existeSuperposicion(Integer organizacionId, String dia, LocalTime horaEntrada, LocalTime horaSalida);
}
