package cl.bicevida.xbrl.ejb.service.local;

import javax.ejb.Local;

@Local
public interface XbrlInstanceGeneratorServiceLocal {
    
    public void generarInstancia();
}
