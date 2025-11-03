package Esfe.Sgar.modelos;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ReferentesOperadores")

public class ReferenteOperador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /*@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdOperador", nullable = false)
    private Operador operador;*/

    @Column(name = "Nombre", length = 120, nullable = false)
    private String nombre;

    @Column(name = "Parentesco", length = 50)
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
