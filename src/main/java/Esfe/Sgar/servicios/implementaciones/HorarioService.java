package Esfe.Sgar.servicios.implementaciones;

import Esfe.Sgar.dtos.horario.HorarioGuardarDto;
import Esfe.Sgar.dtos.horario.HorarioModificarDto;
import Esfe.Sgar.dtos.horario.HorarioSalidaDto;
import Esfe.Sgar.modelos.Horario;
import Esfe.Sgar.repositorios.HorarioRepository;
import Esfe.Sgar.servicios.Interfaces.IHorarioService;
import Esfe.Sgar.servicios.Interfaces.IExternalSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalTime;
import java.util.Optional;

@Service
public class HorarioService implements IHorarioService {

    private final HorarioRepository horarioRepository;
    private final IExternalSecurityService externalSecurityService;

    @Autowired
    public HorarioService(HorarioRepository horarioRepository, 
                         IExternalSecurityService externalSecurityService) {
        this.horarioRepository = horarioRepository;
        this.externalSecurityService = externalSecurityService;
    }

    @Override
    public Page<HorarioSalidaDto> filtrarHorarios(Integer organizacionId, Integer zonaId, Byte turno, String dia, LocalTime inicio, LocalTime fin, Pageable pageable) {
        return horarioRepository
                .filtrarHorarios(organizacionId, zonaId, turno, dia, inicio, fin, pageable)
                .map(this::toSalidaDto);
    }

    @Override
    public HorarioSalidaDto obtenerPorId(Integer id) {
        Optional<Horario> opt = horarioRepository.findById(id);
        Horario h = opt.orElseThrow(() -> new IllegalArgumentException("Horario no encontrado con id: " + id));
        return toSalidaDto(h);
    }

    @Override
    @Transactional
    public HorarioSalidaDto crear(HorarioGuardarDto dto) {
        if (dto == null) throw new IllegalArgumentException("El DTO no puede ser nulo");
        if (dto.getHoraEntrada() == null || dto.getHoraSalida() == null) {
            throw new IllegalArgumentException("Horas de entrada y salida son requeridas");
        }
        if (!dto.getHoraEntrada().isBefore(dto.getHoraSalida())) {
            throw new IllegalArgumentException("La hora de entrada debe ser anterior a la hora de salida");
        }
        
        // Validar que existe la organización
        if (!externalSecurityService.existeOrganizacion(dto.getIdOrganizacion())) {
            throw new IllegalArgumentException("La organización con ID " + dto.getIdOrganizacion() + " no existe");
        }

        Horario h = new Horario();
        h.setHoraEntrada(dto.getHoraEntrada());
        h.setHoraSalida(dto.getHoraSalida());
        h.setDia(dto.getDia());
        h.setIdOrganizacion(dto.getIdOrganizacion());
        h.setTurno(dto.getTurno());
        h.setZonaId(dto.getZonaId());

        Horario guardado = horarioRepository.save(h);
        return toSalidaDto(guardado);
    }

    @Override
    @Transactional
    public HorarioSalidaDto actualizar(Integer id, HorarioModificarDto dto) {
        Horario existente = horarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Horario no encontrado con id: " + id));

        if (dto.getHoraEntrada() != null && dto.getHoraSalida() != null) {
            if (!dto.getHoraEntrada().isBefore(dto.getHoraSalida())) {
                throw new IllegalArgumentException("La hora de entrada debe ser anterior a la hora de salida");
            }
        }

        if (dto.getHoraEntrada() != null) existente.setHoraEntrada(dto.getHoraEntrada());
        if (dto.getHoraSalida() != null) existente.setHoraSalida(dto.getHoraSalida());
        if (dto.getDia() != null) existente.setDia(dto.getDia());
        if (dto.getTurno() != null) existente.setTurno(dto.getTurno());
        if (dto.getIdOrganizacion() != null) existente.setIdOrganizacion(dto.getIdOrganizacion());
        if (dto.getZonaId() != null) existente.setZonaId(dto.getZonaId());

        Horario actualizado = horarioRepository.save(existente);
        return toSalidaDto(actualizado);
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        if (!horarioRepository.existsById(id)) {
            throw new IllegalArgumentException("Horario no encontrado con id: " + id);
        }
        horarioRepository.deleteById(id);
    }

    @Override
    public boolean existeSuperposicion(Integer organizacionId, String dia, LocalTime horaEntrada, LocalTime horaSalida) {
        if (organizacionId == null || dia == null || horaEntrada == null || horaSalida == null) return false;
        return horarioRepository.existeSuperposicion(organizacionId, dia, horaEntrada, horaSalida);
    }

    private HorarioSalidaDto toSalidaDto(Horario h) {
        HorarioSalidaDto out = new HorarioSalidaDto();
        out.setId(h.getId());
        out.setHoraEntrada(h.getHoraEntrada());
        out.setHoraSalida(h.getHoraSalida());
        out.setDia(h.getDia());
        out.setIdOrganizacion(h.getIdOrganizacion());
        out.setTurno(h.getTurno());
        out.setZonaId(h.getZonaId());
        return out;
    }
}
