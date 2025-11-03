package Esfe.Sgar.dtos.operador;

import lombok.Data;
import java.util.List;

@Data
public class OperadorSalidaDto {
    private Integer id;
    private String nombre;
    private String apellido;
    private String telefonoPersonal;
    private String correoPersonal;
    private String dui;
    private byte[] foto;
    private String ayudantes;
    private String codigoOperador;
    private String telefonoLaboral;
    private String correoLaboral;
    private Integer vehiculoId;
    private String vehiculoPlaca;
    private byte[] licenciaDoc;
    private byte[] antecedentesDoc;
    private byte[] solvenciaDoc;
    private Integer idAlcaldia;
    private String nombreMunicipio;
    private List<HorarioResumenDto> horarios;
    private List<UbicacionResumenDto> ubicaciones;
    private List<ReferenteResumenDto> referentes;
    private List<MantenimientoResumenDto> mantenimientos;

    @Data
    public static class HorarioResumenDto {
        private Integer id;
        private String horaEntrada;
        private String horaSalida;
        private String dia;
        private Byte turno;
        private Integer idZona;
        private String nombreZona;
    }

    @Data
    public static class UbicacionResumenDto {
        private Integer id;
        private String latitud;
        private String longitud;
        private String fechaActualizacion;
    }

    @Data
    public static class ReferenteResumenDto {
        private Integer id;
        private String nombre;
        private String parentesco;
        private Byte tipo;
    }

    @Data
    public static class MantenimientoResumenDto {
        private Integer id;
        private String titulo;
        private Byte estado;
    }
}