package Esfe.Sgar.modelos;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Ciudadanos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ciudadano {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "Nombre", length = 120, nullable = false)
    private String nombre;

    @Column(name = "Apellido", length = 120)
    private String apellido;

    @Column(name = "DUI", length = 10, nullable = false, unique = true)
    private String dui;

    @Column(name = "Correo", length = 255, nullable = false, unique = true)
    private String correo;

    @Column(name = "Password", length = 64, nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ZonaId", nullable = false)
    private Zona zona;
}
