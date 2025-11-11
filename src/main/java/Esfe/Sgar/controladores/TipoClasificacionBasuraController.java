package Esfe.Sgar.controladores;

import Esfe.Sgar.dtos.tipoclasificacionbasura.TipoClasificacionBasuraGuardarDto;
import Esfe.Sgar.dtos.tipoclasificacionbasura.TipoClasificacionBasuraModificarDto;
import Esfe.Sgar.dtos.tipoclasificacionbasura.TipoClasificacionBasuraSalidaDto;
import Esfe.Sgar.servicios.Interfaces.ITipoClasificacionBasuraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/tipos-clasificacion-basura")
@Tag(name = "Tipos de Clasificación de Basura", description = "API para la gestión de tipos de clasificación de basura")
public class TipoClasificacionBasuraController {

    private final ITipoClasificacionBasuraService service;

    @Autowired
    public TipoClasificacionBasuraController(ITipoClasificacionBasuraService service) {
        this.service = service;
    }

    @Operation(summary = "Buscar tipos de clasificación de basura por nombre con paginación")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda exitosa")
    })
    @PreAuthorize("hasAnyAuthority('ROLE_Operador', 'ROLE_Organizacion', 'ROLE_Administrador')")
    @GetMapping
    public ResponseEntity<Page<TipoClasificacionBasuraSalidaDto>> buscar(
            @Parameter(description = "Nombre del tipo (búsqueda parcial, opcional)") 
            @RequestParam(required = false) String nombre,
            @Parameter(description = "Información de paginación") 
            Pageable pageable) {
        Page<TipoClasificacionBasuraSalidaDto> page = service.buscarPorNombre(nombre, pageable);
        return ResponseEntity.ok(page);
    }

    @Operation(summary = "Obtener un tipo de clasificación de basura por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tipo encontrado"),
        @ApiResponse(responseCode = "404", description = "Tipo no encontrado")
    })
    @PreAuthorize("hasAnyAuthority('ROLE_Operador', 'ROLE_Organizacion', 'ROLE_Administrador')")
    @GetMapping("/{id}")
    public ResponseEntity<TipoClasificacionBasuraSalidaDto> obtener(
            @Parameter(description = "ID del tipo de clasificación") 
            @PathVariable Integer id) {
        try {
            TipoClasificacionBasuraSalidaDto tipo = service.obtenerPorId(id);
            return ResponseEntity.ok(tipo);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Crear un nuevo tipo de clasificación de basura")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Tipo creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o nombre duplicado")
    })
    @PreAuthorize("hasAnyAuthority('ROLE_Organizacion', 'ROLE_Administrador')")
    @PostMapping
    public ResponseEntity<TipoClasificacionBasuraSalidaDto> crear(
            @Valid @RequestBody TipoClasificacionBasuraGuardarDto dto) {
        try {
            TipoClasificacionBasuraSalidaDto creado = service.crear(dto);
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("/api/tipos-clasificacion-basura/" + creado.getId()));
            return new ResponseEntity<>(creado, headers, HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Actualizar un tipo de clasificación de basura existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tipo actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Tipo no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o nombre duplicado")
    })
    @PreAuthorize("hasAnyAuthority('ROLE_Organizacion', 'ROLE_Administrador')")
    @PutMapping("/{id}")
    public ResponseEntity<TipoClasificacionBasuraSalidaDto> actualizar(
            @Parameter(description = "ID del tipo de clasificación") 
            @PathVariable Integer id,
            @Valid @RequestBody TipoClasificacionBasuraModificarDto dto) {
        try {
            if (dto.getId() != null && !dto.getId().equals(id)) {
                return ResponseEntity.badRequest().build();
            }
            TipoClasificacionBasuraSalidaDto actualizado = service.actualizar(id, dto);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Eliminar un tipo de clasificación de basura")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Tipo eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Tipo no encontrado"),
        @ApiResponse(responseCode = "409", description = "No se puede eliminar, existen registros relacionados")
    })
    @PreAuthorize("hasAnyAuthority('ROLE_Organizacion', 'ROLE_Administrador')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del tipo de clasificación") 
            @PathVariable Integer id) {
        try {
            service.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
