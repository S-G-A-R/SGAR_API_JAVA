package Esfe.Sgar.repositorios;

import Esfe.Sgar.modelos.Queja;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuejaRepository extends JpaRepository<Queja, Integer> {
    
    // Buscar por ciudadano
    List<Queja> findByCiudadanoId(Integer ciudadanoId);
    
    // Buscar por estado
    List<Queja> findByEstado(Byte estado);
    
    // Buscar por tipo de situación
    List<Queja> findByTipoSituacion(String tipoSituacion);
    
    // Buscar por título conteniendo texto
    List<Queja> findByTituloContainingIgnoreCase(String titulo);
    
    // Buscar quejas por ciudadano y estado (paginado)
    Page<Queja> findByCiudadanoIdAndEstado(Integer ciudadanoId, Byte estado, Pageable pageable);
    
    // Buscar quejas por zona
    @Query("SELECT q FROM Queja q WHERE q.ciudadano.zona.id = :zonaId")
    List<Queja> findByZonaId(@Param("zonaId") Integer zonaId);
    
    // Contar quejas por estado en una zona específica
    @Query("SELECT COUNT(q) FROM Queja q WHERE q.ciudadano.zona.id = :zonaId AND q.estado = :estado")
    Long countByZonaIdAndEstado(@Param("zonaId") Integer zonaId, @Param("estado") Byte estado);
    
    // Buscar quejas sin resolver por zona
    @Query("SELECT q FROM Queja q WHERE q.ciudadano.zona.id = :zonaId AND q.estado IN (0, 1)")
    List<Queja> findPendientesByZonaId(@Param("zonaId") Integer zonaId);
}