package Esfe.Sgar.repositorios;

import Esfe.Sgar.modelos.Marca;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, Integer> {
    
    // Consulta única con filtros opcionales y paginación
    @Query("SELECT m FROM Marca m WHERE " +
           "(:nombre IS NULL OR LOWER(m.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
           "(:modelo IS NULL OR LOWER(m.modelo) LIKE LOWER(CONCAT('%', :modelo, '%'))) AND " +
           "(:year IS NULL OR m.yearOfFabrication = :year)")
    Page<Marca> buscarConFiltros(@Param("nombre") String nombre,
                                  @Param("modelo") String modelo,
                                  @Param("year") String year,
                                  Pageable pageable);
    
    // Verificar si existe combinación de marca, modelo y año
    boolean existsByNombreAndModeloAndYearOfFabrication(String nombre, String modelo, String yearOfFabrication);
}