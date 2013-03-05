package cl.bicevida.revelaciones.ejb.entity;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@NamedQueries( { @NamedQuery(name = TipoParametro.FIND_ALL, query = "select o from TipoParametro o") })
@Table(name = "REV_TIPO_PARAMETRO")
public class TipoParametro implements Serializable {
    
    public final static String FIND_ALL = "TipoParametro.findAll";
    
    @Column(nullable = false, length = 512)
    private String descripcion;
    @Id
    @Column(name = "ID_TIPO_PARAMETRO", nullable = false)
    private Long idTipoParametro;
    @OneToMany(mappedBy = "tipoParametro")
    private List<Parametro> parametroList;

    public TipoParametro() {
    }

    public TipoParametro(Long idTipoParametro) {
        this.idTipoParametro = idTipoParametro;
    }
    
    public TipoParametro(String descripcion, Long idTipoParametro) {
        this.descripcion = descripcion;
        this.idTipoParametro = idTipoParametro;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getIdTipoParametro() {
        return idTipoParametro;
    }

    public void setIdTipoParametro(Long idTipoParametro) {
        this.idTipoParametro = idTipoParametro;
    }

    public List<Parametro> getParametroList() {
        return parametroList;
    }

    public void setParametroList(List<Parametro> parametroList) {
        this.parametroList = parametroList;
    }

    public Parametro addParametro(Parametro parametro) {
        getParametroList().add(parametro);
        parametro.setTipoParametro(this);
        return parametro;
    }

    public Parametro removeParametro(Parametro parametro) {
        getParametroList().remove(parametro);
        parametro.setTipoParametro(null);
        return parametro;
    }
}
