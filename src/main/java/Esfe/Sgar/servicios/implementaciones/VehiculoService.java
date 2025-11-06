package Esfe.Sgar.servicios.implementaciones;

import Esfe.Sgar.dtos.vehiculo.VehiculoGuardarDto;
import Esfe.Sgar.dtos.vehiculo.VehiculoModificarDto;
import Esfe.Sgar.dtos.vehiculo.VehiculoSalidaDto;
import Esfe.Sgar.modelos.Marca;
import Esfe.Sgar.modelos.TipoVehiculo;
import Esfe.Sgar.modelos.Vehiculo;
import Esfe.Sgar.repositorios.MarcaRepository;
import Esfe.Sgar.repositorios.TipoVehiculoRepository;
import Esfe.Sgar.repositorios.VehiculoRepository;
import Esfe.Sgar.servicios.Interfaces.IVehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class VehiculoService implements IVehiculoService {

    @Autowired
    private VehiculoRepository vehiculoRepository;
    
    @Autowired
    private MarcaRepository marcaRepository;
    
    @Autowired
    private TipoVehiculoRepository tipoVehiculoRepository;

    @Override
    public Page<VehiculoSalidaDto> buscarConFiltros(String placa, String codigo, Integer marcaId, 
                                                     Integer tipoVehiculoId, Byte estado, String mecanico, 
                                                     Pageable pageable) {
        return vehiculoRepository.buscarConFiltros(placa, codigo, marcaId, tipoVehiculoId, estado, mecanico, pageable)
                .map(this::toSalidaDto);
    }

    @Override
    public VehiculoSalidaDto obtenerPorId(Integer id) {
        Optional<Vehiculo> opt = vehiculoRepository.findById(id);
        Vehiculo v = opt.orElseThrow(() -> new IllegalArgumentException("Vehículo no encontrado con id: " + id));
        return toSalidaDto(v);
    }

    @Override
    @Transactional
    public VehiculoSalidaDto crear(VehiculoGuardarDto dto) {
        if (dto == null) throw new IllegalArgumentException("El DTO no puede ser nulo");
        
        // Validar placa única
        if (vehiculoRepository.existsByPlaca(dto.getPlaca())) {
            throw new IllegalArgumentException("Ya existe un vehículo con esa placa: " + dto.getPlaca());
        }
        
        // Validar código único
        if (vehiculoRepository.existsByCodigo(dto.getCodigo())) {
            throw new IllegalArgumentException("Ya existe un vehículo con ese código: " + dto.getCodigo());
        }
        
        // Validar que existe la marca
        Marca marca = marcaRepository.findById(dto.getIdMarca())
                .orElseThrow(() -> new IllegalArgumentException("Marca no encontrada con id: " + dto.getIdMarca()));
        
        // Validar que existe el tipo de vehículo
        TipoVehiculo tipoVehiculo = tipoVehiculoRepository.findById(dto.getIdTipoVehiculo())
                .orElseThrow(() -> new IllegalArgumentException("Tipo de vehículo no encontrado con id: " + dto.getIdTipoVehiculo()));

        Vehiculo v = new Vehiculo();
        v.setMarca(marca);
        v.setPlaca(dto.getPlaca());
        v.setCodigo(dto.getCodigo());
        v.setTipoVehiculo(tipoVehiculo);
        v.setMecanico(dto.getMecanico());
        v.setTaller(dto.getTaller());
        v.setEstado(dto.getEstado());
        v.setDescripcion(dto.getDescripcion());
        v.setFoto(dto.getFoto());
        v.setOperadorId(dto.getIdOperador());

        Vehiculo guardado = vehiculoRepository.save(v);
        return toSalidaDto(guardado);
    }

    @Override
    @Transactional
    public VehiculoSalidaDto actualizar(Integer id, VehiculoModificarDto dto) {
        Vehiculo existente = vehiculoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehículo no encontrado con id: " + id));

        // Validar placa única (si cambió)
        if (!existente.getPlaca().equals(dto.getPlaca())) {
            if (vehiculoRepository.existsByPlaca(dto.getPlaca())) {
                throw new IllegalArgumentException("Ya existe un vehículo con esa placa: " + dto.getPlaca());
            }
        }
        
        // Validar código único (si cambió)
        if (!existente.getCodigo().equals(dto.getCodigo())) {
            if (vehiculoRepository.existsByCodigo(dto.getCodigo())) {
                throw new IllegalArgumentException("Ya existe un vehículo con ese código: " + dto.getCodigo());
            }
        }
        
        // Validar marca si cambió
        if (!existente.getMarca().getId().equals(dto.getIdMarca())) {
            Marca marca = marcaRepository.findById(dto.getIdMarca())
                    .orElseThrow(() -> new IllegalArgumentException("Marca no encontrada con id: " + dto.getIdMarca()));
            existente.setMarca(marca);
        }
        
        // Validar tipo de vehículo si cambió
        if (!existente.getTipoVehiculo().getId().equals(dto.getIdTipoVehiculo())) {
            TipoVehiculo tipoVehiculo = tipoVehiculoRepository.findById(dto.getIdTipoVehiculo())
                    .orElseThrow(() -> new IllegalArgumentException("Tipo de vehículo no encontrado con id: " + dto.getIdTipoVehiculo()));
            existente.setTipoVehiculo(tipoVehiculo);
        }

        existente.setPlaca(dto.getPlaca());
        existente.setCodigo(dto.getCodigo());
        existente.setMecanico(dto.getMecanico());
        existente.setTaller(dto.getTaller());
        existente.setEstado(dto.getEstado());
        existente.setDescripcion(dto.getDescripcion());
        if (dto.getFoto() != null) {
            existente.setFoto(dto.getFoto());
        }
            existente.setOperadorId(dto.getIdOperador());

        Vehiculo actualizado = vehiculoRepository.save(existente);
        return toSalidaDto(actualizado);
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        if (!vehiculoRepository.existsById(id)) {
            throw new IllegalArgumentException("Vehículo no encontrado con id: " + id);
        }
        vehiculoRepository.deleteById(id);
    }

    @Override
    public boolean existePorPlaca(String placa) {
        return vehiculoRepository.existsByPlaca(placa);
    }
    
    @Override
    public boolean existePorCodigo(String codigo) {
        return vehiculoRepository.existsByCodigo(codigo);
    }

    private VehiculoSalidaDto toSalidaDto(Vehiculo v) {
        VehiculoSalidaDto out = new VehiculoSalidaDto();
        out.setId(v.getId());
        out.setIdMarca(v.getMarca().getId());
        out.setNombreMarca(v.getMarca().getNombre());
        out.setModeloMarca(v.getMarca().getModelo());
        out.setPlaca(v.getPlaca());
        out.setCodigo(v.getCodigo());
        out.setIdTipoVehiculo(v.getTipoVehiculo().getId());
        out.setTipoVehiculoDescripcion(v.getTipoVehiculo().getDescripcion());
        out.setMecanico(v.getMecanico());
        out.setTaller(v.getTaller());
        out.setIdOperador(v.getOperadorId());
        out.setEstado(v.getEstado());
        out.setDescripcion(v.getDescripcion());
        out.setFoto(v.getFoto());
        return out;
    }
}
