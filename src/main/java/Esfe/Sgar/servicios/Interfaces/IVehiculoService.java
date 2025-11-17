package Esfe.Sgar.servicios.Interfaces;

import Esfe.Sgar.dtos.vehiculo.VehiculoGuardarDto;
import Esfe.Sgar.dtos.vehiculo.VehiculoModificarDto;
import Esfe.Sgar.dtos.vehiculo.VehiculoSalidaDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IVehiculoService {

    Page<VehiculoSalidaDto> buscarConFiltros(String placa, String codigo, Integer marcaId, 
                                              Integer tipoVehiculoId, Byte estado, String mecanico, 
                                              Pageable pageable);

    VehiculoSalidaDto obtenerPorId(Integer id);

    VehiculoSalidaDto crear(VehiculoGuardarDto dto);

    VehiculoSalidaDto actualizar(Integer id, VehiculoModificarDto dto);

    void eliminar(Integer id);

    boolean existePorPlaca(String placa);
    
    boolean existePorCodigo(String codigo);
    
    // Contar vehículos asignados a un operador
    long contarVehiculosPorOperador(Integer operadorId);
    
    // Obtener vehículos asignados a un operador
    List<VehiculoSalidaDto> obtenerVehiculosPorOperador(Integer operadorId);
}
