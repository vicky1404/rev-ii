package cl.mdr.exfida.xbrl.ejb.service.local;

import cl.mdr.ifrs.ejb.entity.Celda;
import cl.mdr.ifrs.ejb.entity.Columna;
import cl.mdr.ifrs.ejb.entity.Estructura;
import cl.mdr.exfida.xbrl.ejb.entity.XbrlConceptoCelda;

import cl.mdr.exfida.xbrl.ejb.entity.XbrlTaxonomia;

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
    void persistMappingTaxonomiaRevelacion(XbrlTaxonomia xbrlTaxonomia, Map<Celda, List<Concept>> mapping) throws Exception;
    
    void persistMappingTaxonomiaRevelacionColumnas(XbrlTaxonomia xbrlTaxonomia, Map<Columna, List<Concept>> mapping) throws Exception;

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
    
    Map<Columna, List<Concept>> buildMappingByEstructuraColumna(Estructura estructura , List<Concept> conceptList) throws Exception;
}