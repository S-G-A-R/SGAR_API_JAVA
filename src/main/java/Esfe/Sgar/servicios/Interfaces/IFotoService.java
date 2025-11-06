package Esfe.Sgar.servicios.Interfaces;

import Esfe.Sgar.dtos.foto.FotoGuardarDto;
import Esfe.Sgar.dtos.foto.FotoModificarDto;
import Esfe.Sgar.dtos.foto.FotoSalidaDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IFotoService {
    Page<FotoSalidaDto> buscarTodas(Pageable pageable);
    FotoSalidaDto obtenerPorId(Integer id);
    byte[] obtenerImagenPorId(Integer id);
    FotoSalidaDto crear(FotoGuardarDto dto);
    FotoSalidaDto actualizar(Integer id, FotoModificarDto dto);
    void eliminar(Integer id);
}
