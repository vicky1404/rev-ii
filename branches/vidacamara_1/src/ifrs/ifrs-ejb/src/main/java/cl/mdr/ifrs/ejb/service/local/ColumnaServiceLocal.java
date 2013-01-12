package cl.mdr.ifrs.ejb.service.local;


import javax.ejb.Local;

import cl.mdr.ifrs.ejb.entity.Columna;

@Local
public interface ColumnaServiceLocal {

    Object mergeEntity(Columna entity);

    Object persistEntity(Columna entity);
}
