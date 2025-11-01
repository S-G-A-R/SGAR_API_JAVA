package Esfe.Sgar.modelos;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ReferentesSupervisores")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReferenteSupervisor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdSupervisor", nullable = false)
    private Supervisor supervisor;

    @Column(name = "Nombre", length = 120, nullable = false)
    private String nombre;

    @Column(name = "Parentesco", length = 30)
    private String parentesco;

    @Column(name = "Tipo", nullable = false)
    private Byte tipo;
}
