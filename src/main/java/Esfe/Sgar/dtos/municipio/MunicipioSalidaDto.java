package Esfe.Sgar.dtos.municipio;

import lombok.Data;
import java.util.List;

@Data
public class MunicipioSalidaDto {
    private Integer id;
    private String nombre;
    private Integer idDepartamento;
    private String nombreDepartamento;
    private List<DistritoResumenDto> distritos;
    private List<AlcaldiaResumenDto> alcaldias;
    
    @Data
    public static class DistritoResumenDto {
        private Integer id;
        private String nombre;
    }
    
    @Data
    public static class AlcaldiaResumenDto {
        private Integer id;
        private String correo;
    }
}