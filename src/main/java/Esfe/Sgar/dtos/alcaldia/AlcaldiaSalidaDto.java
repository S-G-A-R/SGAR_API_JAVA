package Esfe.Sgar.dtos.alcaldia;

import lombok.Data;
import java.util.List;

@Data
public class AlcaldiaSalidaDto {
    private Integer id;
    private Integer idMunicipio;
    private String nombreMunicipio;
    private String correo;
    private List<ZonaResumenDto> zonas;
    private List<SupervisorResumenDto> supervisores;
    private List<OperadorResumenDto> operadores;
    
    @Data
    public static class ZonaResumenDto {
        private Integer id;
        private String nombre;
    }
    
    @Data
    public static class SupervisorResumenDto {
        private Integer id;
        private String nombre;
        private String apellido;
        private String codigo;
    }
    
    @Data
    public static class OperadorResumenDto {
        private Integer id;
        private String nombre;
        private String apellido;
        private String codigoOperador;
    }
}