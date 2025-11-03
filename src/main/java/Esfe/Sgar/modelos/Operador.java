package Esfe.Sgar.modelos;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "operador")
public class Operador {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(length = 100, nullable = false)
    private String nombres;
    
    @Column(length = 100, nullable = false)
    private String apellidos;
    
    @Column(length = 10, nullable = false)
    private String dui;
    
    @Column(length = 100, nullable = false)
    private String direccion;
    
    @Column(length = 9, nullable = false)
    private String telefono;
    
    @Column(nullable = false)
    private LocalDate fechaNacimiento;
    
    @Column(length = 20, nullable = false)
    private String licencia;
    
    @Column(nullable = false)
    private Boolean estado;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "horario_id", nullable = false)
    private Horario horario;
    
    @OneToMany(mappedBy = "operador")
    private List<VehiculoOperador> vehiculos;
    
    @OneToMany(mappedBy = "operador")
    private List<ReferenteOperador> referentes;
}