package Esfe.Sgar.modelos;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Vehiculos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdMarca", nullable = false)
    private Marca marca;

    @Column(name = "Placa", length = 20, nullable = false, unique = true)
    private String placa;

    @Column(name = "Codigo", length = 20, nullable = false, unique = true)
    private String codigo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdTipoVehiculo", nullable = false)
    private TipoVehiculo tipoVehiculo;

    @Column(name = "Mecanico", length = 120)
    private String mecanico;

    @Column(name = "Taller", length = 120)
    private String taller;

    // IdOperador FK (optional)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdOperador")
    private Operador operador;

    @Column(name = "Estado")
    private Byte estado;

    @Column(name = "Descripcion", length = 500)
    private String descripcion;

    @Lob
    @Column(name = "Foto")
    private byte[] foto;
}
