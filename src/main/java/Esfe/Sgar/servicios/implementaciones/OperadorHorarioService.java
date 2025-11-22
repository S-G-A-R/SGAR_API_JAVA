package Esfe.Sgar.servicios.implementaciones;

import Esfe.Sgar.dtos.operadorhorario.OperadorHorarioGuardarDto;
import Esfe.Sgar.dtos.operadorhorario.OperadorHorarioModificarDto;
import Esfe.Sgar.dtos.operadorhorario.OperadorHorarioSalidaDto;
import Esfe.Sgar.modelos.Horario;
import Esfe.Sgar.modelos.OperadorHorario;
import Esfe.Sgar.repositorios.HorarioRepository;
import Esfe.Sgar.repositorios.OperadorHorarioRepository;
import Esfe.Sgar.servicios.Interfaces.IOperadorHorarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class OperadorHorarioService implements IOperadorHorarioService {
    private final OperadorHorarioRepository operadorHorarioRepository;
    private final HorarioRepository horarioRepository;

    @Autowired
    public OperadorHorarioService(OperadorHorarioRepository operadorHorarioRepository, HorarioRepository horarioRepository) {
        this.operadorHorarioRepository = operadorHorarioRepository;
        this.horarioRepository = horarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OperadorHorarioSalidaDto> buscarConFiltros(Integer operadorId, Integer horarioId, Pageable pageable) {
        return operadorHorarioRepository.buscarConFiltros(operadorId, horarioId, pageable)
                .map(this::toSalidaDto);
    }

    @Override
    @Transactional(readOnly = true)
    public OperadorHorarioSalidaDto obtenerPorId(Integer id) {
        OperadorHorario oh = operadorHorarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Asignación no encontrada con id: " + id));
        return toSalidaDto(oh);
    }

    @Override
    @Transactional
    public OperadorHorarioSalidaDto crear(OperadorHorarioGuardarDto dto) {
        Horario horario = horarioRepository.findById(dto.getHorarioId())
                .orElseThrow(() -> new IllegalArgumentException("Horario no encontrado con id: " + dto.getHorarioId()));
        OperadorHorario oh = new OperadorHorario();
        oh.setOperadorId(dto.getOperadorId());
        oh.setHorario(horario);
        OperadorHorario guardado = operadorHorarioRepository.save(oh);
        return toSalidaDto(guardado);
    }

    @Override
    @Transactional
    public OperadorHorarioSalidaDto actualizar(Integer id, OperadorHorarioModificarDto dto) {
        OperadorHorario oh = operadorHorarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Asignación no encontrada con id: " + id));
        Horario horario = horarioRepository.findById(dto.getHorarioId())
                .orElseThrow(() -> new IllegalArgumentException("Horario no encontrado con id: " + dto.getHorarioId()));
        oh.setOperadorId(dto.getOperadorId());
        oh.setHorario(horario);
        OperadorHorario actualizado = operadorHorarioRepository.save(oh);
        return toSalidaDto(actualizado);
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        if (!operadorHorarioRepository.existsById(id)) {
            throw new IllegalArgumentException("Asignación no encontrada con id: " + id);
        }
        operadorHorarioRepository.deleteById(id);
    }

    private OperadorHorarioSalidaDto toSalidaDto(OperadorHorario oh) {
        OperadorHorarioSalidaDto dto = new OperadorHorarioSalidaDto();
        dto.setId(oh.getId());
        dto.setOperadorId(oh.getOperadorId());
        dto.setHorarioId(oh.getHorario().getId());
        return dto;
    }
}
