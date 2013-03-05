package cl.bicevida.xbrl.ejb.service.local;

import java.io.Serializable;

import javax.ejb.Remote;


@Remote
public interface XbrlInstanceGeneratorServiceLocal extends Serializable {
    
    public void generarInstancia();
}
