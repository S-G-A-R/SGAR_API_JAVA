package Esfe.Sgar.modelos;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ReferentesOperadores")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReferenteOperador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdOperador", nullable = false)
    private Operador operador;

    @Column(name = "Nombre", length = 120, nullable = false)
    private String nombre;

    @Column(name = "Parentesco", length = 50)
    private String parentesco;

    @Column(name = "Tipo", nullable = false)
    private Byte tipo;
}
