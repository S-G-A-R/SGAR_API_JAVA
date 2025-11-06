package Esfe.Sgar.controladores;

import Esfe.Sgar.dtos.foto.FotoGuardarDto;
import Esfe.Sgar.dtos.foto.FotoModificarDto;
import Esfe.Sgar.dtos.foto.FotoSalidaDto;
import Esfe.Sgar.servicios.Interfaces.IFotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/api/fotos")
@Tag(name = "Fotos", description = "API para la gestión de fotos")
public class FotoController {

    private final IFotoService fotoService;

    @Autowired
    public FotoController(IFotoService fotoService) {
        this.fotoService = fotoService;
    }

    @Operation(summary = "Buscar todas las fotos con paginación")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda exitosa")
    })
    @GetMapping
    public ResponseEntity<Page<FotoSalidaDto>> buscar(
            @Parameter(description = "Información de paginación") Pageable pageable) {
        Page<FotoSalidaDto> page = fotoService.buscarTodas(pageable);
        return ResponseEntity.ok(page);
    }

    @Operation(summary = "Obtener una foto por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Foto encontrada"),
        @ApiResponse(responseCode = "404", description = "Foto no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<FotoSalidaDto> obtener(
            @Parameter(description = "ID de la foto") @PathVariable Integer id) {
        try {
            FotoSalidaDto foto = fotoService.obtenerPorId(id);
            return ResponseEntity.ok(foto);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Obtener la imagen de una foto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Imagen encontrada"),
        @ApiResponse(responseCode = "404", description = "Foto no encontrada")
    })
    @GetMapping("/{id}/imagen")
    public ResponseEntity<byte[]> obtenerImagen(
            @Parameter(description = "ID de la foto") @PathVariable Integer id) {
        try {
            byte[] imagen = fotoService.obtenerImagenPorId(id);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imagen);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Subir una nueva foto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Foto subida exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FotoSalidaDto> crear(
            @Parameter(description = "Archivo de imagen") @RequestParam("imagen") MultipartFile imagen,
            @Parameter(description = "Tipo MIME") @RequestParam(required = false) String tipoMime
    ) {
        try {
            if (imagen == null || imagen.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            // Validar que sea una imagen
            String contentType = imagen.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest().build();
            }

            FotoGuardarDto dto = new FotoGuardarDto();
            dto.setImagen(imagen.getBytes());
            dto.setTipoMime(tipoMime != null ? tipoMime : contentType);
            dto.setTamano(imagen.getSize());

            FotoSalidaDto creada = fotoService.crear(dto);
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("/api/fotos/" + creada.getId()));
            return new ResponseEntity<>(creada, headers, HttpStatus.CREATED);
        } catch (IOException ex) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Actualizar una foto existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Foto actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Foto no encontrada"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FotoSalidaDto> actualizar(
            @Parameter(description = "ID de la foto") @PathVariable Integer id,
            @Parameter(description = "Archivo de imagen") @RequestParam(value = "imagen", required = false) MultipartFile imagen,
            @Parameter(description = "Tipo MIME") @RequestParam(required = false) String tipoMime
    ) {
        try {
            FotoModificarDto dto = new FotoModificarDto();
            dto.setId(id);

            if (imagen != null && !imagen.isEmpty()) {
                // Validar que sea una imagen
                String contentType = imagen.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.badRequest().build();
                }
                dto.setImagen(imagen.getBytes());
                dto.setTamano(imagen.getSize());
                dto.setTipoMime(tipoMime != null ? tipoMime : contentType);
            } else if (tipoMime != null) {
                dto.setTipoMime(tipoMime);
            }

            FotoSalidaDto actualizada = fotoService.actualizar(id, dto);
            return ResponseEntity.ok(actualizada);
        } catch (IOException ex) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Eliminar una foto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Foto eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Foto no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID de la foto") @PathVariable Integer id) {
        try {
            fotoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
