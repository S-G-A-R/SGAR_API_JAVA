package Esfe.Sgar.modelos;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Marcas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Marca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "Nombre", length = 100, nullable = false)
    private String nombre;

    @Column(name = "Modelo", length = 150, nullable = false)
    private String modelo;

    @Column(name = "YearOfFabrication", length = 4, nullable = false)
    private String yearOfFabrication;

    @OneToMany(mappedBy = "marca", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vehiculo> vehiculos;
}
