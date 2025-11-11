package Esfe.Sgar.repositorios;

import Esfe.Sgar.modelos.TipoClasificacionBasura;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoClasificacionBasuraRepository extends JpaRepository<TipoClasificacionBasura, Integer> {

    @Query("SELECT t FROM TipoClasificacionBasura t WHERE " +
           "(:nombre IS NULL OR LOWER(t.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))")
    Page<TipoClasificacionBasura> buscarPorNombre(@Param("nombre") String nombre, Pageable pageable);

    boolean existsByNombre(String nombre);
}
