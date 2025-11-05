package Esfe.Sgar.modelos;
import jakarta.persistence.*;


@Entity
@Table(name = "TiposClasificacionBasura")
public class TipoClasificacionBasura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="Nombre",  length = 50, nullable = false)
    private String nombre;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
