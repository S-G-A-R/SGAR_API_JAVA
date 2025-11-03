package Esfe.Sgar.dtos.mantenimiento;

import lombok.Data;

@Data
public class MantenimientoSalidaDto {
    private Integer id;
    private String titulo;
    private String descripcion;
    private Integer idOperador;
    private String nombreOperador;
    private String codigoOperador;
    private byte[] archivo;
    private String tipoSituacion;
    private Byte estado;
    private String motivo;
    private String vehiculoPlaca;
    private String alcaldia;
    private String municipio;
}