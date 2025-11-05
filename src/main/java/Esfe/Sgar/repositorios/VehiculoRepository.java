package Esfe.Sgar.repositorios;

import Esfe.Sgar.modelos.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Integer> {

    // Buscar por estado (0/1 o valores TINYINT usados en el modelo)
    List<Vehiculo> findByEstado(Byte estado);

    // Buscar por placa única
    Vehiculo findByPlaca(String placa);

    // Buscar por código único
    Vehiculo findByCodigo(String codigo);

    // Buscar por marca
    @Query("SELECT v FROM Vehiculo v WHERE v.marca.id = :marcaId")
    List<Vehiculo> findByMarcaId(@Param("marcaId") Integer marcaId);

    // Buscar por tipo de vehículo
    @Query("SELECT v FROM Vehiculo v WHERE v.tipoVehiculo.id = :tipoVehiculoId")
    List<Vehiculo> findByTipoVehiculoId(@Param("tipoVehiculoId") Integer tipoVehiculoId);

    // Buscar por mecánico (texto parcial, case-insensitive)
    List<Vehiculo> findByMecanicoContainingIgnoreCase(String mecanico);

    // Verificar existencia por placa
    boolean existsByPlaca(String placa);

    // Contar vehículos por marca
    @Query("SELECT COUNT(v) FROM Vehiculo v WHERE v.marca.id = :marcaId")
    Long countByMarcaId(@Param("marcaId") Integer marcaId);

    // Buscar por estado y marca
    @Query("SELECT v FROM Vehiculo v WHERE v.estado = :estado AND v.marca.id = :marcaId")
    List<Vehiculo> findByEstadoAndMarcaId(@Param("estado") Byte estado, @Param("marcaId") Integer marcaId);
}
