package Esfe.Sgar.repositorios;

import Esfe.Sgar.modelos.OperadorHorario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OperadorHorarioRepository extends JpaRepository<OperadorHorario, Integer> {
    // Filtro por operadorId y horarioId con paginación
    @Query("SELECT oh FROM OperadorHorario oh WHERE (:operadorId IS NULL OR oh.operadorId = :operadorId) AND (:horarioId IS NULL OR oh.horario.id = :horarioId)")
    Page<OperadorHorario> buscarConFiltros(@Param("operadorId") Integer operadorId, @Param("horarioId") Integer horarioId, Pageable pageable);
}
