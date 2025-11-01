package Esfe.Sgar.modelos;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Municipios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Municipio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "Nombre", length = 80, nullable = false)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdDepartamento", nullable = false)
    private Departamento departamento;

    @OneToMany(mappedBy = "municipio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Distrito> distritos;

    @OneToMany(mappedBy = "municipio", cascade = CascadeType.ALL)
    private List<Alcaldia> alcaldias;
}
