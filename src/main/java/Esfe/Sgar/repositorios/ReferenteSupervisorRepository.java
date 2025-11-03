package Esfe.Sgar.repositorios;

import Esfe.Sgar.modelos.ReferenteSupervisor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReferenteSupervisorRepository extends JpaRepository<ReferenteSupervisor, Integer> {
    
    // Buscar por supervisor
    List<ReferenteSupervisor> findBySupervisorId(Integer supervisorId);
    
    // Buscar por tipo
    List<ReferenteSupervisor> findByTipo(Byte tipo);
    
    // Buscar por supervisor y tipo
    List<ReferenteSupervisor> findBySupervisorIdAndTipo(Integer supervisorId, Byte tipo);
    
    // Buscar por nombre conteniendo texto
    List<ReferenteSupervisor> findByNombreContainingIgnoreCase(String nombre);
    
    // Buscar por parentesco
    List<ReferenteSupervisor> findByParentescoIgnoreCase(String parentesco);
    
    // Verificar si existe referente por nombre y supervisor
    boolean existsByNombreAndSupervisorId(String nombre, Integer supervisorId);
    
    // Contar referentes por supervisor
    Long countBySupervisorId(Integer supervisorId);
}