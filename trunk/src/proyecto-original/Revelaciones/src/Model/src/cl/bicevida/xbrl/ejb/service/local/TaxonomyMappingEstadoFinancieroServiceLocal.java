package cl.bicevida.xbrl.ejb.service.local;

import cl.bicevida.revelaciones.ejb.entity.EstadoFinanciero;
import cl.bicevida.xbrl.ejb.entity.XbrlTaxonomia;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import xbrlcore.taxonomy.Concept;

@Local
public interface TaxonomyMappingEstadoFinancieroServiceLocal {
           
    void persistMappingTaxonomiaEstadoFinanciero(XbrlTaxonomia xbrlTaxonomia, Map<Concept, Map<EstadoFinanciero, Boolean>> mappingEstadoFinanciero) throws Exception;
    
    void deleteMappingByConceptoAndTaxonomia(XbrlTaxonomia xbrlTaxonomia, Concept concept) throws Exception;
    
    Map<String, Long[]> getRangoCodigoFecuMap() throws Exception;
    
    Map<Concept, Map<EstadoFinanciero, Boolean>> buildMappingEstadoFinanciero(XbrlTaxonomia xbrlTaxonomia, List<Concept> conceptList, List<EstadoFinanciero> estadoFinancieroList) throws Exception;
}
