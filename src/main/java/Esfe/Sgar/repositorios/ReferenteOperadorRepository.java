package Esfe.Sgar.repositorios;

import Esfe.Sgar.modelos.ReferenteOperador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReferenteOperadorRepository extends JpaRepository<ReferenteOperador, Integer> {
    
    // Buscar por operador
    List<ReferenteOperador> findByOperadorId(Integer operadorId);
    
    // Buscar por tipo
    List<ReferenteOperador> findByTipo(Byte tipo);
    
    // Buscar por operador y tipo
    List<ReferenteOperador> findByOperadorIdAndTipo(Integer operadorId, Byte tipo);
    
    // Buscar por nombre conteniendo texto
    List<ReferenteOperador> findByNombreContainingIgnoreCase(String nombre);
    
    // Buscar por parentesco
    List<ReferenteOperador> findByParentescoIgnoreCase(String parentesco);
    
    // Verificar si existe referente por nombre y operador
    boolean existsByNombreAndOperadorId(String nombre, Integer operadorId);
    
    // Contar referentes por operador
    Long countByOperadorId(Integer operadorId);
}