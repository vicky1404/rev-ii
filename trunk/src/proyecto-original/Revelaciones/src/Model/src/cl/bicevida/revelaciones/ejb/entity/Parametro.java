package cl.bicevida.revelaciones.ejb.entity;

import cl.bicevida.revelaciones.ejb.entity.pk.ParametroPK;

import java.io.Serializable;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries( { @NamedQuery(name = Parametro.FIND_ALL, query = "select o from Parametro o"),
                 @NamedQuery(name = Parametro.FIND_BY_TIPO_PARAMETRO, query = "select o from Parametro o where o.idTipoParametro = :idTipoParametro")})
@Table(name = "REV_PARAMETRO")
@IdClass(ParametroPK.class)
public class Parametro implements Serializable {
    
    public final static String FIND_ALL = "Parametro.findAll";
    public final static String FIND_BY_TIPO_PARAMETRO = "Parametro.findByTipoParametro";
    
    @Id
    @Column(name = "ID_PARAMETRO", nullable = false, length = 64)
    private String idParametro;
    
    @Id
    @Column(name = "ID_TIPO_PARAMETRO", nullable = false, insertable = false, updatable = false)
    private Long idTipoParametro;
    
    @Column(nullable = false, length = 512)
    private String valor;
    
    @ManyToOne
    @JoinColumn(name = "ID_TIPO_PARAMETRO")
    private TipoParametro tipoParametro;

    public Parametro() {
    }

    public Parametro(String idParametro, TipoParametro tipoParametro, String valor) {
        this.idParametro = idParametro;
        this.tipoParametro = tipoParametro;
        this.valor = valor;
    }

    public String getIdParametro() {
        return idParametro;
    }

    public void setIdParametro(String idParametro) {
        this.idParametro = idParametro;
    }

    public Long getIdTipoParametro() {
        return idTipoParametro;
    }

    public void setIdTipoParametro(Long idTipoParametro) {
        this.idTipoParametro = idTipoParametro;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public TipoParametro getTipoParametro() {
        return tipoParametro;
    }

    public void setTipoParametro(TipoParametro tipoParametro) {
        this.tipoParametro = tipoParametro;
        if (tipoParametro != null) {
            this.idTipoParametro = tipoParametro.getIdTipoParametro();
        }
    }
}
