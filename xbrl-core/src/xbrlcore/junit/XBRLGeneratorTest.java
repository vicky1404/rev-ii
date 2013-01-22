package xbrlcore.junit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Set;

import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.InputSource;

import xbrlcore.constants.NamespaceConstants;
import xbrlcore.constants.NamespacesTaxonomyEEFF;
import xbrlcore.instance.Fact;
import xbrlcore.instance.Instance;
import xbrlcore.instance.InstanceContext;
import xbrlcore.instance.InstanceOutputter;
import xbrlcore.taxonomy.Concept;
import xbrlcore.taxonomy.DiscoverableTaxonomySet;
import xbrlcore.taxonomy.sax.SAXBuilder;

public class XBRLGeneratorTest {
	
	private static Instance instance;
	
	@BeforeClass
	public static void setUpXbrlInstancia(){			
		 try{
			 final String PATH = "xbrl/test/svs/eeff/";
			 SAXBuilder saxBuilder = new SAXBuilder();			
			 Set<DiscoverableTaxonomySet> taxonomySets = new HashSet<DiscoverableTaxonomySet>();
			 taxonomySets.add(saxBuilder.build(new InputSource(PATH+"cl-cs_shell_2012-01-02.xsd")));
			 taxonomySets.add(saxBuilder.build(new InputSource(PATH+"cl-cs_eeff/cl-cs_eeff_role-110000.xsd")));
			 taxonomySets.add(saxBuilder.build(new InputSource(PATH+"cl-cs_eeff/cl-cs_eeff_role-200000.xsd")));
			 taxonomySets.add(saxBuilder.build(new InputSource(PATH+"cl-cs_eeff/cl-cs_eeff_role-300000.xsd")));
			 taxonomySets.add(saxBuilder.build(new InputSource(PATH+"cl-cs_eeff/cl-cs_eeff_role-500000.xsd")));
			 taxonomySets.add(saxBuilder.build(new InputSource(PATH+"cl-cs_eeff/cl-cs_eeff_role-600000.xsd")));
			 instance = new Instance(taxonomySets);
			 instance.addNamespace(NamespaceConstants.XSD_NAMESPACE);
			 instance.addNamespace(NamespacesTaxonomyEEFF.CL_CS_SHELL_NAMESPACE);
			 instance.addNamespace(NamespaceConstants.XLINK_NAMESPACE);
			 instance.addNamespace(NamespaceConstants.ISO4217_NAMESPACE);
			 instance.addNamespace(NamespaceConstants.XBRLDT_NAMESPACE);
			 instance.addNamespace(NamespaceConstants.XBRLI_NAMESPACE);
			 instance.addNamespace(NamespaceConstants.XBRLDI_NAMESPACE);
			 instance.setComment("Generado por IFRS Team");
			 
			 InstanceContext instanceContext;
			 
			 for(DiscoverableTaxonomySet taxonomySet : taxonomySets){
				 for(Concept concept : taxonomySet.getConcepts()){					 					 
					 if(concept.getId().equals("cl-cs_RazonSocialEntidadQueInforma")){
						 Fact fact = new Fact(concept);
						 fact.setValue("BICE VIDA COMPANIA DE SEGUROS S.A.");
						 instanceContext = new InstanceContext("AcumuladoActual");
						 //instanceContext.setPeriodValue("2012-03-31");
						 instanceContext.setIdentifierScheme("http://www.svs.cl/rut");
						 instanceContext.setIdentifier("96656410-5");
						 instanceContext.setPeriodStartDate("2012-01-01");
						 instanceContext.setPeriodEndDate("2012-03-31");
						 fact.setInstanceContext(instanceContext);
						 instance.addFact(fact);
					 }					 					 
				 }
			 }
			 
		} catch (Exception e) {			
			e.printStackTrace();
		} 
	}
	
	
	 @Test
	 public void printInstance() {
        assertNotNull(instance);
        try {
            InstanceOutputter instanceOutputter = new InstanceOutputter(instance);
            Document instanceXML = instanceOutputter.getXML();

            /* outputting XML */
            XMLOutputter serializer = new XMLOutputter();
            Format f = Format.getPrettyFormat();
            f.setOmitDeclaration(false);
            serializer.setFormat(f);
            serializer.output(instanceXML, System.err);
        } catch (Exception ex) {
            fail(ex.toString());
        }
	 }
	
	

}
