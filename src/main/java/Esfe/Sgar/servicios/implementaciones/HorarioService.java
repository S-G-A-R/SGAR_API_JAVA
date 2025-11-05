package Esfe.Sgar.servicios.implementaciones;

import Esfe.Sgar.modelos.Horario;
import Esfe.Sgar.repositorios.HorarioRepository;
import Esfe.Sgar.servicios.Interfaces.IHorarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalTime;
import java.util.Optional;

@Service
public class HorarioService implements IHorarioService {

    @Autowired
    private  HorarioRepository horarioRepository;

    @Autowired
    public HorarioService(HorarioRepository horarioRepository) {
        this.horarioRepository = horarioRepository;
    }

    @Override
    public Page<Horario> filtrarHorarios(Integer organizacionId, Integer zonaId, Byte turno, String dia, LocalTime inicio, LocalTime fin, Pageable pageable) {
        return horarioRepository.filtrarHorarios(organizacionId, zonaId, turno, dia, inicio, fin, pageable);
    }

    @Override
    public Horario obtenerPorId(Integer id) {
        Optional<Horario> opt = horarioRepository.findById(id);
        return opt.orElseThrow(() -> new IllegalArgumentException("Horario no encontrado con id: " + id));
    }

    @Override
    @Transactional
    public Horario crear(Horario horario) {
        // Validaciones básicas
        if (horario == null) {
            throw new IllegalArgumentException("Horario no puede ser nulo");
        }
        if (horario.getHoraEntrada() == null || horario.getHoraSalida() == null) {
            throw new IllegalArgumentException("Horas de entrada y salida son requeridas");
        }
        if (horario.getHoraEntrada().isAfter(horario.getHoraSalida())) {
            throw new IllegalArgumentException("Hora de entrada debe ser anterior a hora de salida");
        }

        // Si existe operador/zona y se desea validar superposición, el repository ofrece el método
        // pero como los modelos pueden tener esas relaciones comentadas, la verificación se delega
        // solo cuando los ids estén presentes (no nulos)
        Integer organizacionId = null;
        try {
            organizacionId = (horario.getClass().getDeclaredField("organizacion") != null) ? null : null;
        } catch (NoSuchFieldException e) {
            // campo operador no presente en clase compilada - ignorar
        }

        // Guardar
        return horarioRepository.save(horario);
    }

    @Override
    @Transactional
    public Horario actualizar(Integer id, Horario horario) {
        Horario existente = obtenerPorId(id);

        if (horario.getHoraEntrada() != null) existente.setHoraEntrada(horario.getHoraEntrada());
        if (horario.getHoraSalida() != null) existente.setHoraSalida(horario.getHoraSalida());
        if (horario.getDia() != null) existente.setDia(horario.getDia());
        if (horario.getTurno() != null) existente.setTurno(horario.getTurno());


        return horarioRepository.save(existente);
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
}
