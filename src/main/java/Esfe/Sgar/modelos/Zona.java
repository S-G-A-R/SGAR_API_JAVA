package Esfe.Sgar.modelos;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Zonas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Zona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "Nombre", length = 80, nullable = false)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdDistrito", nullable = false)
    private Distrito distrito;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdAlcaldia", nullable = false)
    private Alcaldia alcaldia;

    @Column(name = "Descripcion", length = 200)
    private String descripcion;

    @OneToMany(mappedBy = "zona", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ciudadano> ciudadanos;

    @OneToMany(mappedBy = "zona", cascade = CascadeType.ALL)
    private List<Horario> horarios;
}
