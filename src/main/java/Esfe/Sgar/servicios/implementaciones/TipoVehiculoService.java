package Esfe.Sgar.servicios.implementaciones;

import Esfe.Sgar.dtos.tipovehiculo.TipoVehiculoGuardarDto;
import Esfe.Sgar.dtos.tipovehiculo.TipoVehiculoModificarDto;
import Esfe.Sgar.dtos.tipovehiculo.TipoVehiculoSalidaDto;
import Esfe.Sgar.modelos.TipoVehiculo;
import Esfe.Sgar.repositorios.TipoVehiculoRepository;
import Esfe.Sgar.servicios.Interfaces.ITipoVehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TipoVehiculoService implements ITipoVehiculoService {

    @Autowired
    private TipoVehiculoRepository tipoVehiculoRepository;

    @Override
    public Page<TipoVehiculoSalidaDto> buscarConFiltros(Byte tipo, String descripcion, Pageable pageable) {
        return tipoVehiculoRepository.buscarConFiltros(tipo, descripcion, pageable)
                .map(this::toSalidaDto);
    }

    @Override
    public TipoVehiculoSalidaDto obtenerPorId(Integer id) {
        Optional<TipoVehiculo> opt = tipoVehiculoRepository.findById(id);
        TipoVehiculo tv = opt.orElseThrow(() -> new IllegalArgumentException("Tipo de vehículo no encontrado con id: " + id));
        return toSalidaDto(tv);
    }

    @Override
    @Transactional
    public TipoVehiculoSalidaDto crear(TipoVehiculoGuardarDto dto) {
        if (dto == null) throw new IllegalArgumentException("El DTO no puede ser nulo");
        
        // Validar que no exista el tipo
        if (tipoVehiculoRepository.existsByTipo(dto.getTipo())) {
            throw new IllegalArgumentException("Ya existe un tipo de vehículo con ese tipo: " + dto.getTipo());
        }

        TipoVehiculo tv = new TipoVehiculo();
        tv.setTipo(dto.getTipo());
        tv.setDescripcion(dto.getDescripcion());

        TipoVehiculo guardado = tipoVehiculoRepository.save(tv);
        return toSalidaDto(guardado);
    }

    @Override
    @Transactional
    public TipoVehiculoSalidaDto actualizar(Integer id, TipoVehiculoModificarDto dto) {
        TipoVehiculo existente = tipoVehiculoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tipo de vehículo no encontrado con id: " + id));

        // Validar que el nuevo tipo no exista (si cambió)
        if (!existente.getTipo().equals(dto.getTipo())) {
            if (tipoVehiculoRepository.existsByTipo(dto.getTipo())) {
                throw new IllegalArgumentException("Ya existe un tipo de vehículo con ese tipo: " + dto.getTipo());
            }
        }

        existente.setTipo(dto.getTipo());
        existente.setDescripcion(dto.getDescripcion());

        TipoVehiculo actualizado = tipoVehiculoRepository.save(existente);
        return toSalidaDto(actualizado);
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        if (!tipoVehiculoRepository.existsById(id)) {
            throw new IllegalArgumentException("Tipo de vehículo no encontrado con id: " + id);
        }
        tipoVehiculoRepository.deleteById(id);
    }

    @Override
    public boolean existePorTipo(Byte tipo) {
        return tipoVehiculoRepository.existsByTipo(tipo);
    }

    private TipoVehiculoSalidaDto toSalidaDto(TipoVehiculo tv) {
        TipoVehiculoSalidaDto out = new TipoVehiculoSalidaDto();
        out.setId(tv.getId());
        out.setTipo(tv.getTipo());
        out.setDescripcion(tv.getDescripcion());
        return out;
    }
}
