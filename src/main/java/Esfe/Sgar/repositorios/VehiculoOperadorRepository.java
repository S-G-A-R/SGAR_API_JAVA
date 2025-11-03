package Esfe.Sgar.repositorios;

import Esfe.Sgar.modelos.VehiculoOperador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VehiculoOperadorRepository extends JpaRepository<VehiculoOperador, Integer> {
    
    // Buscar por estado
    List<VehiculoOperador> findByEstado(Boolean estado);
    
    // Buscar por placa
    VehiculoOperador findByPlaca(String placa);
    
    // Buscar por operador
    List<VehiculoOperador> findByOperadorId(Integer operadorId);
    
    // Buscar por modelo
    List<VehiculoOperador> findByModeloId(Integer modeloId);
    
    // Buscar por color
    List<VehiculoOperador> findByColorContainingIgnoreCase(String color);
    
    // Buscar por año
    List<VehiculoOperador> findByAnio(Short anio);
    
    // Buscar vehículos asignados en un rango de fechas
    @Query("SELECT v FROM VehiculoOperador v WHERE v.fechaAsignacion BETWEEN :fechaInicio AND :fechaFin")
    List<VehiculoOperador> findByFechaAsignacionBetween(
        @Param("fechaInicio") LocalDate fechaInicio,
        @Param("fechaFin") LocalDate fechaFin
    );
    
    // Verificar si existe vehículo por placa
    boolean existsByPlaca(String placa);
    
    // Buscar vehículos con mantenimientos pendientes
    @Query("SELECT DISTINCT v FROM VehiculoOperador v JOIN v.mantenimientos m WHERE m.estado = true")
    List<VehiculoOperador> findVehiculosWithPendingMantenimientos();
    
    // Contar vehículos por marca
    @Query("SELECT COUNT(v) FROM VehiculoOperador v WHERE v.modelo.marca.id = :marcaId")
    Long countByMarca(@Param("marcaId") Integer marcaId);
    
    // Buscar vehículos por rango de años
    @Query("SELECT v FROM VehiculoOperador v WHERE v.anio BETWEEN :anioInicio AND :anioFin")
    List<VehiculoOperador> findByRangoAnio(
        @Param("anioInicio") Short anioInicio,
        @Param("anioFin") Short anioFin
    );
}