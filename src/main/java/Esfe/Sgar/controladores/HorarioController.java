package Esfe.Sgar.controladores;
import Esfe.Sgar.dtos.horario.HorarioGuardarDto;
import Esfe.Sgar.dtos.horario.HorarioModificarDto;
import Esfe.Sgar.dtos.horario.HorarioSalidaDto;
import Esfe.Sgar.servicios.Interfaces.IHorarioService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/api/horarios")
@Tag(name = "Horarios", description = "API para la gestión de horarios de operadores")
public class HorarioController {

    private final IHorarioService horarioService;

    @Autowired
    public HorarioController(IHorarioService horarioService) {
        this.horarioService = horarioService;
    }

    @Operation(summary = "Obtener todos los horarios con filtros opcionales")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Horarios encontrados"),
        @ApiResponse(responseCode = "400", description = "Parámetros de tiempo inválidos")
    })
    @PreAuthorize("hasAnyAuthority('ROLE_Operador', 'ROLE_Organizacion', 'ROLE_Administrador')")
    @GetMapping
    public ResponseEntity<Page<HorarioSalidaDto>> listar(
        @Parameter(description = "ID de la organización") @RequestParam(required = false) Integer organizacion,
            @Parameter(description = "ID de la zona") @RequestParam(required = false) Integer zonaId,
            @Parameter(description = "Turno (1: mañana, 2: tarde, 3: noche)") @RequestParam(required = false) Byte turno,
            @Parameter(description = "Día de la semana") @RequestParam(required = false) String dia,
            @Parameter(description = "Hora de inicio (HH:mm)") @RequestParam(required = false) String inicio,
            @Parameter(description = "Hora de fin (HH:mm)") @RequestParam(required = false) String fin,
            @Parameter(description = "Información de paginación") Pageable pageable
    ) {
        LocalTime tInicio = null;
        LocalTime tFin = null;
        try {
            if (inicio != null && !inicio.isBlank()) tInicio = LocalTime.parse(inicio);
            if (fin != null && !fin.isBlank()) tFin = LocalTime.parse(fin);
        } catch (DateTimeParseException ex) {
            return ResponseEntity.badRequest().build();
        }

        Page<HorarioSalidaDto> page = horarioService.filtrarHorarios(organizacion, zonaId, turno, dia, tInicio, tFin, pageable);
        return ResponseEntity.ok(page);
    }

    @Operation(summary = "Obtener un horario por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Horario encontrado"),
        @ApiResponse(responseCode = "404", description = "Horario no encontrado")
    })
    @PreAuthorize("hasAnyAuthority('ROLE_Operador', 'ROLE_Organizacion', 'ROLE_Administrador')")
    @GetMapping("/{id}")
    public ResponseEntity<HorarioSalidaDto> obtener(
            @Parameter(description = "ID del horario") @PathVariable Integer id) {
        try {
            HorarioSalidaDto h = horarioService.obtenerPorId(id);
            return ResponseEntity.ok(h);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Crear un nuevo horario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Horario creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PreAuthorize("hasAnyAuthority('ROLE_Organizacion', 'ROLE_Administrador')")
    @PostMapping
    public ResponseEntity<HorarioSalidaDto> crear(
        @Parameter(description = "Hora de entrada (HH:mm:ss)") @RequestParam String horaEntrada,
        @Parameter(description = "Hora de salida (HH:mm:ss)") @RequestParam String horaSalida,
        @Parameter(description = "Día de la semana") @RequestParam String dia,
        @Parameter(description = "ID de la organización") @RequestParam Integer idOrganizacion,
        @Parameter(description = "Turno (1: mañana, 2: tarde, 3: noche)") @RequestParam Byte turno,
        @Parameter(description = "ID de la zona") @RequestParam Integer zonaId
    ) {
        try {
            HorarioGuardarDto dto = new HorarioGuardarDto();
            dto.setHoraEntrada(LocalTime.parse(horaEntrada));
            dto.setHoraSalida(LocalTime.parse(horaSalida));
            dto.setDia(dia);
            dto.setIdOrganizacion(idOrganizacion);
            dto.setTurno(turno);
            dto.setZonaId(zonaId);
            
            HorarioSalidaDto creado = horarioService.crear(dto);
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("/api/horarios/" + creado.getId()));
            return new ResponseEntity<>(creado, headers, HttpStatus.CREATED);
        } catch (DateTimeParseException ex) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Actualizar un horario existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Horario actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Horario no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PreAuthorize("hasAnyAuthority('ROLE_Organizacion', 'ROLE_Administrador')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<HorarioSalidaDto> actualizar(
        @Parameter(description = "ID del horario") @PathVariable Integer id,
        @Parameter(description = "Hora de entrada (HH:mm:ss)") @RequestParam String horaEntrada,
        @Parameter(description = "Hora de salida (HH:mm:ss)") @RequestParam String horaSalida,
        @Parameter(description = "Día de la semana") @RequestParam String dia,
        @Parameter(description = "ID de la organización") @RequestParam Integer idOrganizacion,
        @Parameter(description = "Turno (1: mañana, 2: tarde, 3: noche)") @RequestParam Byte turno,
        @Parameter(description = "ID de la zona") @RequestParam Integer zonaId
    ) {
        try {
            HorarioModificarDto dto = new HorarioModificarDto();
            dto.setId(id);
            dto.setHoraEntrada(LocalTime.parse(horaEntrada));
            dto.setHoraSalida(LocalTime.parse(horaSalida));
            dto.setDia(dia);
            dto.setIdOrganizacion(idOrganizacion);
            dto.setTurno(turno);
            dto.setZonaId(zonaId);
            
            HorarioSalidaDto actualizado = horarioService.actualizar(id, dto);
            return ResponseEntity.ok(actualizado);
        } catch (DateTimeParseException ex) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Eliminar un horario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Horario eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Horario no encontrado")
    })
    @PreAuthorize("hasAnyAuthority('ROLE_Organizacion', 'ROLE_Administrador')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del horario") @PathVariable Integer id) {
        try {
            horarioService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Verificar superposición de horarios")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Verificación realizada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Formato de hora inválido")
    })
    @PreAuthorize("hasAnyAuthority('ROLE_Operador', 'ROLE_Organizacion', 'ROLE_Administrador')")
    @GetMapping("/check-overlap")
    public ResponseEntity<Boolean> checkOverlap(
            @Parameter(description = "ID de la organización") @RequestParam Integer organizacionId,
            @Parameter(description = "Día de la semana") @RequestParam String dia,
            @Parameter(description = "Hora de entrada (HH:mm)") @RequestParam String horaEntrada,
            @Parameter(description = "Hora de salida (HH:mm)") @RequestParam String horaSalida) {
        try {
            LocalTime he = LocalTime.parse(horaEntrada);
            LocalTime hs = LocalTime.parse(horaSalida);
            boolean existe = horarioService.existeSuperposicion(organizacionId, dia, he, hs);
            return ResponseEntity.ok(existe);
        } catch (DateTimeParseException ex) {
            return ResponseEntity.badRequest().build();
        }
    }
}
