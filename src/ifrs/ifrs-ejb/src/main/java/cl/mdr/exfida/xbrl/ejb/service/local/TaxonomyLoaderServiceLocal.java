package cl.mdr.exfida.xbrl.ejb.service.local;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import xbrlcore.taxonomy.DiscoverableTaxonomySet;
import cl.mdr.exfida.xbrl.ejb.entity.XbrlTaxonomia;

@Local
public interface TaxonomyLoaderServiceLocal {

    /**
     * @param vigente
     * @return
     * @throws Exception
     */
    List<XbrlTaxonomia> findTaxonomiasByFiltro(Date fechaDesde, Date fechaHasta, Long vigente)throws Exception ;

    /**
     * @param taxonomias
     * @throws Exception
     */
    void mergeTaxonomias(List<XbrlTaxonomia> taxonomias, String usuario) throws Exception;

    /**
     * @param systemId
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    DiscoverableTaxonomySet loadDiscoverableTaxonomySet(String systemId) throws ParserConfigurationException, SAXException, IOException;
}
