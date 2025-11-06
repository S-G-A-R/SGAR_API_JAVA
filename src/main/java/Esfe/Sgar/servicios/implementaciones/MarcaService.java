package Esfe.Sgar.servicios.implementaciones;

import Esfe.Sgar.dtos.marca.MarcaGuardarDto;
import Esfe.Sgar.dtos.marca.MarcaModificarDto;
import Esfe.Sgar.dtos.marca.MarcaSalidaDto;
import Esfe.Sgar.modelos.Marca;
import Esfe.Sgar.repositorios.MarcaRepository;
import Esfe.Sgar.servicios.Interfaces.IMarcaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MarcaService implements IMarcaService {

    @Autowired
    private MarcaRepository marcaRepository;

    @Override
    public Page<MarcaSalidaDto> buscarConFiltros(String nombre, String modelo, String year, Pageable pageable) {
        return marcaRepository.buscarConFiltros(nombre, modelo, year, pageable)
                .map(this::toSalidaDto);
    }

    @Override
    public MarcaSalidaDto obtenerPorId(Integer id) {
        Optional<Marca> opt = marcaRepository.findById(id);
        Marca m = opt.orElseThrow(() -> new IllegalArgumentException("Marca no encontrada con id: " + id));
        return toSalidaDto(m);
    }

    @Override
    @Transactional
    public MarcaSalidaDto crear(MarcaGuardarDto dto) {
        if (dto == null) throw new IllegalArgumentException("El DTO no puede ser nulo");
        
        // Validar que no exista la combinación
        if (marcaRepository.existsByNombreAndModeloAndYearOfFabrication(
                dto.getNombre(), dto.getModelo(), dto.getYearOfFabrication())) {
            throw new IllegalArgumentException("Ya existe una marca con ese nombre, modelo y año de fabricación");
        }

        Marca marca = new Marca();
        marca.setNombre(dto.getNombre());
        marca.setModelo(dto.getModelo());
        marca.setYearOfFabrication(dto.getYearOfFabrication());

        Marca guardada = marcaRepository.save(marca);
        return toSalidaDto(guardada);
    }

    @Override
    @Transactional
    public MarcaSalidaDto actualizar(Integer id, MarcaModificarDto dto) {
        Marca existente = marcaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Marca no encontrada con id: " + id));

        // Validar que la nueva combinación no exista (excepto si es la misma marca)
        if (!existente.getNombre().equals(dto.getNombre()) 
            || !existente.getModelo().equals(dto.getModelo())
            || !existente.getYearOfFabrication().equals(dto.getYearOfFabrication())) {
            
            if (marcaRepository.existsByNombreAndModeloAndYearOfFabrication(
                    dto.getNombre(), dto.getModelo(), dto.getYearOfFabrication())) {
                throw new IllegalArgumentException("Ya existe una marca con ese nombre, modelo y año de fabricación");
            }
        }

        existente.setNombre(dto.getNombre());
        existente.setModelo(dto.getModelo());
        existente.setYearOfFabrication(dto.getYearOfFabrication());

        Marca actualizada = marcaRepository.save(existente);
        return toSalidaDto(actualizada);
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        if (!marcaRepository.existsById(id)) {
            throw new IllegalArgumentException("Marca no encontrada con id: " + id);
        }
        marcaRepository.deleteById(id);
    }

    @Override
    public boolean existeCombinacion(String nombre, String modelo, String yearOfFabrication) {
        return marcaRepository.existsByNombreAndModeloAndYearOfFabrication(nombre, modelo, yearOfFabrication);
    }

    private MarcaSalidaDto toSalidaDto(Marca m) {
        MarcaSalidaDto out = new MarcaSalidaDto();
        out.setId(m.getId());
        out.setNombre(m.getNombre());
        out.setModelo(m.getModelo());
        out.setYearOfFabrication(m.getYearOfFabrication());
        return out;
    }
}
