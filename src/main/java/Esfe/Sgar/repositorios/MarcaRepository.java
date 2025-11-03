package Esfe.Sgar.repositorios;

import Esfe.Sgar.modelos.Marca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, Integer> {
    
    // Buscar por nombre
    List<Marca> findByNombreContainingIgnoreCase(String nombre);
    
    // Buscar por modelo
    List<Marca> findByModeloContainingIgnoreCase(String modelo);
    
    // Buscar por año de fabricación
    List<Marca> findByYearOfFabrication(String year);
    
    // Buscar marcas con vehículos activos
    @Query("SELECT DISTINCT m FROM Marca m INNER JOIN m.vehiculos v WHERE v.estado = 1")
    List<Marca> findAllWithActiveVehicles();
    
    // Verificar si existe combinación de marca, modelo y año
    boolean existsByNombreAndModeloAndYearOfFabrication(String nombre, String modelo, String yearOfFabrication);
    
    // Buscar por nombre o modelo
    @Query("SELECT m FROM Marca m WHERE LOWER(m.nombre) LIKE LOWER(CONCAT('%', ?1, '%')) OR LOWER(m.modelo) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<Marca> findByNombreOrModelo(String searchTerm);
}