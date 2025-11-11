package Esfe.Sgar.modelos;
import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "Horarios")
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

    @Column(name = "IdOrganizacion", nullable = false)
    private Integer IdOrganizacion;

    @Column(name = "Turno", nullable = false)
    private Byte turno;

    @Column(name = "ZonaId", nullable = false)
    private String zonaId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalTime getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(LocalTime horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public LocalTime getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(LocalTime horaSalida) {
        this.horaSalida = horaSalida;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public Byte getTurno() {
        return turno;
    }

    public void setTurno(Byte turno) {
        this.turno = turno;
    }

   
    public String getZonaId() {
        return zonaId;
    }
    public void setZonaId(String zonaId) {
        this.zonaId = zonaId;
    }

	public Integer getIdOrganizacion() {
		return IdOrganizacion;
	}

	public void setIdOrganizacion(Integer idOrganizacion) {
		IdOrganizacion = idOrganizacion;
	}
}
