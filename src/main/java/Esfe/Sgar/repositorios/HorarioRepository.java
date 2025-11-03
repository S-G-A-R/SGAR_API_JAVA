package Esfe.Sgar.repositorios;

import Esfe.Sgar.modelos.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface HorarioRepository extends JpaRepository<Horario, Integer> {
    
    // Buscar por operador
    List<Horario> findByOperadorId(Integer operadorId);
    
    // Buscar por zona
    List<Horario> findByZonaId(Integer zonaId);
    
    // Buscar por turno
    List<Horario> findByTurno(Byte turno);
    
    // Buscar horarios por día específico (usando el patrón de día)
    @Query("SELECT h FROM Horario h WHERE h.dia LIKE %:patron%")
    List<Horario> findByDiaPattern(@Param("patron") String patron);
    
    // Buscar horarios por rango de hora
    @Query("SELECT h FROM Horario h WHERE h.horaEntrada >= :inicio AND h.horaSalida <= :fin")
    List<Horario> findByRangoHora(@Param("inicio") LocalTime inicio, @Param("fin") LocalTime fin);
    
    // Buscar horarios por operador y día
    List<Horario> findByOperadorIdAndDia(Integer operadorId, String dia);
    
    // Verificar si existe superposición de horarios para un operador
    @Query("SELECT COUNT(h) > 0 FROM Horario h " +
           "WHERE h.operador.id = :operadorId " +
           "AND h.dia = :dia " +
           "AND ((h.horaEntrada <= :horaSalida AND h.horaSalida >= :horaEntrada) " +
           "OR (h.horaEntrada <= :horaSalida AND h.horaSalida >= :horaSalida))")
    boolean existeSuperposicion(@Param("operadorId") Integer operadorId, 
                              @Param("dia") String dia,
                              @Param("horaEntrada") LocalTime horaEntrada, 
                              @Param("horaSalida") LocalTime horaSalida);
}