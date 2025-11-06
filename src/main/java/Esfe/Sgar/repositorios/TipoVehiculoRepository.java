package Esfe.Sgar.repositorios;

import Esfe.Sgar.modelos.TipoVehiculo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoVehiculoRepository extends JpaRepository<TipoVehiculo, Integer> {

    // Consulta única con filtros opcionales y paginación
    @Query("SELECT t FROM TipoVehiculo t WHERE " +
           "(:tipo IS NULL OR t.tipo = :tipo) AND " +
           "(:descripcion IS NULL OR LOWER(t.descripcion) LIKE LOWER(CONCAT('%', :descripcion, '%')))")
    Page<TipoVehiculo> buscarConFiltros(@Param("tipo") Byte tipo,
                                         @Param("descripcion") String descripcion,
                                         Pageable pageable);

    // Verificar existencia por tipo
    boolean existsByTipo(Byte tipo);
}
