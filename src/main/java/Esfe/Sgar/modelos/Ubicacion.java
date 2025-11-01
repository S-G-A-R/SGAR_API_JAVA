package Esfe.Sgar.modelos;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "Ubicaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ubicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdOperador", nullable = false)
    private Operador operador;

    @Column(name = "Latitud", precision = 18, scale = 15, nullable = false)
    private BigDecimal latitud;

    @Column(name = "Longitud", precision = 18, scale = 15, nullable = false)
    private BigDecimal longitud;

    @Column(name = "FechaActualizacion", length = 30, nullable = false)
    private String fechaActualizacion;
}
