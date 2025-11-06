package Esfe.Sgar.controladores;

import Esfe.Sgar.dtos.tipovehiculo.TipoVehiculoGuardarDto;
import Esfe.Sgar.dtos.tipovehiculo.TipoVehiculoModificarDto;
import Esfe.Sgar.dtos.tipovehiculo.TipoVehiculoSalidaDto;
import Esfe.Sgar.servicios.Interfaces.ITipoVehiculoService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/tipos-vehiculos")
@Tag(name = "Tipos de Vehículos", description = "API para la gestión de tipos de vehículos")
public class TipoVehiculoController {

    private final ITipoVehiculoService tipoVehiculoService;

    @Autowired
    public TipoVehiculoController(ITipoVehiculoService tipoVehiculoService) {
        this.tipoVehiculoService = tipoVehiculoService;
    }

    @Operation(summary = "Buscar tipos de vehículos con filtros opcionales y paginación")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda exitosa")
    })
    @GetMapping
    public ResponseEntity<Page<TipoVehiculoSalidaDto>> buscar(
            @Parameter(description = "Tipo (opcional)") @RequestParam(required = false) Byte tipo,
            @Parameter(description = "Descripción (opcional)") @RequestParam(required = false) String descripcion,
            @Parameter(description = "Información de paginación") Pageable pageable) {
        Page<TipoVehiculoSalidaDto> page = tipoVehiculoService.buscarConFiltros(tipo, descripcion, pageable);
        return ResponseEntity.ok(page);
    }

    @Operation(summary = "Obtener un tipo de vehículo por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tipo encontrado"),
        @ApiResponse(responseCode = "404", description = "Tipo no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TipoVehiculoSalidaDto> obtener(
            @Parameter(description = "ID del tipo de vehículo") @PathVariable Integer id) {
        try {
            TipoVehiculoSalidaDto tv = tipoVehiculoService.obtenerPorId(id);
            return ResponseEntity.ok(tv);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Crear un nuevo tipo de vehículo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Tipo creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping
    public ResponseEntity<TipoVehiculoSalidaDto> crear(
        @Parameter(description = "Tipo (valor numérico)") @RequestParam Byte tipo,
        @Parameter(description = "Descripción del tipo") @RequestParam String descripcion
    ) {
        try {
            TipoVehiculoGuardarDto dto = new TipoVehiculoGuardarDto();
            dto.setTipo(tipo);
            dto.setDescripcion(descripcion);
            
            TipoVehiculoSalidaDto creado = tipoVehiculoService.crear(dto);
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("/api/tipos-vehiculos/" + creado.getId()));
            return new ResponseEntity<>(creado, headers, HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Actualizar un tipo de vehículo existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tipo actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Tipo no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TipoVehiculoSalidaDto> actualizar(
        @Parameter(description = "ID del tipo de vehículo") @PathVariable Integer id,
        @Parameter(description = "Tipo (valor numérico)") @RequestParam Byte tipo,
        @Parameter(description = "Descripción del tipo") @RequestParam String descripcion
    ) {
        try {
            TipoVehiculoModificarDto dto = new TipoVehiculoModificarDto();
            dto.setId(id);
            dto.setTipo(tipo);
            dto.setDescripcion(descripcion);
            
            TipoVehiculoSalidaDto actualizado = tipoVehiculoService.actualizar(id, dto);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Eliminar un tipo de vehículo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Tipo eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Tipo no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del tipo de vehículo") @PathVariable Integer id) {
        try {
            tipoVehiculoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Verificar si existe un tipo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Verificación realizada exitosamente")
    })
    @GetMapping("/existe")
    public ResponseEntity<Boolean> existe(
            @Parameter(description = "Tipo a verificar") @RequestParam Byte tipo) {
        boolean existe = tipoVehiculoService.existePorTipo(tipo);
        return ResponseEntity.ok(existe);
    }
}
