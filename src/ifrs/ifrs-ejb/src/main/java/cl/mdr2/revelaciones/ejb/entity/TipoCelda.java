package cl.bicevida.revelaciones.ejb.entity;


import cl.bicevida.revelaciones.ejb.common.Constantes;

import java.io.Serializable;

import java.lang.Long;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@NamedQueries( { @NamedQuery(name = "TipoCelda.findAll", query = "select o from TipoCelda o order by o.nombre asc") })
@Table(name = Constantes.TIPO_CELDA)
public class TipoCelda implements Serializable {
    
    public static final String QUERY_FIND_CELDA_ALL = "TipoCelda.findAll";
    public static final Long TIPO_CELDA_TEXTO = 0L;
    public static final Long TIPO_CELDA_NUMERO = 1L;
        
    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 7453861536937128669L;
    @Id
    @Column(name = "ID_TIPO_CELDA", nullable = false)
    private Long idTipoCelda;
    @Column(nullable = false, length = 128)
    private String nombre;
    @OneToMany(mappedBy = "tipoCelda")
    private List<Celda> celdaList;

    public TipoCelda() {
    }
    
    
    public TipoCelda(Long idTipoCelda, String nombre) {
        this.idTipoCelda = idTipoCelda;
        this.nombre = nombre;
    }

    public TipoCelda(Long idTipoCelda) {        
        this.idTipoCelda = idTipoCelda;
    }


    public Long getIdTipoCelda() {
        return idTipoCelda;
    }

    public void setIdTipoCelda(Long idTipoCelda) {
        this.idTipoCelda = idTipoCelda;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Celda> getCeldaList() {
        return celdaList;
    }

    public void setCeldaList(List<Celda> celdaNotaList) {
        this.celdaList = celdaNotaList;
    }

    public Celda addCeldaNota(Celda celdaNota) {
        getCeldaList().add(celdaNota);
        celdaNota.setTipoCelda(this);
        return celdaNota;
    }

    public Celda removeCeldaNota(Celda celdaNota) {
        getCeldaList().remove(celdaNota);
        celdaNota.setTipoCelda(null);
        return celdaNota;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
                return true;
        if (obj == null)
                return false;
        if (getClass() != obj.getClass())
                return false;
        final TipoCelda other = (TipoCelda) obj;
        if (idTipoCelda == null) {
            if (other.idTipoCelda != null)
                    return false;
        } else if (!idTipoCelda.equals(other.idTipoCelda))
                return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("idTipoCelda=");
        buffer.append(getIdTipoCelda());
        buffer.append(',');
        buffer.append("nombre=");
        buffer.append(getNombre());
        buffer.append(']');
        return buffer.toString();
    }
}
