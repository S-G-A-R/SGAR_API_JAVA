package Esfe.Sgar.dtos.ciudadano;

import lombok.Data;
import java.util.List;

@Data
public class CiudadanoSalidaDto {
    private Integer id;
    private String nombre;
    private String apellido;
    private String dui;
    private String correo;
    private Integer zonaId;
    private String nombreZona;
    private String nombreDistrito;
    private String nombreMunicipio;
    private List<QuejaResumenDto> quejas;
    private List<NotificacionResumenDto> notificaciones;

    @Data
    public static class QuejaResumenDto {
        private Integer id;
        private String titulo;
        private Byte estado;
        private String tipoSituacion;
    }

    @Data
    public static class NotificacionResumenDto {
        private Integer id;
        private String titulo;
        private Integer distanciaMetros;
        private Byte estado;
    }
}