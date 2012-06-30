package cl.mdr.ifrs.ejb.entity;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import cl.mdr.ifrs.ejb.common.Constantes;


@Entity
@NamedQueries( { 
					@NamedQuery(name = TipoCuadro.FIND_ALL, query = "select o from TipoCuadro o order by o.idTipoCuadro asc"), 
					@NamedQuery(name = TipoCuadro.FIND_BY_FILTRO, query = "select o from TipoCuadro o where (:nombre = null or lower(o.nombre) like :nombre) and (:titulo = null or lower(o.titulo) like :titulo) order by o.idTipoCuadro desc")

})
@Table(name = Constantes.TIPO_CUADRO)
public class TipoCuadro implements Serializable {
    private static final long serialVersionUID = -960337295716773306L;
    
    public static final String FIND_ALL = "TipoCuadro.findAll";
    public static final String FIND_BY_FILTRO = "TipoCuadro.findByFiltro";
    
    @Id
    @GeneratedValue(generator="ID_GEN_TIPO_CUADRO")
    @SequenceGenerator(name="ID_GEN_TIPO_CUADRO", sequenceName = "SEQ_TIPO_CUADRO" ,allocationSize = 1)
    @Column(name = "ID_TIPO_CUADRO", nullable = false)
    private Long idTipoCuadro;
    @Column(nullable = false, length = 256)
    private String nombre;
    @Column(length = 512)
    private String titulo;

    public TipoCuadro() {
    }

    public TipoCuadro(Long idTipoCuadro, String nombre, String titulo) {
        this.idTipoCuadro = idTipoCuadro;
        this.nombre = nombre;
        this.titulo = titulo;
    }

    public Long getIdTipoCuadro() {
        return idTipoCuadro;
    }

    public void setIdTipoCuadro(Long idTipoCuadro) {
        this.idTipoCuadro = idTipoCuadro;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof TipoCuadro)) {
            return false;
        }
        final TipoCuadro other = (TipoCuadro)object;
        if (!(idTipoCuadro == null ? other.idTipoCuadro == null : idTipoCuadro.equals(other.idTipoCuadro))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int PRIME = 37;
        int result = 1;
        result = PRIME * result + ((idTipoCuadro == null) ? 0 : idTipoCuadro.hashCode());
        return result;
    }
}
