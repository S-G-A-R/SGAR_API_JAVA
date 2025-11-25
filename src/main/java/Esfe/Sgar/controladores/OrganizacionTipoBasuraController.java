package Esfe.Sgar.controladores;

import Esfe.Sgar.dtos.OrganizacionDto;
import Esfe.Sgar.dtos.OrganizacionTipoBasura.AsignarTiposBasuraDto;
import Esfe.Sgar.servicios.implementaciones.OrganizacionTipoBasuraService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organizacion-tipo-basura")
public class OrganizacionTipoBasuraController {

    private final OrganizacionTipoBasuraService service;

    public OrganizacionTipoBasuraController(OrganizacionTipoBasuraService service) {
        this.service = service;
    }

    // Asignar una clasificación de basura a una organización
    @PreAuthorize("hasAnyAuthority('ROLE_Organizacion', 'ROLE_Administrador')")
    @PostMapping("/asignar")
    public String asignarTipoBasura(@RequestBody AsignarTiposBasuraDto dto) {
        return service.asignarTipoBasura(dto);
    }

    // Obtener IDs de organizaciones por tipo de basura (endpoint corto)
    @PreAuthorize("hasAnyAuthority('ROLE_Organizacion', 'ROLE_Administrador')")
    @GetMapping("/orgs/{idTipo}")
    public List<Integer> obtenerOrganizacionesPorTipo(@PathVariable Integer idTipo) {
        return service.obtenerOrganizacionesPorTipoBasura(idTipo);
    }

    // Obtener nombres de organizaciones por tipo de basura (endpoint corto)
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('ORGANIZACION')")
    @GetMapping("/orgs-nombre/{idTipo}")
    public List<OrganizacionDto> obtenerOrganizacionesPorTipoConNombre(
            @PathVariable Integer idTipo,
            @RequestHeader("Authorization") String authorization
    ) {
        String token = authorization.replace("Bearer ", "");
        return service.obtenerOrganizacionesPorTipoBasuraConNombre(idTipo, token);
    }
}