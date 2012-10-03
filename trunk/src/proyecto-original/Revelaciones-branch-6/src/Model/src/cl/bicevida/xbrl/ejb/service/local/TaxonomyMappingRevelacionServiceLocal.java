package cl.bicevida.xbrl.ejb.service.local;

import cl.bicevida.revelaciones.ejb.entity.Celda;
import cl.bicevida.revelaciones.ejb.entity.Estructura;
import cl.bicevida.xbrl.ejb.entity.XbrlConceptoCelda;

import cl.bicevida.xbrl.ejb.entity.XbrlTaxonomia;

import java.util.List;

import java.util.Map;

import javax.ejb.Local;

import xbrlcore.taxonomy.Concept;

@Local
public interface TaxonomyMappingRevelacionServiceLocal {

    /**
     * @param mapping
     * @throws Exception
     */
    void persistMappingTaxonomiaRevelacion(XbrlTaxonomia xbrlTaxonomia, Map<Concept, Map<Celda, Boolean>> mapping) throws Exception;

    /**
     * @param xbrlTaxonomia
     * @param concept
     */
    void deleteMappingByConceptoAndTaxonomia(XbrlTaxonomia xbrlTaxonomia, Concept concept);

    /**
     * @param estructura
     * @return
     * @throws Exception
     */
    List<XbrlConceptoCelda> findMappingByEstructura(Estructura estructura) throws Exception;

    /**
     * @param estructura
     * @return
     * @throws Exception
     */
    Map<Concept, Map<Celda, Boolean>> buildMappingByEstructura(Estructura estructura , List<Concept> conceptList) throws Exception;
}
