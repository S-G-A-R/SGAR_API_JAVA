package Esfe.Sgar.dtos.vehiculo;

import lombok.Data;

@Data
public class VehiculoSalidaDto {
    private Integer id;
    private Integer idMarca;
    private String nombreMarca;
    private String modeloMarca;
    private String placa;
    private String codigo;
    private Integer idTipoVehiculo;
    private String tipoVehiculoDescripcion;
    private String mecanico;
    private String taller;
    private Integer idOperador;
    private String nombreOperador;
    private Byte estado;
    private String descripcion;
    private byte[] foto;
}