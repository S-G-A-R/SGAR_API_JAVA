package Esfe.Sgar.repositorios;

import Esfe.Sgar.modelos.Vehiculo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Integer> {

    // Consulta única con filtros opcionales y paginación
    @Query("SELECT v FROM Vehiculo v WHERE " +
           "(:placa IS NULL OR LOWER(v.placa) LIKE LOWER(CONCAT('%', :placa, '%'))) AND " +
           "(:codigo IS NULL OR LOWER(v.codigo) LIKE LOWER(CONCAT('%', :codigo, '%'))) AND " +
           "(:marcaId IS NULL OR v.marca.id = :marcaId) AND " +
           "(:tipoVehiculoId IS NULL OR v.tipoVehiculo.id = :tipoVehiculoId) AND " +
           "(:estado IS NULL OR v.estado = :estado) AND " +
           "(:mecanico IS NULL OR LOWER(v.mecanico) LIKE LOWER(CONCAT('%', :mecanico, '%')))")
    Page<Vehiculo> buscarConFiltros(@Param("placa") String placa,
                                     @Param("codigo") String codigo,
                                     @Param("marcaId") Integer marcaId,
                                     @Param("tipoVehiculoId") Integer tipoVehiculoId,
                                     @Param("estado") Byte estado,
                                     @Param("mecanico") String mecanico,
                                     Pageable pageable);

    // Verificar existencia por placa
    boolean existsByPlaca(String placa);
    
    // Verificar existencia por código
    boolean existsByCodigo(String codigo);
    
    // Verificar si una foto está siendo usada por algún vehículo
    boolean existsByFotoId(Integer fotoId);
    
    // Contar vehículos asignados a un operador
    long countByOperadorId(Integer operadorId);
    
    // Buscar vehículos por operador
    List<Vehiculo> findByOperadorId(Integer operadorId);
}
