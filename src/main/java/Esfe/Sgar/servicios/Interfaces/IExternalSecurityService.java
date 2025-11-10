package Esfe.Sgar.servicios.Interfaces;

public interface IExternalSecurityService {
    /**
     * Verifica si existe una organización por su ID consultando la lista de organizaciones
     * @param organizacionId ID de la organización a verificar
     * @return true si la organización existe
     */
    boolean existeOrganizacion(Integer organizacionId);
    
    /**
     * Verifica si existe un operador por su ID
     * @param operadorId ID del operador a verificar
     * @return true si el operador existe
     */
    boolean existeOperador(Integer operadorId);
    
    /**
     * Verifica si existe una zona por su ID consultando la API de navegación
     * @param zonaId ID de la zona a verificar
     * @return true si la zona existe
     */
    boolean existeZona(Integer zonaId);
}