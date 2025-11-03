package Esfe.Sgar.dtos.supervisor;

import lombok.Data;
import java.util.List;

@Data
public class SupervisorSalidaDto {
    private Integer id;
    private String nombre;
    private String apellido;
    private String telefono;
    private String correoPersonal;
    private String dui;
    private byte[] foto;
    private String codigo;
    private String correoLaboral;
    private String telefonoLaboral;
    private Integer idAlcaldia;
    private String nombreMunicipio;
    private List<ReferenteResumenDto> referentes;

    @Data
    public static class ReferenteResumenDto {
        private Integer id;
        private String nombre;
        private String parentesco;
        private Byte tipo;
    }
}