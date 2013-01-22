package xbrlcore.junit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Set;

import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.junit.BeforeClass;
import org.junit.Test;

import xbrlcore.instance.Fact;
import xbrlcore.instance.Instance;
import xbrlcore.instance.InstanceFactory;
import xbrlcore.instance.InstanceOutputter;
import xbrlcore.instance.InstanceValidator;
import xbrlcore.taxonomy.Concept;
import xbrlcore.taxonomy.DiscoverableTaxonomySet;

public class XBRLValidationTest {
	
	private static String PATH = "xbrl/test/svs/eeff/";
	private static Instance instance;
	
	@BeforeClass
	public static void setUp(){
		InstanceFactory instanceFactory = InstanceFactory.get();
		 try {
			instance = instanceFactory.createInstance(new File(PATH+ "96656410_201203.xbrl"));
		} catch (Exception e) {			
			e.printStackTrace();
		} 
	}
	
	@Test
	public void readInstancia(){
		Set<DiscoverableTaxonomySet> taxonomySet = instance.getDiscoverableTaxonomySet();
		for(DiscoverableTaxonomySet set : taxonomySet){			
			for(Concept concept : set.getConcepts()){
				System.out.println(concept.getId());
			}
			
		}
	}
	
	@Test
	public void readFacts(){
		 Set<Fact> factSet = instance.getFactSet();		 
         Iterator<Fact> factSetIterator = factSet.iterator();
         while (factSetIterator.hasNext()) {
             Fact currFact = factSetIterator.next();             
             System.out.println(MessageFormat.format("{0}  -  {1}  -  {2}", currFact.getConcept().getType(), currFact.getConcept().getId(), currFact.getValue()));             
         }
		
	}
	
	@Test
	public void validateInstanciaTest(){
		InstanceValidator validator = new InstanceValidator(instance);
		try {
			validator.validate();
		} catch (Exception e) {			
			e.printStackTrace();
		} 
	}
	
	@Test
	public void schemaValidationTest(){
		InstanceValidator validator = new InstanceValidator(instance);
		try {
			validator.schemaValidation();
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
