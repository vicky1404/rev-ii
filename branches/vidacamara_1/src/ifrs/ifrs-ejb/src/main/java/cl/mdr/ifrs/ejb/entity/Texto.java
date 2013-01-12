package cl.mdr.ifrs.ejb.entity;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import cl.mdr.ifrs.ejb.common.Constantes;


@Entity
@NamedQueries( { @NamedQuery(name = "Texto.findAll", query = "select o from Texto o") })
@Table(name = Constantes.TEXTO)
public class Texto implements Serializable {
    private static final long serialVersionUID = -7602242346615456161L;
    
    @Id
    @Column(name = "ID_TEXTO", nullable = false)
    private Long idTexto;
    
    @Column(length = 4000)
    private String texto;
    
    @Column(name = "NEGRITA")
    private boolean negrita;
    
    @OneToOne(targetEntity = Estructura.class)
    @JoinColumn(name = "ID_TEXTO", insertable = false, updatable = false)
    private Estructura estructura;

    public Texto() {
    }

    public Texto(Estructura estructura, Long idTexto, String texto) {
        this.estructura = estructura;
        this.idTexto = idTexto;
        this.texto = texto;
    }


    public Long getIdTexto() {
        return idTexto;
    }

    public void setIdTexto(Long idTexto) {
        this.idTexto = idTexto;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Estructura getEstructura() {
        return estructura;
    }

    public void setEstructura(Estructura estructura) {
        this.estructura = estructura;
    }

    public void setNegrita(boolean negrita) {
        this.negrita = negrita;
    }

    public boolean isNegrita() {
        return negrita;
    }
    
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()+"@"+Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append("idTexto=");
        buffer.append(getIdTexto());
        buffer.append(',');
        buffer.append("texto=");
        buffer.append(getTexto());
        buffer.append(']');
        return buffer.toString();
    }
}
