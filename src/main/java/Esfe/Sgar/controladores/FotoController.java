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
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyAuthority('ROLE_Operador', 'ROLE_Organizacion', 'ROLE_Administrador')")
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
    @PreAuthorize("hasAnyAuthority('ROLE_Operador', 'ROLE_Organizacion', 'ROLE_Administrador')")
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
    @PreAuthorize("hasAnyAuthority('ROLE_Operador', 'ROLE_Organizacion', 'ROLE_Administrador')")
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
    @PreAuthorize("hasAnyAuthority('ROLE_Organizacion', 'ROLE_Administrador')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FotoSalidaDto> crear(
            @Parameter(description = "Archivo de imagen") @RequestParam("imagen") MultipartFile imagen,
            @Parameter(description = "Tipo MIME") @RequestParam(required = false) String tipoMime
    ) {
        try {
            // Validación y extracción de imagen en helpers para reducir duplicación
            validateImageOrThrow(imagen);
            String contentType = imagen.getContentType();

            FotoGuardarDto dto = new FotoGuardarDto();
            dto.setImagen(imagen.getBytes());
            dto.setTipoMime(tipoMime != null ? tipoMime : contentType);
            dto.setTamano(imagen.getSize());

            FotoSalidaDto creada = fotoService.crear(dto);
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("/api/fotos/" + creada.getId()));
            return new ResponseEntity<>(creada, headers, HttpStatus.CREATED);
        } catch (IOException | IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Actualizar una foto existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Foto actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Foto no encontrada"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PreAuthorize("hasAnyAuthority('ROLE_Organizacion', 'ROLE_Administrador')")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FotoSalidaDto> actualizar(
            @Parameter(description = "ID de la foto") @PathVariable Integer id,
            @Parameter(description = "Archivo de imagen") @RequestParam(value = "imagen", required = false) MultipartFile imagen,
            @Parameter(description = "Tipo MIME") @RequestParam(required = false) String tipoMime
    ) {
        try {
            FotoModificarDto dto = new FotoModificarDto();
            dto.setId(id);

            // El helper maneja la validación y población del DTO en base al multipart y tipoMime
            populateDtoFromMultipart(dto, imagen, tipoMime);

            FotoSalidaDto actualizada = fotoService.actualizar(id, dto);
            return ResponseEntity.ok(actualizada);
        } catch (IOException ex) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalArgumentException ex) {
            // IllegalArgumentException aquí se interpreta como recurso no encontrado por el servicio
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Eliminar una foto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Foto eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Foto no encontrada")
    })
    @PreAuthorize("hasAnyAuthority('ROLE_Organizacion', 'ROLE_Administrador')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(
            @Parameter(description = "ID de la foto") @PathVariable Integer id) {
        try {
            fotoService.eliminar(id);
            return ResponseEntity.ok().body(new MessageResponse("Foto eliminada exitosamente"));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Foto no encontrada"));
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new MessageResponse(ex.getMessage()));
        }
    }

    private static class MessageResponse {
        private final String mensaje;

        public MessageResponse(String mensaje) {
            this.mensaje = mensaje;
        }

        public String getMensaje() {
            return mensaje;
        }
    }

    // --- Helpers para validar y poblar DTOs con multipart ---
    private void validateImageOrThrow(MultipartFile imagen) {
        if (imagen == null || imagen.isEmpty()) {
            throw new IllegalArgumentException("Imagen vacía o nula");
        }
        String contentType = imagen.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Tipo MIME no es una imagen");
        }
    }

    private void populateDtoFromMultipart(FotoModificarDto dto, MultipartFile imagen, String tipoMime) throws java.io.IOException {
        if (imagen != null && !imagen.isEmpty()) {
            validateImageOrThrow(imagen);
            dto.setImagen(imagen.getBytes());
            dto.setTamano(imagen.getSize());
            dto.setTipoMime(tipoMime != null ? tipoMime : imagen.getContentType());
        } else if (tipoMime != null) {
            dto.setTipoMime(tipoMime);
        }
    }
}
