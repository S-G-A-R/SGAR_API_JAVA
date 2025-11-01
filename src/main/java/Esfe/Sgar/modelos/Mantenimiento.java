package Esfe.Sgar.modelos;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Mantenimientos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mantenimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "Titulo", length = 80, nullable = false)
    private String titulo;

    @Column(name = "Descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdOperador", nullable = false)
    private Operador operador;

    @Lob
    @Column(name = "Archivo")
    private byte[] archivo;

    @Column(name = "TipoSituacion", length = 20, nullable = false)
    private String tipoSituacion;

    @Column(name = "Estado")
    private Byte estado;

    @Column(name = "Motivo", length = 255)
    private String motivo;
}
