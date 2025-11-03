package Esfe.Sgar.dtos.queja;

import lombok.Data;

@Data
public class QuejaSalidaDto {
    private Integer id;
    private String titulo;
    private String descripcion;
    private Integer idCiudadano;
    private String nombreCiudadano;
    private String duiCiudadano;
    private byte[] archivo;
    private String tipoSituacion;
    private Byte estado;
    private String motivo;
    private String zonaAfectada;
    private String distrito;
    private String municipio;
}