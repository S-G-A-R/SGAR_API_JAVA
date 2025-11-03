package Esfe.Sgar.modelos;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ReferentesSupervisores")
public class ReferenteSupervisor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /*@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdSupervisor", nullable = false)
    private Supervisor supervisor;*/

    @Column(name = "Nombre", length = 120, nullable = false)
    private String nombre;

    @Column(name = "Parentesco", length = 30)
    private String parentesco;

    @Column(name = "Tipo", nullable = false)
    private Byte tipo;

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

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    public Byte getTipo() {
        return tipo;
    }

    public void setTipo(Byte tipo) {
        this.tipo = tipo;
    }
    
}
