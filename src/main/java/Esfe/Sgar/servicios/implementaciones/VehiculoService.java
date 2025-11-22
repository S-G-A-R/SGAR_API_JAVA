package Esfe.Sgar.servicios.implementaciones;

import Esfe.Sgar.dtos.vehiculo.VehiculoGuardarDto;
import Esfe.Sgar.dtos.vehiculo.VehiculoModificarDto;
import Esfe.Sgar.dtos.vehiculo.VehiculoSalidaDto;
import Esfe.Sgar.modelos.Foto;
import Esfe.Sgar.modelos.Marca;
import Esfe.Sgar.modelos.TipoVehiculo;
import Esfe.Sgar.modelos.Vehiculo;
import Esfe.Sgar.repositorios.FotoRepository;
import Esfe.Sgar.repositorios.MarcaRepository;
import Esfe.Sgar.repositorios.TipoVehiculoRepository;
import Esfe.Sgar.repositorios.VehiculoRepository;
import Esfe.Sgar.servicios.Interfaces.IVehiculoService;
import Esfe.Sgar.servicios.Interfaces.IExternalSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VehiculoService implements IVehiculoService {

    private final VehiculoRepository vehiculoRepository;
    private final MarcaRepository marcaRepository;
    private final TipoVehiculoRepository tipoVehiculoRepository;
    private final FotoRepository fotoRepository;
    private final IExternalSecurityService externalSecurityService;

    @Autowired
    public VehiculoService(VehiculoRepository vehiculoRepository,
                          MarcaRepository marcaRepository,
                          TipoVehiculoRepository tipoVehiculoRepository,
                          FotoRepository fotoRepository,
                          IExternalSecurityService externalSecurityService) {
        this.vehiculoRepository = vehiculoRepository;
        this.marcaRepository = marcaRepository;
        this.tipoVehiculoRepository = tipoVehiculoRepository;
        this.fotoRepository = fotoRepository;
        this.externalSecurityService = externalSecurityService;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VehiculoSalidaDto> buscarConFiltros(String placa, String codigo, Integer marcaId, 
                                                     Integer tipoVehiculoId, Byte estado, String mecanico, 
                                                     Pageable pageable) {
        return vehiculoRepository.buscarConFiltros(placa, codigo, marcaId, tipoVehiculoId, estado, mecanico, pageable)
                .map(this::toSalidaDto);
    }

    @Override
    @Transactional(readOnly = true)
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
        v.setOperadorId(dto.getIdOperador());

            // Guardar el id de organización si viene en el DTO
            v.setOrganizacionId(dto.getOrganizacionId());
        
        // Asignar foto si se proporciona
        if (dto.getIdFoto() != null) {
            Foto foto = fotoRepository.findById(dto.getIdFoto())
                .orElseThrow(() -> new IllegalArgumentException("Foto no encontrada con id: " + dto.getIdFoto()));
            v.setFoto(foto);
        }

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
           existente.setOperadorId(dto.getIdOperador());
        
           // Actualizar foto si se proporciona
           if (dto.getIdFoto() != null) {
              Foto foto = fotoRepository.findById(dto.getIdFoto())
                    .orElseThrow(() -> new IllegalArgumentException("Foto no encontrada con id: " + dto.getIdFoto()));
              existente.setFoto(foto);
        }

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
    @Transactional(readOnly = true)
    public boolean existePorPlaca(String placa) {
        return vehiculoRepository.existsByPlaca(placa);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existePorCodigo(String codigo) {
        return vehiculoRepository.existsByCodigo(codigo);
    }

    @Override
    @Transactional(readOnly = true)
    public long contarVehiculosPorOperador(Integer operadorId) {
        return vehiculoRepository.countByOperadorId(operadorId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehiculoSalidaDto> obtenerVehiculosPorOperador(Integer operadorId) {
        return vehiculoRepository.findByOperadorId(operadorId)
                .stream()
                .map(this::toSalidaDto)
                .collect(Collectors.toList());
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
        out.setOrganizacionId(v.getOrganizacionId());
        // Mapear información de la foto si existe
        if (v.getFoto() != null) {
            out.setIdFoto(v.getFoto().getId());
        }
        return out;
    }
}
