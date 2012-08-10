package cl.bicevida.xbrl.ejb.service;

import static cl.bicevida.revelaciones.ejb.cross.Constantes.PERSISTENCE_UNIT_NAME;
import cl.bicevida.xbrl.ejb.entity.XbrlTaxonomia;
import cl.bicevida.xbrl.ejb.service.local.TaxonomyLoaderServiceLocal;

import java.io.IOException;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import javax.persistence.Query;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import xbrlcore.taxonomy.DiscoverableTaxonomySet;
import xbrlcore.taxonomy.sax.SAXBuilder;

@Stateless
public class TaxonomyLoaderServiceBean implements TaxonomyLoaderServiceLocal {
    
    @PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    private EntityManager em;
    
    
    private SAXBuilder saxBuilder = null;

    public TaxonomyLoaderServiceBean() {
        super();
    }

    /**
     * @param vigente
     * @return
     * @throws Exception
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<XbrlTaxonomia> findTaxonomiasByFiltro(final Date fechaDesde, final Date fechaHasta, final Long vigente) throws Exception{        
            StringBuffer jpql = new StringBuffer();
            jpql.append(" select o from XbrlTaxonomia o ");
            jpql.append(" where 1 = 1 ");
             if(vigente != null){
                jpql.append(" and (o.vigente = :vigente) ");
             }
             if(fechaDesde != null && fechaHasta != null){
                jpql.append(" and (o.fechaCreacion between :fechaDesde and :fechaHasta )");
             }                    
            jpql.append("order by o.fechaCreacion desc");
            Query query = em.createQuery(jpql.toString());
            if(vigente != null){
                query.setParameter("vigente", vigente);
            }
            if(fechaDesde != null && fechaHasta != null){
                query.setParameter("fechaDesde", fechaDesde);
                query.setParameter("fechaHasta", fechaHasta);
            }             
            return query.getResultList();
    }

    /**
     * @param taxonomias
     * @throws Exception
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void mergeTaxonomias(final List<XbrlTaxonomia> taxonomias, final String usuario) throws Exception{
        for(XbrlTaxonomia taxonomia : taxonomias){            
            taxonomia.setFechaEdicion(new Date());             
            taxonomia.setUsuarioEdicion(usuario);
            em.merge(taxonomia);
        }
    }
        
    /**
     * Carga desde un path absoluto un DiscoverableTaxonomySet
     * objeto que contiene la taxonomia referenciada.
     * @param systemId
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */   
     @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public DiscoverableTaxonomySet loadDiscoverableTaxonomySet(final String systemId) throws ParserConfigurationException, SAXException, IOException {
        saxBuilder = new SAXBuilder();
        return saxBuilder.build(new InputSource(systemId));         
    }
    
    
}
