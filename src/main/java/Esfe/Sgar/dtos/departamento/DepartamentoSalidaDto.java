package Esfe.Sgar.dtos.departamento;

import lombok.Data;
import java.util.List;

@Data
public class DepartamentoSalidaDto {
    private Integer id;
    private String nombre;
    private List<MunicipioResumenDto> municipios;
    
    @Data
    public static class MunicipioResumenDto {
        private Integer id;
        private String nombre;
    }
}