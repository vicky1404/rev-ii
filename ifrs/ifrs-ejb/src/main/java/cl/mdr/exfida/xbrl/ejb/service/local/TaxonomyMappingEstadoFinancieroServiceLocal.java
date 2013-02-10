package cl.mdr.exfida.xbrl.ejb.service.local;

import cl.mdr.ifrs.ejb.entity.EstadoFinanciero;
import cl.mdr.exfida.xbrl.ejb.entity.XbrlTaxonomia;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import xbrlcore.taxonomy.Concept;

@Local
public interface TaxonomyMappingEstadoFinancieroServiceLocal {
           
    void persistMappingTaxonomiaEstadoFinanciero(XbrlTaxonomia xbrlTaxonomia, Map<EstadoFinanciero, List<Concept>> mappingEstadoFinanciero) throws Exception;
    
    void deleteMappingByConceptoAndTaxonomia(XbrlTaxonomia xbrlTaxonomia, Concept concept) throws Exception;
    
    Map<EstadoFinanciero, List<Concept>> buildMappingEstadoFinanciero(XbrlTaxonomia xbrlTaxonomia, List<Concept> conceptList, List<EstadoFinanciero> estadoFinancieroList) throws Exception;
}
