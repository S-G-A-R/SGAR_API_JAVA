package Esfe.Sgar.modelos;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "TiposVehiculos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoVehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "Tipo", nullable = false)
    private Byte tipo;

    @Column(name = "Descripcion", length = 200, nullable = false)
    private String descripcion;

    @OneToMany(mappedBy = "tipoVehiculo", cascade = CascadeType.ALL)
    private List<Vehiculo> vehiculos;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Byte getTipo() {
        return tipo;
    }
    public void setTipo(Byte tipo) {
        this.tipo = tipo;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public List<Vehiculo> getVehiculos() {
        return vehiculos;
    }
    public void setVehiculos(List<Vehiculo> vehiculos) {
        this.vehiculos = vehiculos;
    }
    
}
