package Esfe.Sgar.modelos;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Distritos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Distrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "Nombre", length = 80, nullable = false)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdMunicipio", nullable = false)
    private Municipio municipio;

    @OneToMany(mappedBy = "distrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Zona> zonas;
}
