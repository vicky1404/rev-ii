package cl.bicevida.xbrl.ejb.service.local;

import cl.bicevida.xbrl.ejb.entity.XbrlConceptoCelda;

import java.util.List;

import javax.ejb.Local;

@Local
public interface TaxonomyMappingServiceLocal {
    
    void persistMappingTaxonomiaRevelacion(final List<XbrlConceptoCelda> mappingList) throws Exception;
}
