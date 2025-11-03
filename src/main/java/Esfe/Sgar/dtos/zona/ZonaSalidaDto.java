package Esfe.Sgar.dtos.zona;

import lombok.Data;
import java.util.List;

@Data
public class ZonaSalidaDto {
    private Integer id;
    private String nombre;
    private Integer idDistrito;
    private String nombreDistrito;
    private Integer idAlcaldia;
    private String nombreMunicipio;
    private String descripcion;
    private List<CiudadanoResumenDto> ciudadanos;
    private List<HorarioResumenDto> horarios;

    @Data
    public static class CiudadanoResumenDto {
        private Integer id;
        private String nombre;
        private String apellido;
        private String dui;
    }

    @Data
    public static class HorarioResumenDto {
        private Integer id;
        private String horaEntrada;
        private String horaSalida;
        private String dia;
        private Byte turno;
        private Integer idOperador;
        private String nombreOperador;
    }
}