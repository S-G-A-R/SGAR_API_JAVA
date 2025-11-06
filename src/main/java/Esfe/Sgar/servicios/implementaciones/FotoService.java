package Esfe.Sgar.servicios.implementaciones;

import Esfe.Sgar.dtos.foto.FotoGuardarDto;
import Esfe.Sgar.dtos.foto.FotoModificarDto;
import Esfe.Sgar.dtos.foto.FotoSalidaDto;
import Esfe.Sgar.modelos.Foto;
import Esfe.Sgar.repositorios.FotoRepository;
import Esfe.Sgar.servicios.Interfaces.IFotoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FotoService implements IFotoService {

    private static final Logger logger = LoggerFactory.getLogger(FotoService.class);
    
    @Value("${app.foto.maxSize:5242880}") // 5MB por defecto
    private long maxImageSize;

    private final FotoRepository fotoRepository;

    @Autowired
    public FotoService(FotoRepository fotoRepository) {
        this.fotoRepository = fotoRepository;
    }

    @Override
    public Page<FotoSalidaDto> buscarTodas(Pageable pageable) {
        logger.debug("Buscando todas las fotos con paginación");
        return fotoRepository.findAll(pageable).map(this::toSalidaDto);
    }

    @Override
    public FotoSalidaDto obtenerPorId(Integer id) {
        logger.debug("Buscando foto con ID: {}", id);
        Foto foto = fotoRepository.findById(id)
            .orElseThrow(() -> {
                logger.error("Foto no encontrada con ID: {}", id);
                return new IllegalArgumentException("Foto no encontrada con id: " + id);
            });
        return toSalidaDto(foto);
    }

    @Override
    public byte[] obtenerImagenPorId(Integer id) {
        logger.debug("Obteniendo imagen de foto con ID: {}", id);
        Foto foto = fotoRepository.findById(id)
            .orElseThrow(() -> {
                logger.error("Foto no encontrada con ID: {}", id);
                return new IllegalArgumentException("Foto no encontrada con id: " + id);
            });
        return foto.getImagen();
    }

    @Override
    @Transactional
    public FotoSalidaDto crear(FotoGuardarDto dto) {
        if (dto == null) {
            logger.error("Intento de crear foto con DTO nulo");
            throw new IllegalArgumentException("El DTO no puede ser nulo");
        }
        
        if (dto.getImagen() == null || dto.getImagen().length == 0) {
            logger.error("Intento de crear foto sin imagen");
            throw new IllegalArgumentException("La imagen es requerida");
        }

        if (dto.getImagen().length > maxImageSize) {
            logger.error("Intento de subir imagen que excede el tamaño máximo permitido: {} bytes", dto.getImagen().length);
            throw new IllegalArgumentException("La imagen excede el tamaño máximo permitido de " + maxImageSize + " bytes");
        }

        try {
            Foto f = new Foto();
            f.setImagen(dto.getImagen());
            f.setTipoMime(dto.getTipoMime());
            f.setTamano((long) dto.getImagen().length);
            
            Foto guardada = fotoRepository.save(f);
            logger.info("Foto creada exitosamente con ID: {}", guardada.getId());
            return toSalidaDto(guardada);
        } catch (Exception e) {
            logger.error("Error al crear la foto", e);
            throw new RuntimeException("Error al procesar la imagen", e);
        }
    }

    @Override
    @Transactional
    public FotoSalidaDto actualizar(Integer id, FotoModificarDto dto) {
        logger.debug("Iniciando actualización de foto con ID: {}", id);
        
        Foto existente = fotoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Intento de actualizar foto inexistente con ID: {}", id);
                    return new IllegalArgumentException("Foto no encontrada con id: " + id);
                });

        try {
            if (dto.getImagen() != null && dto.getImagen().length > 0) {
                if (dto.getImagen().length > maxImageSize) {
                    logger.error("Intento de actualizar con imagen que excede el tamaño máximo: {} bytes", dto.getImagen().length);
                    throw new IllegalArgumentException("La imagen excede el tamaño máximo permitido de " + maxImageSize + " bytes");
                }
                existente.setImagen(dto.getImagen());
                existente.setTamano((long) dto.getImagen().length);
            }
            
            if (dto.getTipoMime() != null) {
                existente.setTipoMime(dto.getTipoMime());
            }

            Foto actualizada = fotoRepository.save(existente);
            logger.info("Foto actualizada exitosamente con ID: {}", actualizada.getId());
            return toSalidaDto(actualizada);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error al actualizar la foto con ID: {}", id, e);
            throw new RuntimeException("Error al actualizar la imagen", e);
        }
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        logger.debug("Iniciando eliminación de foto con ID: {}", id);
        
        if (!fotoRepository.existsById(id)) {
            logger.error("Intento de eliminar foto inexistente con ID: {}", id);
            throw new IllegalArgumentException("Foto no encontrada con id: " + id);
        }
        
        try {
            fotoRepository.deleteById(id);
            logger.info("Foto eliminada exitosamente con ID: {}", id);
        } catch (Exception e) {
            logger.error("Error al eliminar la foto con ID: {}", id, e);
            throw new RuntimeException("Error al eliminar la imagen", e);
        }
    }

    private FotoSalidaDto toSalidaDto(Foto f) {
        FotoSalidaDto out = new FotoSalidaDto();
        out.setId(f.getId());
        out.setTipoMime(f.getTipoMime());
        out.setTamano(f.getTamano());
        return out;
    }
}
