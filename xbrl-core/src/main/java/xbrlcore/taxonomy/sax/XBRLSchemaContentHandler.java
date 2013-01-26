/*
 * Created on 25.05.2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package xbrlcore.taxonomy.sax;

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import xbrlcore.constants.NamespaceConstants;
import xbrlcore.taxonomy.Concept;
import xbrlcore.taxonomy.Namespace;
import xbrlcore.taxonomy.TaxonomySchema;

/**
 * @author d2504hd
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class XBRLSchemaContentHandler implements ContentHandler {

	private TaxonomySchema taxonomySchema;

	private Map<String, String> namespaceMapping;

	private Map<String, String> importedSchemaFiles;

	private Map<String, LinkBaseInfo> linkbaseFiles;

	public void setTaxonomySchema(TaxonomySchema taxonomySchema) {
		this.taxonomySchema = taxonomySchema;
		this.importedSchemaFiles = new HashMap<String, String>();
		this.linkbaseFiles = new HashMap<String, LinkBaseInfo>();
	}
	
	public static class LinkBaseInfo{
		public LinkBaseInfo(String herf, String role, TaxonomySchema taxonomy) {
			super();
			this.herf = herf;
			this.role = role;
			this.taxonomy = taxonomy;
		}
		private String herf;
		private String role;
		private TaxonomySchema taxonomy;
		public String getHerf() {
			return herf;
		}
		public void setHerf(String herf) {
			this.herf = herf;
		}
		public String getRole() {
			return role;
		}
		public void setRole(String role) {
			this.role = role;
		}
		public TaxonomySchema getTaxonomy() {
			return taxonomy;
		}
		public void setTaxonomy(TaxonomySchema taxonomy) {
			this.taxonomy = taxonomy;
		}
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void startDocument() throws SAXException {
		if (namespaceMapping == null) {
			namespaceMapping = new HashMap<String, String>();
		} else {
			namespaceMapping = new HashMap<String, String>();
		}
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
		// TODO Auto-generated method stub

	}

    /**
     * {@inheritDoc}
     */
	@Override
	public void ignorableWhitespace(char[] arg0, int arg1, int arg2)
			throws SAXException {
		// TODO Auto-generated method stub

	}

    /**
     * {@inheritDoc}
     */
	@Override
	public void endPrefixMapping(String arg0) throws SAXException {
		// TODO Auto-generated method stub

	}

    /**
     * {@inheritDoc}
     */
	@Override
	public void skippedEntity(String arg0) throws SAXException {
		// TODO Auto-generated method stub

	}

    /**
     * {@inheritDoc}
     */
	@Override
	public void setDocumentLocator(Locator arg0) {
		// TODO Auto-generated method stub

	}

    /**
     * {@inheritDoc}
     */
	@Override
	public void processingInstruction(String arg0, String arg1)
			throws SAXException {
		// TODO Auto-generated method stub

	}

    /**
     * {@inheritDoc}
     */
	@Override
	public void startPrefixMapping(String prefix, String url)
			throws SAXException {
		namespaceMapping.put(url, prefix);
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public void endElement(String arg0, String arg1, String arg2)
			throws SAXException {
		// TODO Auto-generated method stub

	}

    /**
     * {@inheritDoc}
     */
	@Override
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		
		
		
		if (localName.equals("schema")) {
			String targetNamespaceURI = atts.getValue("targetNamespace");
			//try to get the prefix from the additionnal namespace instead of forging it
			String targetNamespacePrefix = null;
			/*
			for (int i = 0; i < atts.getLength(); i++){
				if (atts.getQName(i).startsWith("xmlns:")){
					if (atts.getValue(i).equalsIgnoreCase(targetNamespaceURI)){
						targetNamespacePrefix = atts.getQName(i).substring(atts.getQName(i).indexOf(':') + 1);
						break;
					}
				}
			}
			*/
			for (Map.Entry<String, String> pairs : namespaceMapping.entrySet()) {
		        if (pairs.getKey().equalsIgnoreCase(targetNamespaceURI)){
		        	targetNamespacePrefix = pairs.getValue();
		        	break;
		        }
		    }
			//if we did not found the "official" prefix, build one
			if (targetNamespacePrefix == null) 
				targetNamespacePrefix = "ns_"
					+ targetNamespaceURI.substring(targetNamespaceURI
							.lastIndexOf("/") + 1, targetNamespaceURI.length());
			taxonomySchema.setNamespace(Namespace.getNamespace(
					targetNamespacePrefix, targetNamespaceURI));
		} else if (localName.equals("element")
				&& namespaceURI.equals(NamespaceConstants.XSD_NAMESPACE_URI)) {
			buildConcept(namespaceURI, localName, qName, atts);

		} else if (localName.equals("import")
				&& namespaceURI.equals(NamespaceConstants.XSD_NAMESPACE_URI)) {
			importedSchemaFiles.put(atts.getValue("namespace"), atts.getValue("schemaLocation"));
		} else if (localName.equals("linkbaseRef")
				&& namespaceURI.equals(NamespaceConstants.XBRL_SCHEMA_LOC_LINKBASE_URI)) {
			String href = atts.getValue(NamespaceConstants.XBRL_SCHEMA_LOC_XLINK_URI, "href");
			String role = atts.getValue(NamespaceConstants.XBRL_SCHEMA_LOC_XLINK_URI, "role");
			linkbaseFiles.put(href, new LinkBaseInfo(href, role, taxonomySchema));
		}

	}

	/**
	 * 
	 * @param namespaceURI
	 * @param localName
	 * @param qName
	 * @param atts
	 */
    private void buildConcept(String namespaceURI, String localName,
			String qName, Attributes atts) {
    	
    	
		if (atts.getValue("name") == null || atts.getValue("id") == null)
			return;
		Concept concept = new Concept(atts.getValue("name"),
		                              atts.getValue("id"),
		                              atts.getValue("type"),
		                              null,
		                              taxonomySchema,
		                              atts.getValue("substitutionGroup"),
		                              atts.getValue("abstract") != null && atts.getValue("abstract").equals("true"),
		                              atts.getValue("nillable") != null && atts.getValue("nillable").equals("true"),
		                              atts.getValue(namespaceMapping.get(NamespaceConstants.XBRL_SCHEMA_LOC_INSTANCE_URI) + ":periodType"),
		                              atts.getValue(NamespaceConstants.XBRLDT_URI, "typedDomainRef"), toMap(atts)
		                              );
		

		// TODO: This exception must be thrown to the invoking method!
		try {
			taxonomySchema.addConcept(concept);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println(ex.toString());
		}
	}
    
    private Map<String, String> toMap(Attributes attributes){
    	Map<String, String> map = new HashMap<String, String>();
    	int length = attributes.getLength();
    	for (int i = 0; i < length; i++) {
			String localName = attributes.getLocalName(i);
			String qName = attributes.getQName(i);
			String value = attributes.getValue(qName);
			map.put(localName, value);
		}
    	return map;
    }

	

	public Map<String, String> getImportedSchemaFiles() {
		return new HashMap<String, String>(importedSchemaFiles);
	}

	public Map<String, LinkBaseInfo> getLinkbaseFiles() {
		return new HashMap<String, LinkBaseInfo>(linkbaseFiles);
	}
}