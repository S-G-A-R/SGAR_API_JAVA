package Esfe.Sgar.repositorios;

import Esfe.Sgar.modelos.Queja;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuejaRepository extends JpaRepository<Queja, Integer> {
    
    // Buscar por ciudadano
    List<Queja> findByCiudadano(Integer ciudadano);
    
    // Buscar por estado
    List<Queja> findByEstado(Byte estado);
    
    // Buscar por tipo de situación
    List<Queja> findByTipoSituacion(String tipoSituacion);
    
    // Buscar por título conteniendo texto
    List<Queja> findByTituloContainingIgnoreCase(String titulo);
    
    // Buscar quejas por ciudadano y estado (paginado)
    Page<Queja> findByCiudadanoAndEstado(Integer ciudadano, Byte estado, Pageable pageable);
    
    // Por el momento, no podemos buscar por zona ya que no tenemos la relación entre ciudadano y zona
    // estas consultas se han comentado hasta que implementemos la relación adecuada
    /*
    // Buscar quejas por zona
    @Query("SELECT q FROM Queja q WHERE q.ciudadano.zona.id = :zonaId")
    List<Queja> findByZonaId(@Param("zonaId") Integer zonaId);
    
    // Contar quejas por estado en una zona específica
    @Query("SELECT COUNT(q) FROM Queja q WHERE q.ciudadano.zona.id = :zonaId AND q.estado = :estado")
    Long countByZonaIdAndEstado(@Param("zonaId") Integer zonaId, @Param("estado") Byte estado);
    
    // Buscar quejas sin resolver por zona
    @Query("SELECT q FROM Queja q WHERE q.ciudadano.zona.id = :zonaId AND q.estado IN (0, 1)")
    List<Queja> findPendientesByZonaId(@Param("zonaId") Integer zonaId);
    */
}