package cl.mdr.exfida.xbrl.ejb.service;


import static cl.mdr.ifrs.ejb.cross.Constantes.PERSISTENCE_UNIT_NAME;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import cl.mdr.exfida.xbrl.ejb.service.local.XbrlInstanceGeneratorService;
import cl.mdr.exfida.xbrl.ejb.service.local.XbrlInstanceGeneratorServiceLocal;
import cl.mdr.exfida.xbrl.ejb.entity.XbrlTaxonomia;

import xbrlcore.instance.Instance;

import xbrlcore.taxonomy.DiscoverableTaxonomySet;
import xbrlcore.taxonomy.sax.SAXBuilder;


@Stateless(name = "XbrlInstanceGeneratorService", mappedName = "AppRevelaciones-branch-6-Model-XbrlInstanceGeneratorService")
public class XbrlInstanceGeneratorServiceBean implements XbrlInstanceGeneratorServiceLocal, XbrlInstanceGeneratorService {

    @Resource
    SessionContext sessionContext;

    @PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    private EntityManager em;

    private Instance instance;


    public XbrlInstanceGeneratorServiceBean() {
        super();
    }
    
    public void setUpXbrlInstancia(final XbrlTaxonomia XbrlTaxonomia) throws ParserConfigurationException,
                                                                             SAXException {
        SAXBuilder saxBuilder = new SAXBuilder();                      
        Set<DiscoverableTaxonomySet> taxonomySets = new HashSet<DiscoverableTaxonomySet>();
        
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void generarInstancia() {
        System.out.println("Hola mundo");
    }


    public void setInstance(Instance instance) {
        this.instance = instance;
    }

    public Instance getInstance() {
        return instance;
    }
}
