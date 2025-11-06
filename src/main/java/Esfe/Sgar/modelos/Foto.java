package Esfe.Sgar.modelos;

import jakarta.persistence.*;

@Entity
@Table(name = "Fotos")
public class Foto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Lob
    @Column(name = "Imagen", columnDefinition = "LONGBLOB", nullable = false)
    private byte[] imagen;

    @Column(name = "TipoMime", length = 100)
    private String tipoMime;

    @Column(name = "Tamano")
    private Long tamano;

    

    // Getters y Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String getTipoMime() {
        return tipoMime;
    }

    public void setTipoMime(String tipoMime) {
        this.tipoMime = tipoMime;
    }

    public Long getTamano() {
        return tamano;
    }

    public void setTamano(Long tamano) {
        this.tamano = tamano;
    }

  
}
