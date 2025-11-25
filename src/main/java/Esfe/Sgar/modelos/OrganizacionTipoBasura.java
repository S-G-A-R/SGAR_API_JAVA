package Esfe.Sgar.modelos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "organizacion_tipo_basura")
public class OrganizacionTipoBasura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer idOrganizacion;
    private Integer idTipoClasificacionBasura;
}