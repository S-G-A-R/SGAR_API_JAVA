package Esfe.Sgar.modelos;

import jakarta.persistence.*;

@Entity
@Table(name = "Vehiculos")

public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdMarca", nullable = false)
    private Marca marca;

    @Column(name = "Placa", length = 20, nullable = false, unique = true)
    private String placa;

    @Column(name = "Codigo", length = 20, nullable = false, unique = true)
    private String codigo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdTipoVehiculo", nullable = false)
    private TipoVehiculo tipoVehiculo;

    @Column(name = "Mecanico", length = 120)
    private String mecanico;

    @Column(name = "Taller", length = 120)
    private String taller;

   
    @Column(name = "OperadorId")
    private Integer operadorId;

    @Column(name = "Estado")
    private Byte estado;

    @Column(name = "Descripcion", length = 500)
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdFoto")
    private Foto foto;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public TipoVehiculo getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setTipoVehiculo(TipoVehiculo tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    public String getMecanico() {
        return mecanico;
    }

    public void setMecanico(String mecanico) {
        this.mecanico = mecanico;
    }

    public String getTaller() {
        return taller;
    }

    public void setTaller(String taller) {
        this.taller = taller;
    }

    public Byte getEstado() {
        return estado;
    }

    public void setEstado(Byte estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Foto getFoto() {
        return foto;
    }

    public void setFoto(Foto foto) {
        this.foto = foto;
    }

        public Integer getOperadorId() {
            return operadorId;
        }

        public void setOperadorId(Integer operadorId) {
            this.operadorId = operadorId;
        }

    
}
