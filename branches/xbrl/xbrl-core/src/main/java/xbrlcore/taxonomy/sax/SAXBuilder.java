package xbrlcore.taxonomy.sax;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import xbrlcore.constants.GeneralConstants;
import xbrlcore.exception.XBRLException;
import xbrlcore.linkbase.CalculationLinkbase;
import xbrlcore.linkbase.DefinitionLinkbase;
import xbrlcore.linkbase.LabelLinkbase;
import xbrlcore.linkbase.PresentationLinkbase;
import xbrlcore.linkbase.ReferenceLinkbase;
import xbrlcore.taxonomy.DiscoverableTaxonomySet;
import xbrlcore.taxonomy.RoleType;
import xbrlcore.taxonomy.TaxonomySchema;
import xbrlcore.taxonomy.sax.XBRLSchemaContentHandler.LinkBaseInfo;

/**
 * @author d2504hd
 * 
 * Daniel Hamm
 */
public class SAXBuilder {

	private DiscoverableTaxonomySet dts = null;

	private SAXParserFactory saxParserFactory = null;

	private SAXParser saxParser = null;

	private XMLReader xmlReader = null;

	private XBRLSchemaContentHandler xbrlSchemaContentHandler;

	private XBRLLinkbaseContentHandler xbrlLinkbaseContentHandler;

	private URI baseDir;

	private Set<String> alreadyParsedNamespaces;

	private Map<String, LinkBaseInfo> linkbaseFiles;

	private boolean topTaxonomy;

	private Map<String, String> fixedSchemaFiles;

	/**
	 * TODO: Konstruktor sollte keine Exceptions werfen!
	 * 
	 * @throws ParserConfigurationException 
	 * @throws SAXException 
	 */
	public SAXBuilder() throws ParserConfigurationException, SAXException {
		saxParserFactory = SAXParserFactory.newInstance();
		saxParserFactory.setNamespaceAware(true);
		saxParser = saxParserFactory.newSAXParser();

		fixedSchemaFiles = new HashMap<String, String>();
		fixedSchemaFiles.put(
				"http://www.xbrl.org/2003/xbrl-instance-2003-12-31.xsd",
				"xbrl-instance-2003-12-31.xsd");
		fixedSchemaFiles.put("http://www.xbrl.org/2005/xbrldt-2005.xsd",
				"xbrldt-2005.xsd");
		fixedSchemaFiles.put("http://www.xbrl.org/2003/xbrl-linkbase-2003-12-31.xsd", "xbrl-linkbase-2003-12-31.xsd");
		fixedSchemaFiles.put("http://www.xbrl.org/2003/2005-11-07/xbrl-linkbase-2003-12-31.xsd", "xbrl-linkbase-2003-12-31.xsd");
		
		 			

		
	}

	public DiscoverableTaxonomySet build(InputSource source)
			throws IOException, SAXException, XBRLException {
		topTaxonomy = true;
		xbrlSchemaContentHandler = new XBRLSchemaContentHandler();
		xbrlLinkbaseContentHandler = new XBRLLinkbaseContentHandler();

		baseDir = null;
		alreadyParsedNamespaces = null;
		linkbaseFiles = new HashMap<String, LinkBaseInfo>();

		if (baseDir == null) {
			baseDir = URI.create(source.getSystemId().substring(0,
					source.getSystemId().lastIndexOf("/") + 1));
		}

		xmlReader = saxParser.getXMLReader();

		/* parse schemas */
		dts = new DiscoverableTaxonomySet();
		dts = parseSchema(source);

		/* parse linkbases */
		xbrlLinkbaseContentHandler.setDTS(dts);
		
		for (String fileName : linkbaseFiles.keySet()) {
			LinkBaseInfo linkBaseInfo = linkbaseFiles.get(fileName);
			String role = linkBaseInfo.getRole();
			parseLinkbases(new InputSource(fileName), role, linkBaseInfo.getTaxonomy());
		}

		if (dts.getPresentationLinkbase() != null) {
			dts.getPresentationLinkbase().buildLinkbase();
		}
		if (dts.getDefinitionLinkbase() != null) {
			dts.getDefinitionLinkbase().buildLinkbase();
		}
		
		
		return dts;

	}

