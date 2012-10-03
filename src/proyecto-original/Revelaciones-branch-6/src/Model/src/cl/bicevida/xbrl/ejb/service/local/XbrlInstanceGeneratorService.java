package cl.bicevida.xbrl.ejb.service.local;

import javax.ejb.Remote;

@Remote
public interface XbrlInstanceGeneratorService {
    void generarInstancia();
}
