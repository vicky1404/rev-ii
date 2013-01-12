package cl.mdr.ifrs.ejb.entity;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.google.gson.annotations.Expose;

import cl.mdr.ifrs.ejb.common.Constantes;


@Entity
@NamedQueries( { @NamedQuery(name = "TipoDato.findAll", query = "select o from TipoDato o order by o.nombre asc") })
@Table(name = Constantes.TIPO_DATO)
public class TipoDato implements Serializable {
	private static final long serialVersionUID = 4108510817096911983L;
	public static final String QUERY_FIND_TIPO_DATO_ALL = "TipoDato.findAll";
    public static final Long TIPO_DATO_TEXTO = 0L;
    public static final Long TIPO_DATO_FECHA = 1L;
    public static final Long TIPO_DATO_ENTERO = 2L;
    public static final Long TIPO_DATO_DECIMAL = 3L;
    
    @Id
    @Column(name = "ID_TIPO_DATO", nullable = false)
    @Expose
    private Long idTipoDato;
    @Column(nullable = false, length = 64)
    @Expose
    private String nombre;
    @Column(name = "NOMBRE_CLASE", length = 256)
    @Expose
    private String nombreClase;
//    @OneToMany(mappedBy = "tipoDato")
//    private List<Celda> celdaList;

    public TipoDato() {
    }

    public TipoDato(Long idTipoDato, String nombre, String nombreClase) {
        this.idTipoDato = idTipoDato;
        this.nombre = nombre;
        this.nombreClase = nombreClase;
    }

    public TipoDato(Long idTipoDato) {
        this.idTipoDato = idTipoDato;
    }


    public Long getIdTipoDato() {
        return idTipoDato;
    }

    public void setIdTipoDato(Long idTipoDato) {
        this.idTipoDato = idTipoDato;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreClase() {
        return nombreClase;
    }

    public void setNombreClase(String nombreClase) {
        this.nombreClase = nombreClase;
    }
//        
    @Override
    public boolean equals(final Object obj) {
            if (this == obj)
                    return true;
            if (obj == null)
                    return false;
            if (getClass() != obj.getClass())
                    return false;
            final TipoDato other = (TipoDato) obj;
            if (idTipoDato == null) {
                if (other.idTipoDato != null)
                        return false;
            } else if (!idTipoDato.equals(other.idTipoDato))
                    return false;
            return true;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("idTipoDato=");
        buffer.append(getIdTipoDato());
        buffer.append(',');
        buffer.append("nombre=");
        buffer.append(getNombre());
        buffer.append(',');
        buffer.append("nombreClase=");
        buffer.append(getNombreClase());
        buffer.append(']');
        return buffer.toString();
    }
}
