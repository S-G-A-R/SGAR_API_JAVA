package Esfe.Sgar.modelos;

import jakarta.persistence.*;

@Entity
@Table(name = "Quejas")
public class Queja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "Titulo", length = 80, nullable = false)
    private String titulo;

    @Column(name = "Descripcion", columnDefinition = "TEXT", length = 250)
    private String descripcion;
   
    @Column(name = "CiudadanoId", nullable = false)
    private Integer ciudadano;
     

    @Lob
    @Column(name = "Archivo")
    private byte[] archivo;

    @Column(name = "TipoSituacion", length = 20, nullable = false)
    private String tipoSituacion;

    @Column(name = "Estado")
    private Byte estado;

    @Column(name = "Motivo", length = 255)
    private String motivo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public byte[] getArchivo() {
        return archivo;
    }

    public void setArchivo(byte[] archivo) {
        this.archivo = archivo;
    }

    public String getTipoSituacion() {
        return tipoSituacion;
    }

    public void setTipoSituacion(String tipoSituacion) {
        this.tipoSituacion = tipoSituacion;
    }

    public Byte getEstado() {
        return estado;
    }

    public void setEstado(Byte estado) {
        this.estado = estado;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
    
}
