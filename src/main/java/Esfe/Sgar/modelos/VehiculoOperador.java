package Esfe.Sgar.modelos;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name = "vehiculo_operador")
public class VehiculoOperador {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(length = 10, nullable = false)
    private String placa;
    
    @Column(nullable = false)
    private Short anio;
    
    @Column(length = 50, nullable = false)
    private String color;
    
    @Column(nullable = false)
    private LocalDate fechaAsignacion;
    
    @Column(nullable = false)
    private Boolean estado;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operador_id", nullable = false)
    private Operador operador;
    
   /*@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modelo_id", nullable = false)
    private Modelo modelo;
    */

   
}