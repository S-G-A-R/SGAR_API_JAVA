package Esfe.Sgar.controladores;
import Esfe.Sgar.modelos.Horario;
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
    @GetMapping
    public ResponseEntity<Page<Horario>> listar(
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

        Page<Horario> page = horarioService.filtrarHorarios(organizacion, zonaId, turno, dia, tInicio, tFin, pageable);
        return ResponseEntity.ok(page);
    }

    @Operation(summary = "Obtener un horario por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Horario encontrado"),
        @ApiResponse(responseCode = "404", description = "Horario no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Horario> obtener(
            @Parameter(description = "ID del horario") @PathVariable Integer id) {
        try {
            Horario h = horarioService.obtenerPorId(id);
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
    @PostMapping
    public ResponseEntity<Horario> crear(
        @Parameter(description = "ID de la organización") @RequestParam Integer organizacion,
        @Parameter(description = "ID de la zona") @RequestParam Integer zonaId,
        @Parameter(description = "Turno (1: mañana, 2: tarde, 3: noche)") @RequestParam Byte turno,
        @Parameter(description = "Día de la semana") @RequestParam String dia,
        @Parameter(description = "Hora de entrada (HH:mm)") @RequestParam String horaEntrada,
        @Parameter(description = "Hora de salida (HH:mm)") @RequestParam String horaSalida
    ) {
        try {
            Horario horario = new Horario();
            horario.setIdOrganizacion(organizacion);
            horario.setZonaId(zonaId);
            horario.setTurno(turno);
            horario.setDia(dia);
            horario.setHoraEntrada(LocalTime.parse(horaEntrada));
            horario.setHoraSalida(LocalTime.parse(horaSalida));

            Horario creado = horarioService.crear(horario);
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("/api/horarios/" + creado.getId()));
            return new ResponseEntity<>(creado, headers, HttpStatus.CREATED);
        } catch (DateTimeParseException e) {
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
    @PutMapping(value = "/{id}")
    public ResponseEntity<Horario> actualizar(
        @Parameter(description = "ID del horario") @PathVariable Integer id,
        @Parameter(description = "ID de la organización") @RequestParam Integer organizacion,
        @Parameter(description = "ID de la zona") @RequestParam Integer zonaId,
        @Parameter(description = "Turno (1: mañana, 2: tarde, 3: noche)") @RequestParam Byte turno,
        @Parameter(description = "Día de la semana") @RequestParam String dia,
        @Parameter(description = "Hora de entrada (HH:mm)") @RequestParam String horaEntrada,
        @Parameter(description = "Hora de salida (HH:mm)") @RequestParam String horaSalida
    ) {
        try {
            Horario horario = new Horario();
            horario.setIdOrganizacion(organizacion);
            horario.setZonaId(zonaId);
            horario.setTurno(turno);
            horario.setDia(dia);
            horario.setHoraEntrada(LocalTime.parse(horaEntrada));
            horario.setHoraSalida(LocalTime.parse(horaSalida));

            Horario actualizado = horarioService.actualizar(id, horario);
            return ResponseEntity.ok(actualizado);
        } catch (DateTimeParseException e) {
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
