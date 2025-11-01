package Esfe.Sgar.modelos;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(name = "Horarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Horario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "HoraEntrada", nullable = false)
    private LocalTime horaEntrada;

    @Column(name = "HoraSalida", nullable = false)
    private LocalTime horaSalida;

    @Column(name = "Dia", length = 7, nullable = false)
    private String dia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdOperador", nullable = false)
    private Operador operador;

    @Column(name = "Turno", nullable = false)
    private Byte turno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdZona", nullable = false)
    private Zona zona;
}
