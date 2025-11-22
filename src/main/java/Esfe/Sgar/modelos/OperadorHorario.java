package Esfe.Sgar.modelos;

import jakarta.persistence.*;

@Entity
@Table(name = "OperadorHorario")
public class OperadorHorario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "OperadorId", nullable = false)
    private Integer operadorId; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HorarioId", nullable = false)
    private Horario horario;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getOperadorId() { return operadorId; }
    public void setOperadorId(Integer operadorId) { this.operadorId = operadorId; }

    public Horario getHorario() { return horario; }
    public void setHorario(Horario horario) { this.horario = horario; }
}
