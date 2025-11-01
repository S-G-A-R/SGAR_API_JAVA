package Esfe.Sgar.modelos;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Supervisores")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Supervisor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "Nombre", length = 120, nullable = false)
    private String nombre;

    @Column(name = "Apellido", length = 120, nullable = false)
    private String apellido;

    @Column(name = "Telefono", length = 9, unique = true)
    private String telefono;

    @Column(name = "CorreoPersonal", length = 255, unique = true)
    private String correoPersonal;

    @Column(name = "DUI", length = 10, unique = true, nullable = false)
    private String dui;

    @Lob
    @Column(name = "Foto")
    private byte[] foto;

    @Column(name = "Codigo", length = 20, unique = true, nullable = false)
    private String codigo;

    @Column(name = "CorreoLaboral", length = 255, unique = true, nullable = false)
    private String correoLaboral;

    @Column(name = "TelefonoLaboral", length = 9, unique = true, nullable = false)
    private String telefonoLaboral;

    @Column(name = "Password", length = 64, nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdAlcaldia", nullable = false)
    private Alcaldia alcaldia;

    @OneToMany(mappedBy = "supervisor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReferenteSupervisor> referentes;
}
