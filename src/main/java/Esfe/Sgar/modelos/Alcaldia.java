package Esfe.Sgar.modelos;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Alcaldias")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alcaldia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdMunicipio", nullable = false)
    private Municipio municipio;

    @Column(name = "Correo", length = 255, nullable = false, unique = true)
    private String correo;

    @Column(name = "Password", length = 64, nullable = false)
    private String password;

    @OneToMany(mappedBy = "alcaldia", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Zona> zonas;

    @OneToMany(mappedBy = "alcaldia", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Supervisor> supervisores;

    @OneToMany(mappedBy = "alcaldia", cascade = CascadeType.ALL)
    private List<Operador> operadores;
}
