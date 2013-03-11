package cl.mdr.exfida.xbrl.ejb.service.local;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import xbrlcore.taxonomy.Concept;
import cl.mdr.exfida.xbrl.ejb.entity.XbrlConceptoCelda;
import cl.mdr.exfida.xbrl.ejb.entity.XbrlTaxonomia;
import cl.mdr.ifrs.ejb.entity.Celda;
import cl.mdr.ifrs.ejb.entity.Estructura;
import cl.mdr.ifrs.ejb.entity.Html;

@Local
public interface TaxonomyMappingRevelacionServiceLocal {

    /**
     * @param mapping
     * @throws Exception
     */
    void persistMappingTaxonomiaRevelacion(XbrlTaxonomia xbrlTaxonomia, Map<Celda, List<Concept>> mapping) throws Exception;
    
    void persistMappingTaxonomiaRevelacionHTML(XbrlTaxonomia xbrlTaxonomia, List<Concept> mapping, Html html) throws Exception;

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
    Map<Celda, List<Concept>> buildMappingByEstructura(Estructura estructura , List<Concept> conceptList) throws Exception;
    
    List<Concept> buildMappingByEstructuraHTML(Estructura estructura , List<Concept> conceptList) throws Exception;
}
