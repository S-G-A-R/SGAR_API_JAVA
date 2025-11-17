package Esfe.Sgar.controladores;

import Esfe.Sgar.dtos.vehiculo.VehiculoGuardarDto;
import Esfe.Sgar.dtos.vehiculo.VehiculoModificarDto;
import Esfe.Sgar.dtos.vehiculo.VehiculoSalidaDto;
import Esfe.Sgar.servicios.Interfaces.IVehiculoService;
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
import java.util.List;

@RestController
@RequestMapping("/api/vehiculos")
@Tag(name = "Vehículos", description = "API para la gestión de vehículos")
public class VehiculoController {

    private final IVehiculoService vehiculoService;

    @Autowired
    public VehiculoController(IVehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    @Operation(summary = "Buscar vehículos con filtros opcionales y paginación")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda exitosa")
    })
    @PreAuthorize("hasAnyAuthority('ROLE_Operador', 'ROLE_Organizacion', 'ROLE_Administrador')")
    @GetMapping
    public ResponseEntity<Page<VehiculoSalidaDto>> buscar(
            @Parameter(description = "Placa (opcional)") @RequestParam(required = false) String placa,
            @Parameter(description = "Código (opcional)") @RequestParam(required = false) String codigo,
            @Parameter(description = "ID de marca (opcional)") @RequestParam(required = false) Integer marcaId,
            @Parameter(description = "ID de tipo de vehículo (opcional)") @RequestParam(required = false) Integer tipoVehiculoId,
            @Parameter(description = "Estado (opcional)") @RequestParam(required = false) Byte estado,
            @Parameter(description = "Mecánico (opcional)") @RequestParam(required = false) String mecanico,
            @Parameter(description = "Información de paginación") Pageable pageable) {
        Page<VehiculoSalidaDto> page = vehiculoService.buscarConFiltros(placa, codigo, marcaId, tipoVehiculoId, estado, mecanico, pageable);
        return ResponseEntity.ok(page);
    }

    @Operation(summary = "Obtener un vehículo por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vehículo encontrado"),
        @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
    })
    @PreAuthorize("hasAnyAuthority('ROLE_Operador', 'ROLE_Organizacion', 'ROLE_Administrador')")
    @GetMapping("/{id}")
    public ResponseEntity<VehiculoSalidaDto> obtener(
            @Parameter(description = "ID del vehículo") @PathVariable Integer id) {
        try {
            VehiculoSalidaDto v = vehiculoService.obtenerPorId(id);
            return ResponseEntity.ok(v);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Crear un nuevo vehículo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Vehículo creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PreAuthorize("hasAnyAuthority('ROLE_Organizacion', 'ROLE_Administrador')")
    @PostMapping
    public ResponseEntity<VehiculoSalidaDto> crear(
        @Parameter(description = "ID de la marca") @RequestParam(required = true) Integer idMarca,
        @Parameter(description = "Placa del vehículo") @RequestParam(required = true) String placa,
        @Parameter(description = "Código del vehículo") @RequestParam(required = true) String codigo,
        @Parameter(description = "ID del tipo de vehículo") @RequestParam(required = true) Integer idTipoVehiculo,
        @Parameter(description = "Mecánico") @RequestParam(required = true) String mecanico,
        @Parameter(description = "Taller") @RequestParam(required = true) String taller,
        @Parameter(description = "ID del operador") @RequestParam(required = false) Integer idOperador,
        @Parameter(description = "ID de la foto") @RequestParam(required = false) Integer idFoto,
        @Parameter(description = "Estado (1: Activo, 0: Inactivo)") @RequestParam(required = false) String estado,
        @Parameter(description = "Descripción") @RequestParam(required = false) String descripcion
    ) {
        try {
            VehiculoGuardarDto dto = new VehiculoGuardarDto();
            dto.setIdMarca(idMarca);
            dto.setPlaca(placa);
            dto.setCodigo(codigo);
            dto.setIdTipoVehiculo(idTipoVehiculo);
            dto.setMecanico(mecanico);
            dto.setTaller(taller);
            dto.setIdOperador(idOperador);
            dto.setIdFoto(idFoto);
            dto.setEstado(estado != null ? Byte.parseByte(estado) : (byte) 1);
            dto.setDescripcion(descripcion);
            
            VehiculoSalidaDto creado = vehiculoService.crear(dto);
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("/api/vehiculos/" + creado.getId()));
            return new ResponseEntity<>(creado, headers, HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Actualizar un vehículo existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vehículo actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Vehículo no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PreAuthorize("hasAnyAuthority('ROLE_Organizacion', 'ROLE_Administrador')")
    @PutMapping("/{id}")
    public ResponseEntity<VehiculoSalidaDto> actualizar(
        @Parameter(description = "ID del vehículo") @PathVariable Integer id,
        @Parameter(description = "ID de la marca") @RequestParam(required = false) Integer idMarca,
        @Parameter(description = "Placa del vehículo") @RequestParam(required = false) String placa,
        @Parameter(description = "Código del vehículo") @RequestParam(required = false) String codigo,
        @Parameter(description = "ID del tipo de vehículo") @RequestParam(required = false) Integer idTipoVehiculo,
        @Parameter(description = "Mecánico") @RequestParam(required = false) String mecanico,
        @Parameter(description = "Taller") @RequestParam(required = false) String taller,
        @Parameter(description = "ID del operador") @RequestParam(required = false) Integer idOperador,
        @Parameter(description = "ID de la foto") @RequestParam(required = false) Integer idFoto,
        @Parameter(description = "Estado (1: Activo, 0: Inactivo)") @RequestParam(required = false) String estado,
        @Parameter(description = "Descripción") @RequestParam(required = false) String descripcion
    ) {
        try {
            VehiculoModificarDto dto = new VehiculoModificarDto();
            dto.setId(id);
            dto.setIdMarca(idMarca);
            dto.setPlaca(placa);
            dto.setCodigo(codigo);
            dto.setIdTipoVehiculo(idTipoVehiculo);
            dto.setMecanico(mecanico);
            dto.setTaller(taller);
            dto.setIdOperador(idOperador);
            dto.setIdFoto(idFoto);
            dto.setEstado(estado != null ? Byte.parseByte(estado) : null);
            dto.setDescripcion(descripcion);
            
            VehiculoSalidaDto actualizado = vehiculoService.actualizar(id, dto);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Eliminar un vehículo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Vehículo eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
    })
    @PreAuthorize("hasAnyAuthority('ROLE_Organizacion', 'ROLE_Administrador')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del vehículo") @PathVariable Integer id) {
        try {
            vehiculoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Verificar si existe una placa")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Verificación realizada exitosamente")
    })
    @PreAuthorize("hasAnyAuthority('ROLE_Operador', 'ROLE_Organizacion', 'ROLE_Administrador')")
    @GetMapping("/existe/placa")
    public ResponseEntity<Boolean> existePlaca(
            @Parameter(description = "Placa a verificar") @RequestParam String placa) {
        boolean existe = vehiculoService.existePorPlaca(placa);
        return ResponseEntity.ok(existe);
    }
    
    @Operation(summary = "Verificar si existe un código")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Verificación realizada exitosamente")
    })
    @PreAuthorize("hasAnyAuthority('ROLE_Operador', 'ROLE_Organizacion', 'ROLE_Administrador')")
    @GetMapping("/existe/codigo")
    public ResponseEntity<Boolean> existeCodigo(
            @Parameter(description = "Código a verificar") @RequestParam String codigo) {
        boolean existe = vehiculoService.existePorCodigo(codigo);
        return ResponseEntity.ok(existe);
    }

    @Operation(summary = "Contar vehículos asignados a un operador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Conteo realizado exitosamente")
    })
    @PreAuthorize("hasAnyAuthority('ROLE_Operador', 'ROLE_Organizacion', 'ROLE_Administrador')")
    @GetMapping("/operador/{operadorId}/count")
    public ResponseEntity<Long> contarVehiculosPorOperador(
            @Parameter(description = "ID del operador") @PathVariable Integer operadorId) {
        long cantidad = vehiculoService.contarVehiculosPorOperador(operadorId);
        return ResponseEntity.ok(cantidad);
    }

    @Operation(summary = "Obtener vehículos asignados a un operador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vehículos obtenidos exitosamente")
    })
    @PreAuthorize("hasAnyAuthority('ROLE_Operador', 'ROLE_Organizacion', 'ROLE_Administrador')")
    @GetMapping("/operador/{operadorId}")
    public ResponseEntity<List<VehiculoSalidaDto>> obtenerVehiculosPorOperador(
            @Parameter(description = "ID del operador") @PathVariable Integer operadorId) {
        List<VehiculoSalidaDto> vehiculos = vehiculoService.obtenerVehiculosPorOperador(operadorId);
        return ResponseEntity.ok(vehiculos);
    }

}
