package Esfe.Sgar.modelos;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Operadores")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Operador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "Nombre", length = 120, nullable = false)
    private String nombre;

    @Column(name = "Apellido", length = 120)
    private String apellido;

    @Column(name = "TelefonoPersonal", length = 9, unique = true)
    private String telefonoPersonal;

    @Column(name = "CorreoPersonal", length = 255, unique = true)
    private String correoPersonal;

    @Column(name = "DUI", length = 10, nullable = false, unique = true)
    private String dui;

    @Lob
    @Column(name = "Foto")
    private byte[] foto;

    @Column(name = "Ayudantes", length = 500)
    private String ayudantes;

    @Column(name = "CodigoOperador", length = 20, nullable = false, unique = true)
    private String codigoOperador;

    @Column(name = "TelefonoLaboral", length = 9, nullable = false, unique = true)
    private String telefonoLaboral;

    @Column(name = "CorreoLaboral", length = 255, nullable = false, unique = true)
    private String correoLaboral;

    // VehiculoId optional (OneToOne)
    @OneToOne
    @JoinColumn(name = "VehiculoId")
    private Vehiculo vehiculo;

    @Lob
    @Column(name = "LicenciaDoc", nullable = false)
    private byte[] licenciaDoc;

    @Lob
    @Column(name = "AntecedentesDoc", nullable = false)
    private byte[] antecedentesDoc;

    @Lob
    @Column(name = "SolvenciaDoc", nullable = false)
    private byte[] solvenciaDoc;

    @Column(name = "Password", length = 64, nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdAlcaldia", nullable = false)
    private Alcaldia alcaldia;

    @OneToMany(mappedBy = "operador", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Horario> horarios;

    @OneToMany(mappedBy = "operador", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ubicacion> ubicaciones;

    @OneToMany(mappedBy = "operador", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReferenteOperador> referentes;

    @OneToMany(mappedBy = "operador", cascade = CascadeType.ALL)
    private List<Mantenimiento> mantenimientos;
}
