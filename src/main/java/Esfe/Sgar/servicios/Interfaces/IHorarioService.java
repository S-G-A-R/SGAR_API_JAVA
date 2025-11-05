package Esfe.Sgar.servicios.Interfaces;

import Esfe.Sgar.modelos.Horario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalTime;

public interface IHorarioService {

    Page<Horario> filtrarHorarios(Integer organizacionId,
                                  Integer zonaId,
                                  Byte turno,
                                  String dia,
                                  LocalTime inicio,
                                  LocalTime fin,
                                  Pageable pageable);

    Horario obtenerPorId(Integer id);

    Horario crear(Horario horario);

    Horario actualizar(Integer id, Horario horario);

    void eliminar(Integer id);

    boolean existeSuperposicion(Integer organizacionId, String dia, LocalTime horaEntrada, LocalTime horaSalida);
}
