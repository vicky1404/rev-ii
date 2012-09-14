package xbrlcore.junit;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import java.text.MessageFormat;

import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import xbrlcore.constants.GeneralConstants;

import xbrlcore.linkbase.LabelLinkbase;
import xbrlcore.linkbase.PresentationLinkbase;
import xbrlcore.linkbase.PresentationLinkbaseElement;

import xbrlcore.taxonomy.Concept;
import xbrlcore.taxonomy.DiscoverableTaxonomySet;
import xbrlcore.taxonomy.sax.SAXBuilder;

public class LoadSVSTaxonomy {
	
	@SuppressWarnings("unused")
	@Test
        
	public void  getDTS() throws SAXException, IOException, ParserConfigurationException {
		SAXBuilder saxBuilder = new SAXBuilder();
		try{			 
			//DiscoverableTaxonomySet taxonomySet = saxBuilder.build(new InputSource("file://EQ13797/taxonomias/2012-01-02/taxonomia-svs/eeff/cl-cs_shell_2012-01-02.xsd"));
			//DiscoverableTaxonomySet taxonomySet = saxBuilder.build(new InputSource("xbrl/test/svs/eeff-y-notas/cl-cs_shell_2012-07-17.xsd"));
			DiscoverableTaxonomySet taxonomySet = saxBuilder.build(new InputSource("file://eq13830/taxonomias/2012-07-17/taxonomia-svs/eeff-y-notas/cl-cs_shell_2012-07-17.xsd"));             
                        System.err.println("Nombre de la Taxonomia :"+taxonomySet.getTopTaxonomy().getName());				
			System.err.println("Total de conceptos "+taxonomySet.getConcepts().size());
			/*for(Concept concept : taxonomySet.getConcepts()){
				System.err.println("=====================================\n"+
								   "ID: "+concept.getId()+"\n"+
								   "Name: "+concept.getName()+"\n"+
								   "Type: "+concept.getType()+"\n"+
								   "Namespace: "+concept.getNamespace().getPrefix()+"\n"+
								   "Substitution Group: "+concept.getSubstitutionGroup()+"\n"+
								   "Period Type: "+concept.getPeriodType()+"\n"+
								   "Schema Name: "+concept.getTaxonomySchemaName()+"\n"+
								   "=====================================\n");
			}*/
                //taxonomySet.getPresentationLinkbase().buildPresentationLinkbase();	
                /*for(String taxonomyName : taxonomySet.getTopTaxonomy().getImportedTaxonomyNames()){
                    System.err.println(taxonomyName);
                    for(PresentationLinkbaseElement linkbaseElement : taxonomySet.getPresentationLinkbase().getPresentationList(taxonomyName)){
                        System.err.println(linkbaseElement.getConcept().getId());
                    }
                }*/
                                                        
                LabelLinkbase labelLinkbase = taxonomySet.getLabelLinkbase();                      
                for(Map.Entry<String, List<PresentationLinkbaseElement>> entry : taxonomySet.getPresentationLinkbase().getLinkRoleToElementList().entrySet()) {
                    System.out.println(entry.getKey());
                    for(PresentationLinkbaseElement presentationLinkbaseElement : entry.getValue()){                        
                        System.err.println(presentationLinkbaseElement.getConcept());
                        System.err.println(labelLinkbase.getLabel(presentationLinkbaseElement.getConcept(), "http://www.xbrl.org/2003/role/label"));
                        System.err.println(labelLinkbase.getLabel(presentationLinkbaseElement.getConcept(), "http://www.xbrl.org/2003/role/documentation"));                        
                        System.err.println(presentationLinkbaseElement.getLevel());
                    }
                }
                  
                          
                        	
			
			
			

		}catch (Exception e) {
			e.printStackTrace();
		}
		

	}

}
