package Esfe.Sgar.controladores;

import Esfe.Sgar.dtos.marca.MarcaGuardarDto;
import Esfe.Sgar.dtos.marca.MarcaModificarDto;
import Esfe.Sgar.dtos.marca.MarcaSalidaDto;
import Esfe.Sgar.servicios.Interfaces.IMarcaService;
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
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/marcas")
@Tag(name = "Marcas", description = "API para la gestión de marcas de vehículos")
public class MarcaController {

    private final IMarcaService marcaService;

    @Autowired
    public MarcaController(IMarcaService marcaService) {
        this.marcaService = marcaService;
    }

    @Operation(summary = "Buscar marcas con filtros opcionales y paginación")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda exitosa")
    })
    @GetMapping
    public ResponseEntity<Page<MarcaSalidaDto>> buscar(
            @Parameter(description = "Nombre de la marca (opcional)") @RequestParam(required = false) String nombre,
            @Parameter(description = "Modelo (opcional)") @RequestParam(required = false) String modelo,
            @Parameter(description = "Año de fabricación (opcional)") @RequestParam(required = false) String year,
            @Parameter(description = "Información de paginación") Pageable pageable) {
        Page<MarcaSalidaDto> page = marcaService.buscarConFiltros(nombre, modelo, year, pageable);
        return ResponseEntity.ok(page);
    }

    @Operation(summary = "Obtener una marca por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Marca encontrada"),
        @ApiResponse(responseCode = "404", description = "Marca no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MarcaSalidaDto> obtener(
            @Parameter(description = "ID de la marca") @PathVariable Integer id) {
        try {
            MarcaSalidaDto marca = marcaService.obtenerPorId(id);
            return ResponseEntity.ok(marca);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Crear una nueva marca")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Marca creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping
    public ResponseEntity<MarcaSalidaDto> crear(@Valid @RequestBody MarcaGuardarDto dto) {
        try {
            MarcaSalidaDto creada = marcaService.crear(dto);
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("/api/marcas/" + creada.getId()));
            return new ResponseEntity<>(creada, headers, HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Actualizar una marca existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Marca actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Marca no encontrada"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MarcaSalidaDto> actualizar(
            @Parameter(description = "ID de la marca") @PathVariable Integer id,
            @Valid @RequestBody MarcaModificarDto dto) {
        try {
            if (dto.getId() != null && !dto.getId().equals(id)) {
                return ResponseEntity.badRequest().build();
            }
            MarcaSalidaDto actualizada = marcaService.actualizar(id, dto);
            return ResponseEntity.ok(actualizada);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Eliminar una marca")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Marca eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Marca no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID de la marca") @PathVariable Integer id) {
        try {
            marcaService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Verificar si existe una combinación de marca, modelo y año")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Verificación realizada exitosamente")
    })
    @GetMapping("/existe")
    public ResponseEntity<Boolean> existeCombinacion(
            @Parameter(description = "Nombre de la marca") @RequestParam String nombre,
            @Parameter(description = "Modelo") @RequestParam String modelo,
            @Parameter(description = "Año de fabricación") @RequestParam String year) {
        boolean existe = marcaService.existeCombinacion(nombre, modelo, year);
        return ResponseEntity.ok(existe);
    }
}
