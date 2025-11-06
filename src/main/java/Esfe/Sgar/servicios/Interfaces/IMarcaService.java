package Esfe.Sgar.servicios.Interfaces;

import Esfe.Sgar.dtos.marca.MarcaGuardarDto;
import Esfe.Sgar.dtos.marca.MarcaModificarDto;
import Esfe.Sgar.dtos.marca.MarcaSalidaDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IMarcaService {

    Page<MarcaSalidaDto> buscarConFiltros(String nombre, String modelo, String year, Pageable pageable);

    MarcaSalidaDto obtenerPorId(Integer id);

    MarcaSalidaDto crear(MarcaGuardarDto dto);

    MarcaSalidaDto actualizar(Integer id, MarcaModificarDto dto);

    void eliminar(Integer id);

    boolean existeCombinacion(String nombre, String modelo, String yearOfFabrication);
}