	private void parseLinkbases(InputSource source, String role, TaxonomySchema taxonomySchema)
			throws SAXException, IOException {
		xbrlLinkbaseContentHandler.getRolesType().clear();
		
		/** Label Linkbase */
		if (role.equals(GeneralConstants.XBRL_LINKBASE_ROLE_LABEL)) {
			LabelLinkbase labelLinkbase = dts.getLabelLinkbase();
			if (labelLinkbase == null) {
				/**
				 * TODO: Das sollte ge�ndert werden! --> Verweis von dts auf
				 * linkbase und zur�ck ?!?
				 */
				labelLinkbase = new LabelLinkbase(dts);
				dts.setLabelLinkbase(labelLinkbase);
			}
			xbrlLinkbaseContentHandler.setLinkbase(labelLinkbase);

			xmlReader.setContentHandler(xbrlLinkbaseContentHandler);
			xmlReader.parse(source);

		}
		/** Presentation Linkbase */
		else if (role
				.equals(GeneralConstants.XBRL_LINKBASE_ROLE_PRESENTATION)) {
			PresentationLinkbase presentationLinkbase = dts
					.getPresentationLinkbase();
			if (presentationLinkbase == null) {
				presentationLinkbase = new PresentationLinkbase(dts);
				dts.setPresentationLinkbase(presentationLinkbase);
			}
			xbrlLinkbaseContentHandler.setLinkbase(presentationLinkbase);

			xmlReader.setContentHandler(xbrlLinkbaseContentHandler);
			xmlReader.parse(source);
			
			Map<String, String> rolesType = xbrlLinkbaseContentHandler.getRolesType();
			
			Set<Entry<String, String>> entrySet = rolesType.entrySet();
			for (Entry<String, String> entry : entrySet) {
				InputSource inputSource = new InputSource((source.getSystemId().substring(0, source.getSystemId().lastIndexOf("/") + 1) + entry.getValue()));
				XBRLRoleTypeContentHandler xbrlRoleTypeContentHandler = new XBRLRoleTypeContentHandler(entry.getKey());
				xmlReader.setContentHandler(xbrlRoleTypeContentHandler);
				xmlReader.parse(inputSource);
				RoleType roleType = xbrlRoleTypeContentHandler.getRoleType();
				if(roleType != null){
					taxonomySchema.addRoleType(roleType);
					
				}
				
			}
			rolesType.clear();
			
			
		}
		/** Definition Linkbase */
		else if (role
				.equals(GeneralConstants.XBRL_LINKBASE_ROLE_DEFINITION)) {
			DefinitionLinkbase definitionLinkbase = dts.getDefinitionLinkbase();
			if (definitionLinkbase == null) {
				definitionLinkbase = new DefinitionLinkbase(dts);
				dts.setDefinitionLinkbase(definitionLinkbase);
			}
			xbrlLinkbaseContentHandler.setLinkbase(definitionLinkbase);

			xmlReader.setContentHandler(xbrlLinkbaseContentHandler);
			xmlReader.parse(source);
		}
		/** Calculation Linkbase */
        else if (role
                .equals(GeneralConstants.XBRL_LINKBASE_ROLE_CALCULATION)) {
            CalculationLinkbase calculationLinkbase = dts
                    .getCalculationLinkbase();
            if (calculationLinkbase == null) {
                calculationLinkbase = new CalculationLinkbase(dts);
                dts.setCalculationLinkbase(calculationLinkbase);
            }
            xbrlLinkbaseContentHandler.setLinkbase(calculationLinkbase);

            xmlReader.setContentHandler(xbrlLinkbaseContentHandler);
            xmlReader.parse(source);
        }
        /** Reference Linkbase */
		else if (role
				.equals(GeneralConstants.XBRL_LINKBASE_ROLE_REFERENCE)) {
			ReferenceLinkbase referenceLinkbase = dts
					.getReferenceLinkbase();
			if (referenceLinkbase == null) {
			    referenceLinkbase = new ReferenceLinkbase(dts);
				dts.setReferenceLinkbase(referenceLinkbase);
			}
			xbrlLinkbaseContentHandler.setLinkbase(referenceLinkbase);

			xmlReader.setContentHandler(xbrlLinkbaseContentHandler);
			xmlReader.parse(source);
		}

	}

	private DiscoverableTaxonomySet parseSchema(InputSource source)
			throws SAXException, IOException {
		URI schemaDir = URI.create(source.getSystemId().substring(0,
				source.getSystemId().lastIndexOf("/") + 1));

		/*
		 * TODO: That is just a work-around. Think about how to implement it
		 * correctly with name and location; especially when creating the
		 * references in instance documents
		 */
		String name = source.getSystemId().substring(
				source.getSystemId().lastIndexOf('/') + 1,
				source.getSystemId().length());

        TaxonomySchema newSchema = new TaxonomySchema(dts, name);
        if (topTaxonomy) {
            dts.setTopTaxonomy(newSchema);
            topTaxonomy = false;
        }

		dts.addTaxonomy(newSchema);
		xbrlSchemaContentHandler.setTaxonomySchema(newSchema);

		xmlReader.setContentHandler(xbrlSchemaContentHandler);
		xmlReader.parse(source);

		/* get imported schemas */
		Map<String, String> importedSchemaFiles = xbrlSchemaContentHandler
				.getImportedSchemaFiles();
		newSchema.setImportedTaxonomyNames(new HashSet<String>(importedSchemaFiles
				.values()));

		/* remove all schema files that have already been parsed */
		if (alreadyParsedNamespaces == null) {
			alreadyParsedNamespaces = new HashSet<String>(importedSchemaFiles.keySet());
		} else {
		    for (String ns : alreadyParsedNamespaces) {
				importedSchemaFiles.remove(ns);
			}
			alreadyParsedNamespaces.addAll(importedSchemaFiles.keySet());
		}

		/* get linkbase files */
		Map<String, LinkBaseInfo> tmpLinkbaseFiles = xbrlSchemaContentHandler.getLinkbaseFiles();
		for (String fileName : tmpLinkbaseFiles.keySet()) {
			LinkBaseInfo linkBaseInfo = tmpLinkbaseFiles.get(fileName);
			linkbaseFiles.put(schemaDir.toString() + fileName, linkBaseInfo);
		}
		
		

		/* parse imported schemas */
		for (String currSchemaLocation : importedSchemaFiles.values()) {
			URI uri = URI.create(currSchemaLocation);

			if (uri.isAbsolute()) {
			    for (String key : fixedSchemaFiles.keySet()) {
					String value = fixedSchemaFiles.get(key);
					if (uri.toString().equals(key)) {
						parseSchema(new InputSource(getClass().getResource("/schemaFiles/" + value).toString()));
					}
				}
			}

			else {
				parseSchema(new InputSource( (source.getSystemId().substring(0, source.getSystemId().lastIndexOf("/") + 1) + currSchemaLocation)));
			}

		}
		return dts;
	}
}