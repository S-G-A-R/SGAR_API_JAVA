package Esfe.Sgar.repositorios;

import Esfe.Sgar.modelos.OrganizacionTipoBasura;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrganizacionTipoBasuraRepository extends JpaRepository<OrganizacionTipoBasura, Long> {
    List<OrganizacionTipoBasura> findByIdTipoClasificacionBasura(Integer idTipoClasificacionBasura);
    List<OrganizacionTipoBasura> findByIdOrganizacion(Integer idOrganizacion);
}