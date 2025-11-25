package Esfe.Sgar.servicios.implementaciones;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import Esfe.Sgar.modelos.OrganizacionTipoBasura;
import Esfe.Sgar.repositorios.OrganizacionTipoBasuraRepository;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import Esfe.Sgar.dtos.OrganizacionResponse;

import org.springframework.stereotype.Service;

import Esfe.Sgar.dtos.OrganizacionDto;
import Esfe.Sgar.dtos.OrganizacionTipoBasura.AsignarTiposBasuraDto;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrganizacionTipoBasuraService {

    private final OrganizacionTipoBasuraRepository repository;

    public OrganizacionTipoBasuraService(OrganizacionTipoBasuraRepository repository) {
        this.repository = repository;
    }

    public String asignarTipoBasura(AsignarTiposBasuraDto dto) {
        List<OrganizacionTipoBasura> existentes = repository.findByIdOrganizacion(dto.getIdOrganizacion());
        boolean yaAsignado = existentes.stream()
                .anyMatch(otb -> otb.getIdTipoClasificacionBasura().equals(dto.getIdTipoClasificacionBasura()));
        if (yaAsignado) {
            return "La organización ya tiene asignada esta clasificación de basura";
        }
        OrganizacionTipoBasura otb = new OrganizacionTipoBasura();
        otb.setIdOrganizacion(dto.getIdOrganizacion());
        otb.setIdTipoClasificacionBasura(dto.getIdTipoClasificacionBasura());
        repository.save(otb);
        return "Asignación exitosa";
    }

    public List<Integer> obtenerOrganizacionesPorTipoBasura(Integer idTipoClasificacionBasura) {
        List<OrganizacionTipoBasura> registros = repository.findByIdTipoClasificacionBasura(idTipoClasificacionBasura);
        return registros.stream()
                .map(OrganizacionTipoBasura::getIdOrganizacion)
                .collect(Collectors.toList());
    }
    

    public List<OrganizacionDto> obtenerOrganizacionesPorTipoBasuraConNombre(Integer idTipoClasificacionBasura,
            String token) {
        List<OrganizacionTipoBasura> asignaciones = repository
                .findByIdTipoClasificacionBasura(idTipoClasificacionBasura);
        List<Integer> ids = asignaciones.stream()
                .map(OrganizacionTipoBasura::getIdOrganizacion)
                .distinct()
                .collect(Collectors.toList());

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<OrganizacionResponse> response = restTemplate.exchange(
                "https://sgarseguridad.somee.com/api/organization/list",
                HttpMethod.GET,
                entity,
                OrganizacionResponse.class);

        List<OrganizacionDto> resultado = new ArrayList<>();
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            for (OrganizacionDto org : response.getBody().getItems()) {
                if (ids.contains(org.getId())) {
                    resultado.add(org);
                }
            }
        }
        return resultado;
    }

}