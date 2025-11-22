package Esfe.Sgar.controladores;

import Esfe.Sgar.dtos.operadorhorario.OperadorHorarioGuardarDto;
import Esfe.Sgar.dtos.operadorhorario.OperadorHorarioModificarDto;
import Esfe.Sgar.dtos.operadorhorario.OperadorHorarioSalidaDto;
import Esfe.Sgar.servicios.Interfaces.IOperadorHorarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/operador-horarios")
@Tag(name = "OperadorHorario", description = "API para asignar horarios a operadores")
public class OperadorHorarioController {
    private final IOperadorHorarioService operadorHorarioService;

    @Autowired
    public OperadorHorarioController(IOperadorHorarioService operadorHorarioService) {
        this.operadorHorarioService = operadorHorarioService;
    }

    @Operation(summary = "Buscar asignaciones con filtros y paginación")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Búsqueda exitosa")})
    @PreAuthorize("hasAnyAuthority('ROLE_Operador', 'ROLE_Organizacion', 'ROLE_Administrador')")
    @GetMapping
    public ResponseEntity<Page<OperadorHorarioSalidaDto>> buscar(
            @Parameter(description = "ID del operador") @RequestParam(required = false) Integer operadorId,
            @Parameter(description = "ID del horario") @RequestParam(required = false) Integer horarioId,
            Pageable pageable) {
        Page<OperadorHorarioSalidaDto> page = operadorHorarioService.buscarConFiltros(operadorId, horarioId, pageable);
        return ResponseEntity.ok(page);
    }

    @Operation(summary = "Obtener asignación por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Asignación encontrada"),
        @ApiResponse(responseCode = "404", description = "No encontrada")
    })
    @PreAuthorize("hasAnyAuthority('ROLE_Operador', 'ROLE_Organizacion', 'ROLE_Administrador')")
    @GetMapping("/{id}")
    public ResponseEntity<OperadorHorarioSalidaDto> obtener(@PathVariable Integer id) {
        try {
            OperadorHorarioSalidaDto dto = operadorHorarioService.obtenerPorId(id);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Crear nueva asignación")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Asignación creada"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PreAuthorize("hasAnyAuthority('ROLE_Organizacion', 'ROLE_Administrador')")
    @PostMapping
    public ResponseEntity<OperadorHorarioSalidaDto> crear(@Valid @RequestBody OperadorHorarioGuardarDto dto) {
        try {
            OperadorHorarioSalidaDto creado = operadorHorarioService.crear(dto);
            return new ResponseEntity<>(creado, HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Actualizar asignación")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Asignación actualizada"),
        @ApiResponse(responseCode = "404", description = "No encontrada"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PreAuthorize("hasAnyAuthority('ROLE_Organizacion', 'ROLE_Administrador')")
    @PutMapping("/{id}")
    public ResponseEntity<OperadorHorarioSalidaDto> actualizar(@PathVariable Integer id, @Valid @RequestBody OperadorHorarioModificarDto dto) {
        try {
            if (dto.getId() != null && !dto.getId().equals(id)) {
                return ResponseEntity.badRequest().build();
            }
            OperadorHorarioSalidaDto actualizado = operadorHorarioService.actualizar(id, dto);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Eliminar asignación")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Asignación eliminada"),
        @ApiResponse(responseCode = "404", description = "No encontrada")
    })
    @PreAuthorize("hasAnyAuthority('ROLE_Organizacion', 'ROLE_Administrador')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        try {
            operadorHorarioService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
