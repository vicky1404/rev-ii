package xbrlcore.taxonomy.sax;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import xbrlcore.taxonomy.RoleType;

public class XBRLRoleTypeContentHandler implements ContentHandler {

	private String id;
	private RoleType roleType;
	private boolean definition;
	private boolean usedOn;
	private StringBuilder builder = new StringBuilder();
	private boolean end = true;

	public XBRLRoleTypeContentHandler(String id) {
		this.id = id;
	}

	@Override
	public void characters(char[] chars, int i, int i1) throws SAXException {
		
		if(end){
			return;
		}
		
		if(definition || usedOn){
			builder.append(new String(chars, i, i1));
		}
		
		
		
	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void endElement(String arg0, String localName, String arg2) throws SAXException {
		if(end){
			return;
		}
		
		if(localName.equals("roleType")){
			end = true;
		}
		
		if(localName.equals("definition")){
			definition = false;
			String[] definition2 = roleType.getDefinition();
			List<String> list = new ArrayList<String>();
			if(definition2 != null){
				list.addAll(Arrays.asList(definition2));
			}
			list.add(builder.toString());
			roleType.setDefinition(list.toArray(new String[list.size()]));
			builder = new StringBuilder();
			
		}else if(localName.equals("usedOn")){
			usedOn = false;
			
			String[] definition2 = roleType.getUsedOn();
			List<String> list = new ArrayList<String>();
			if(definition2 != null){
				list.addAll(Arrays.asList(definition2));
			}
			list.add(builder.toString());
			roleType.setUsedOn(list.toArray(new String[list.size()]));
			builder = new StringBuilder();
		}
	}

	@Override
	public void endPrefixMapping(String arg0) throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void ignorableWhitespace(char[] arg0, int arg1, int arg2) throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void processingInstruction(String arg0, String arg1) throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDocumentLocator(Locator arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void skippedEntity(String arg0) throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startDocument() throws SAXException {

	}

	@Override
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		
				
		if(localName.equals("roleType")){
			String idXML = atts.getValue("id");
			if(idXML.equals(id)){
				roleType = new RoleType(id, atts.getValue("roleURI"));
				end = false;
			}
		}else if(localName.equals("definition") && !end){
			definition = true;
		}else if(localName.equals("usedOn")  && !end){
			usedOn = true;
		}
		
		
		
		
	}

	@Override
	public void startPrefixMapping(String arg0, String arg1) throws SAXException {
		// TODO Auto-generated method stub

	}

	public RoleType getRoleType() {
		return roleType;
	}

	public void setRoleType(RoleType roleType) {
		this.roleType = roleType;
	}

}
