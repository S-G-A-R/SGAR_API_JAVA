package Esfe.Sgar.repositorios;

import Esfe.Sgar.modelos.TipoVehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TipoVehiculoRepository extends JpaRepository<TipoVehiculo, Integer> {

    // Buscar por valor numérico del tipo
    List<TipoVehiculo> findByTipo(Byte tipo);

    // Buscar por descripción (texto parcial)
    List<TipoVehiculo> findByDescripcionContainingIgnoreCase(String descripcion);

    // Verificar existencia por tipo
    boolean existsByTipo(Byte tipo);


    // Buscar tipos que tengan vehículos en un estado específico
    @Query("SELECT DISTINCT t FROM TipoVehiculo t JOIN t.vehiculos v WHERE v.estado = :estado")
    List<TipoVehiculo> findTiposWithVehiculosInEstado(@Param("estado") Byte estado);
}
