package Esfe.Sgar.repositorios;

import Esfe.Sgar.modelos.Horario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;

@Repository
public interface HorarioRepository extends JpaRepository<Horario, Integer> {

   
    @Query(value = "SELECT h FROM Horario h WHERE " +
           "(:organizacionId IS NULL OR h.IdOrganizacion = :organizacionId) AND " +
           "(:zonaId IS NULL OR h.zonaId = :zonaId) AND " +
           "(:turno IS NULL OR h.turno = :turno) AND " +
           "(:dia IS NULL OR LOWER(h.dia) LIKE LOWER(CONCAT('%', :dia, '%'))) AND " +
           "(:inicio IS NULL OR h.horaEntrada >= :inicio) AND " +
           "(:fin IS NULL OR h.horaSalida <= :fin)",
           countQuery = "SELECT COUNT(h) FROM Horario h WHERE " +
           "(:organizacionId IS NULL OR h.IdOrganizacion = :organizacionId) AND " +
           "(:zonaId IS NULL OR h.zonaId = :zonaId) AND " +
           "(:turno IS NULL OR h.turno = :turno) AND " +
           "(:dia IS NULL OR LOWER(h.dia) LIKE LOWER(CONCAT('%', :dia, '%'))) AND " +
           "(:inicio IS NULL OR h.horaEntrada >= :inicio) AND " +
           "(:fin IS NULL OR h.horaSalida <= :fin)")
    Page<Horario> filtrarHorarios(@Param("organizacionId") Integer organizacionId,
                                  @Param("zonaId") String zonaId,
                                  @Param("turno") Byte turno,
                                  @Param("dia") String dia,
                                  @Param("inicio") LocalTime inicio,
                                  @Param("fin") LocalTime fin,
                                  Pageable pageable);

   
    @Query("SELECT COUNT(h) > 0 FROM Horario h " +
           "WHERE h.IdOrganizacion = :organizacionId " +
           "AND h.dia = :dia " +
           "AND (h.horaEntrada < :horaSalida AND h.horaSalida > :horaEntrada)")
    boolean existeSuperposicion(@Param("organizacionId") Integer organizacionId,
                                @Param("dia") String dia,
                                @Param("horaEntrada") LocalTime horaEntrada,
                                @Param("horaSalida") LocalTime horaSalida);
}