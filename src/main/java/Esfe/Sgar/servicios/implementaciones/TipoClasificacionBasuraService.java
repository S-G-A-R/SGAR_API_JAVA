package Esfe.Sgar.servicios.implementaciones;

import Esfe.Sgar.dtos.tipoclasificacionbasura.TipoClasificacionBasuraGuardarDto;
import Esfe.Sgar.dtos.tipoclasificacionbasura.TipoClasificacionBasuraModificarDto;
import Esfe.Sgar.dtos.tipoclasificacionbasura.TipoClasificacionBasuraSalidaDto;
import Esfe.Sgar.modelos.TipoClasificacionBasura;
import Esfe.Sgar.repositorios.TipoClasificacionBasuraRepository;
import Esfe.Sgar.servicios.Interfaces.ITipoClasificacionBasuraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TipoClasificacionBasuraService implements ITipoClasificacionBasuraService {

    private final TipoClasificacionBasuraRepository repository;

    @Autowired
    public TipoClasificacionBasuraService(TipoClasificacionBasuraRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TipoClasificacionBasuraSalidaDto> buscarPorNombre(String nombre, Pageable pageable) {
        Page<TipoClasificacionBasura> page = repository.buscarPorNombre(nombre, pageable);
        return page.map(this::toSalidaDto);
    }

    @Override
    @Transactional(readOnly = true)
    public TipoClasificacionBasuraSalidaDto obtenerPorId(Integer id) {
        TipoClasificacionBasura tipo = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tipo de clasificación de basura no encontrado con id: " + id));
        return toSalidaDto(tipo);
    }

    @Override
    @Transactional
    public TipoClasificacionBasuraSalidaDto crear(TipoClasificacionBasuraGuardarDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("El DTO no puede ser nulo");
        }

        // Validar que no exista otro tipo con el mismo nombre
        if (repository.existsByNombre(dto.getNombre())) {
            throw new IllegalArgumentException("Ya existe un tipo de clasificación de basura con el nombre: " + dto.getNombre());
        }

        TipoClasificacionBasura tipo = new TipoClasificacionBasura();
        tipo.setNombre(dto.getNombre());

        TipoClasificacionBasura guardado = repository.save(tipo);
        return toSalidaDto(guardado);
    }

    @Override
    @Transactional
    public TipoClasificacionBasuraSalidaDto actualizar(Integer id, TipoClasificacionBasuraModificarDto dto) {
        TipoClasificacionBasura existente = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tipo de clasificación de basura no encontrado con id: " + id));

        // Validar nombre único solo si cambió
        if (!existente.getNombre().equals(dto.getNombre())) {
            if (repository.existsByNombre(dto.getNombre())) {
                throw new IllegalArgumentException("Ya existe un tipo de clasificación de basura con el nombre: " + dto.getNombre());
            }
        }

        existente.setNombre(dto.getNombre());

        TipoClasificacionBasura actualizado = repository.save(existente);
        return toSalidaDto(actualizado);
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        TipoClasificacionBasura tipo = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tipo de clasificación de basura no encontrado con id: " + id));
        repository.delete(tipo);
    }

    // Método helper para convertir entidad a DTO de salida
    private TipoClasificacionBasuraSalidaDto toSalidaDto(TipoClasificacionBasura tipo) {
        TipoClasificacionBasuraSalidaDto dto = new TipoClasificacionBasuraSalidaDto();
        dto.setId(tipo.getId());
        dto.setNombre(tipo.getNombre());
        return dto;
    }
}
