package Esfe.Sgar.servicios.Interfaces;

import Esfe.Sgar.dtos.tipovehiculo.TipoVehiculoGuardarDto;
import Esfe.Sgar.dtos.tipovehiculo.TipoVehiculoModificarDto;
import Esfe.Sgar.dtos.tipovehiculo.TipoVehiculoSalidaDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ITipoVehiculoService {

    Page<TipoVehiculoSalidaDto> buscarConFiltros(Byte tipo, String descripcion, Pageable pageable);

    TipoVehiculoSalidaDto obtenerPorId(Integer id);

    TipoVehiculoSalidaDto crear(TipoVehiculoGuardarDto dto);

    TipoVehiculoSalidaDto actualizar(Integer id, TipoVehiculoModificarDto dto);

    void eliminar(Integer id);

    boolean existePorTipo(Byte tipo);
}
