package Esfe.Sgar.servicios.Interfaces;

import Esfe.Sgar.dtos.tipoclasificacionbasura.TipoClasificacionBasuraGuardarDto;
import Esfe.Sgar.dtos.tipoclasificacionbasura.TipoClasificacionBasuraModificarDto;
import Esfe.Sgar.dtos.tipoclasificacionbasura.TipoClasificacionBasuraSalidaDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ITipoClasificacionBasuraService {

    Page<TipoClasificacionBasuraSalidaDto> buscarPorNombre(String nombre, Pageable pageable);

    TipoClasificacionBasuraSalidaDto obtenerPorId(Integer id);

    TipoClasificacionBasuraSalidaDto crear(TipoClasificacionBasuraGuardarDto dto);

    TipoClasificacionBasuraSalidaDto actualizar(Integer id, TipoClasificacionBasuraModificarDto dto);

    void eliminar(Integer id);
}
