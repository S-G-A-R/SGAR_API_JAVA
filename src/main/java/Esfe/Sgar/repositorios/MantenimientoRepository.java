package Esfe.Sgar.repositorios;

import Esfe.Sgar.modelos.Mantenimiento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MantenimientoRepository extends JpaRepository<Mantenimiento, Integer> {
    
    // Buscar por operador
    List<Mantenimiento> findByOperadorId(Integer operadorId);
    
    // Buscar por estado
    List<Mantenimiento> findByEstado(Byte estado);
    
    // Buscar por tipo de situación
    List<Mantenimiento> findByTipoSituacion(String tipoSituacion);
    
    // Buscar por título conteniendo texto
    List<Mantenimiento> findByTituloContainingIgnoreCase(String titulo);
    
    // Buscar mantenimientos por operador y estado (paginado)
    Page<Mantenimiento> findByOperadorIdAndEstado(Integer operadorId, Byte estado, Pageable pageable);
    
    // Contar mantenimientos por operador y estado
    @Query("SELECT COUNT(m) FROM Mantenimiento m WHERE m.operador.id = :operadorId AND m.estado = :estado")
    Long countByOperadorIdAndEstado(@Param("operadorId") Integer operadorId, @Param("estado") Byte estado);
    
    // Buscar mantenimientos por vehículo (a través del operador)
    @Query("SELECT m FROM Mantenimiento m WHERE m.operador.vehiculo.id = :vehiculoId")
    List<Mantenimiento> findByVehiculoId(@Param("vehiculoId") Integer vehiculoId);
    
    // Verificar si existe mantenimiento pendiente para un operador
    @Query("SELECT COUNT(m) > 0 FROM Mantenimiento m " +
           "WHERE m.operador.id = :operadorId AND m.estado IN (0, 1)")
    boolean existeMantenimientoPendiente(@Param("operadorId") Integer operadorId);
}