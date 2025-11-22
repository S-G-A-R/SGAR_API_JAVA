package Esfe.Sgar.servicios.Interfaces;

import Esfe.Sgar.dtos.operadorhorario.OperadorHorarioGuardarDto;
import Esfe.Sgar.dtos.operadorhorario.OperadorHorarioModificarDto;
import Esfe.Sgar.dtos.operadorhorario.OperadorHorarioSalidaDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IOperadorHorarioService {
    Page<OperadorHorarioSalidaDto> buscarConFiltros(Integer operadorId, Integer horarioId, Pageable pageable);
    OperadorHorarioSalidaDto obtenerPorId(Integer id);
    OperadorHorarioSalidaDto crear(OperadorHorarioGuardarDto dto);
    OperadorHorarioSalidaDto actualizar(Integer id, OperadorHorarioModificarDto dto);
    void eliminar(Integer id);
}
