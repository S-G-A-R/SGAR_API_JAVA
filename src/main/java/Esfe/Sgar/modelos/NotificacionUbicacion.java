package Esfe.Sgar.modelos;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "NotificacionesUbicaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificacionUbicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdCiudadano", nullable = false)
    private Ciudadano ciudadano;

    @Column(name = "DistanciaMetros", nullable = false)
    private Integer distanciaMetros;

    @Column(name = "Latitud", precision = 18, scale = 15, nullable = false)
    private BigDecimal latitud;

    @Column(name = "Longitud", precision = 18, scale = 15, nullable = false)
    private BigDecimal longitud;

    @Column(name = "Titulo", length = 60, nullable = false)
    private String titulo;

    @Column(name = "Estado", nullable = false)
    private Byte estado;
}
