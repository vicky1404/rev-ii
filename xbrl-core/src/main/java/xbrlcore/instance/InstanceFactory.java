package xbrlcore.instance;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import xbrlcore.constants.ExceptionConstants;
import xbrlcore.constants.GeneralConstants;
import xbrlcore.constants.NamespaceConstants;
import xbrlcore.dimensions.MultipleDimensionType;
import xbrlcore.dimensions.SingleDimensionType;
import xbrlcore.exception.InstanceException;
import xbrlcore.exception.XBRLException;
import xbrlcore.taxonomy.Concept;
import xbrlcore.taxonomy.DiscoverableTaxonomySet;
import xbrlcore.taxonomy.Namespace;
import xbrlcore.taxonomy.TaxonomySchema;
import xbrlcore.taxonomy.TupleDefinition;
import static xbrlcore.taxonomy.Namespace_.toJDOM;

/**
 * This class is responsible for creating an Instance object of an instance
 * file. <br/>
 * <br/>
 * 
 * @author Daniel Hamm
 * 
 */
public class InstanceFactory {

	private static InstanceFactory xbrlInstanceFactory;

	private Instance instance;

	private Document xmlInstance;

	private Map<String, InstanceContext> contextMap;

	private Map<String, InstanceUnit> unitMap;

	private List<Namespace> schemaRefNamespaces;

	/**
	 * Constructor, private.
	 * 
	 */
	private InstanceFactory() {
		contextMap = new HashMap<String, InstanceContext>();
		unitMap = new HashMap<String, InstanceUnit>();
		schemaRefNamespaces = new ArrayList<Namespace>();
	}

	/**
	 * 
	 * @return the only Instance of InstanceFactory object (singleton).
	 */
	public static synchronized InstanceFactory get() {
		if (xbrlInstanceFactory == null) {
			xbrlInstanceFactory = new InstanceFactory();
		}
		return xbrlInstanceFactory;
	}

	/**
	 * Creates an xbrlcore.instance.Instance object.
	 * 
	 * @param instanceFile
	 *            Instance file.
	 * @return An object of xbrlcore.instance.Instance.
	 * @throws JDOMException
	 * @throws IOException
	 * @throws InstanceException
	 * @throws XBRLException
	 * @throws CloneNotSupportedException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public Instance createInstance(File instanceFile) throws JDOMException, IOException, InstanceException, XBRLException, CloneNotSupportedException,
			SAXException, ParserConfigurationException {
		return createInstance(instanceFile.toURI().toURL());
	}

	/**
	 * Creates an xbrlcore.instance.Instance object.
	 * 
	 * @param instanceFile
	 *            Instance file.
	 * @return An object of xbrlcore.instance.Instance.
	 * @throws JDOMException
	 * @throws IOException
	 * @throws InstanceException
	 * @throws XBRLException
	 * @throws CloneNotSupportedException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public Instance createInstance(URL instanceFile) throws JDOMException, IOException, InstanceException, XBRLException, CloneNotSupportedException,
			SAXException, ParserConfigurationException {

		URL base = null;
		String f = instanceFile.toString(); // instanceFile.getFile();
		int sp = f.lastIndexOf('/');
		if (sp >= 0) {
			base = new URL(f.substring(0, sp + 1));
		}

		// Initialise XML document with SAX of JDOM
		SAXBuilder builder = new SAXBuilder();
		xmlInstance = builder.build(instanceFile);

		/* determine taxonomy names */
		Set<String> taxonomyNameSet = getReferencedSchemaNames(xmlInstance);

		/* now build the taxonomy */
		xbrlcore.taxonomy.sax.SAXBuilder xbrlBuilder = new xbrlcore.taxonomy.sax.SAXBuilder();
		Set<DiscoverableTaxonomySet> dtsSet = new HashSet<DiscoverableTaxonomySet>();
		for (String currTaxonomyName : taxonomyNameSet) {
			DiscoverableTaxonomySet currTaxonomy;
			if (base == null)
				currTaxonomy = xbrlBuilder.build(new InputSource(currTaxonomyName));
			else
				currTaxonomy = xbrlBuilder.build(new InputSource(new URL(base, currTaxonomyName).toString()));
			dtsSet.add(currTaxonomy);
		}

		return getInstance(dtsSet, getNameFromURL(instanceFile));
	}

	/**
	 * Builds an instance.
	 * 
	 * @param dtsSet
	 *            Set of discoverable taxonomy sets this instance refers to.
	 * @return An object of xbrlcore.instance.Instance.
	 * @throws InstanceException
	 */
	private Instance getInstance(Set<DiscoverableTaxonomySet> dtsSet, String fileName) throws InstanceException, CloneNotSupportedException {

		instance = new Instance(dtsSet);
		instance.setFileName(fileName);

		/* set instance namespace */
		setInstanceNamespace();

		/* set additional namespaces of the root element */
		setAdditionalNamespaces(xmlInstance.getRootElement().getAdditionalNamespaces());

		/* set additional namespaces of the schemaRef elements */
		setAdditionalNamespaces(schemaRefNamespaces);

		/* determine the schema location */
		setSchemaLocation();

		/* set context elements */
		setContextElements();

		/* set unit elements */
		setUnitElements();

		/* set facts and tuples */
		setFactsAndTuples();

		return instance;
	}

	/**
	 * Determines which taxonomy an instance refers to.
	 * 
	 * @param currDocument
	 *            Structure of the instance file.
	 * @return Set of names of the taxonomy the instance refers to.
	 */
	@SuppressWarnings("unchecked")
	private Set<String> getReferencedSchemaNames(Document currDocument) {
		Set<String> referencedSchemaNameSet = new HashSet<String>();
		List<Element> elementList = currDocument.getRootElement().getChildren();
		for (Element currElement : elementList) {
			if (currElement.getName().equals("schemaRef")) {
				referencedSchemaNameSet.add(currElement.getAttributeValue("href", toJDOM(NamespaceConstants.XLINK_NAMESPACE)));
				/* set namespaces of schemaRef element */
				schemaRefNamespaces = currElement.getAdditionalNamespaces();
			}
		}
		return referencedSchemaNameSet;
	}

	/**
	 * Sets the namespace of the instance.
	 * 
	 */
	private void setInstanceNamespace() {
		org.jdom.Namespace jdomNS = xmlInstance.getRootElement().getNamespace();
		Namespace instanceNamespace = Namespace.getNamespace(jdomNS.getPrefix(), jdomNS.getURI());
		instance.setInstanceNamespace(instanceNamespace);
	}

	/**
	 * Sets additional namespaces needed in this instance.
	 * 
	 */
	private void setAdditionalNamespaces(Collection<?> additionalNamespaces) {

		for (Object currentNamespace : additionalNamespaces) {
			if(currentNamespace instanceof Namespace){
				addAddtionalNamespace((Namespace) currentNamespace);	
			}else if(currentNamespace instanceof org.jdom.Namespace){
				org.jdom.Namespace namespaceJdom = (org.jdom.Namespace) currentNamespace;
				Namespace namespace = new Namespace(namespaceJdom.getPrefix(), namespaceJdom.getURI());
				addAddtionalNamespace(namespace);
			}
			
			
		}
	}

	private void addAddtionalNamespace(Namespace currentNamespace) {
		if (instance.getNamespace(currentNamespace.getURI()) == null) {
			instance.addNamespace(currentNamespace);
		}
	}

	/**
	 * Sets schema location information defined in this instance.
	 * 
	 * 
	 */
	private void setSchemaLocation() {
		if (xmlInstance.getRootElement().getAttributes().size() > 0
				&& xmlInstance.getRootElement().getAttribute("schemaLocation", toJDOM(instance.getNamespace(NamespaceConstants.XSI_NAMESPACE.getURI()))) != null) {
			String schemaLocationValue = xmlInstance.getRootElement().getAttributeValue("schemaLocation",
					toJDOM(instance.getNamespace(NamespaceConstants.XSI_NAMESPACE.getURI())));
			while (schemaLocationValue.indexOf(" ") > 0) {
				String schemaLocationURI = schemaLocationValue.substring(0, schemaLocationValue.indexOf(" "));
				schemaLocationValue = schemaLocationValue.substring(schemaLocationValue.indexOf(" "), schemaLocationValue.length());
				schemaLocationValue = schemaLocationValue.trim();
				String schemaLocationPrefix = null;
				if (schemaLocationValue.indexOf(" ") > 0) {
					schemaLocationPrefix = schemaLocationValue.substring(0, schemaLocationValue.indexOf(" "));
					schemaLocationValue = schemaLocationValue.substring(schemaLocationValue.indexOf(" "), schemaLocationValue.length());
					schemaLocationValue = schemaLocationValue.trim();
				} else {
					schemaLocationPrefix = schemaLocationValue;
				}
				instance.addSchemaLocation(schemaLocationURI, schemaLocationPrefix);
			}
		}
	}

	public String getLocalValue(String value) {
		if (value == null) {
			return value;
		}
		return value.substring(value.indexOf(":") + 1, value.length());
	}

	public String getValueNamespace(String value) {
		if (value == null) {
			return value;
		}
		return value.substring(0, value.indexOf(":"));
	}

	/**
	 * Sets unit elements defined in this instance.
	 * 
	 * @throws InstanceException
	 */
	private void setUnitElements() throws InstanceException {
		@SuppressWarnings("unchecked")
		List<Element> unitElementList = xmlInstance.getRootElement().getChildren("unit", toJDOM(instance.getInstanceNamespace()));
		for (Element currUnitElement : unitElementList) {
			String id = currUnitElement.getAttributeValue("id");

			if (id == null || id.length() == 0) {
				throw new InstanceException(ExceptionConstants.EX_INSTANCE_CREATION_NOID_UNIT);
			}

			InstanceUnit currUnit = new InstanceUnit(id);
			@SuppressWarnings("unchecked")
			List<Element> measureList = currUnitElement.getChildren("measure", toJDOM(instance.getInstanceNamespace()));
			Iterator<Element> measureListIterator = measureList.iterator();
			while (measureListIterator.hasNext()) {
				Element currMeasureElement = measureListIterator.next();
				if (currUnit.getValue() == null) {
					currUnit.setValue(getLocalValue(currMeasureElement.getValue()));
				} else {
					currUnit.setValue(getLocalValue(currUnit.getValue()) + "*" + getLocalValue(currMeasureElement.getValue()));
				}
				currUnit.setNamespaceURI(instance.getNamespaceURI(getValueNamespace(currMeasureElement.getValue())));
			}
			Element divideElement = currUnitElement.getChild("divide", toJDOM(instance.getInstanceNamespace()));
			if (divideElement != null) {
				Element unitNumeratorElement = divideElement.getChild("unitNumerator", toJDOM(instance.getInstanceNamespace()));
				Element unitDenominatorElement = divideElement.getChild("unitDenominator", toJDOM(instance.getInstanceNamespace()));

				Element unitNumeratorMeasureElement = unitNumeratorElement.getChild("measure", toJDOM(instance.getInstanceNamespace()));
				Element unitDenominatorMeasureElement = unitDenominatorElement.getChild("measure", toJDOM(instance.getInstanceNamespace()));

				currUnit.setValue(getLocalValue(unitNumeratorMeasureElement.getValue()) + "/" + getLocalValue(unitDenominatorMeasureElement.getValue()));
				// TODO: need some refactoring, the namespace should be attached
				// to a measure class inside the unit
				currUnit.setNamespaceURI("");
			}

			unitMap.put(id, currUnit);
		}
	}

	/**
	 * @param name
	 * @return Returns a Concept object to a given name from all the
	 *         Discoverable Taxonomy Sets the instance refers to.
	 */
	private Concept getConceptByName(String name) {
		for (DiscoverableTaxonomySet currDts : instance.getDiscoverableTaxonomySet()) {
			Concept currConcept = currDts.getConceptByName(name);
			if (currConcept != null) {
				return currConcept;
			}
		}
		return null;
	}

	/**
	 * Sets context elements of this instance.
	 * 
	 * @throws InstanceException
	 */
	private void setContextElements() throws InstanceException, CloneNotSupportedException {
		@SuppressWarnings("unchecked")
		List<Element> contextElementList = xmlInstance.getRootElement().getChildren("context", toJDOM(instance.getInstanceNamespace()));
		for (Element currContextElement : contextElementList) {
			String id = currContextElement.getAttributeValue("id");

			if (id == null || id.length() == 0) {
				throw new InstanceException(ExceptionConstants.EX_INSTANCE_CREATION_NOID_CONTEXT);
			}

			InstanceContext currContext = new InstanceContext(id);

			/* set identifier scheme and identifier */
			Element identifierElement = currContextElement.getChild("entity", toJDOM(instance.getInstanceNamespace())).getChild("identifier",
					toJDOM(instance.getInstanceNamespace()));
			currContext.setIdentifierScheme(identifierElement.getAttributeValue("scheme"));
			currContext.setIdentifier(identifierElement.getValue());

			/* set period type and period */
			Element periodElement = currContextElement.getChild("period", toJDOM(instance.getInstanceNamespace()));
			if (periodElement != null) {
				if (periodElement.getChild("startDate", toJDOM(instance.getInstanceNamespace())) != null
						&& periodElement.getChild("endDate", toJDOM(instance.getInstanceNamespace())) != null) {
					currContext.setPeriodStartDate(periodElement.getChild("startDate", toJDOM(instance.getInstanceNamespace())).getText());
					currContext.setPeriodEndDate(periodElement.getChild("endDate", toJDOM(instance.getInstanceNamespace())).getText());
				} else if (periodElement.getChild("instant", toJDOM(instance.getInstanceNamespace())) != null) {
					if (periodElement.getChild("instant", toJDOM(instance.getInstanceNamespace())).getChild("forever", toJDOM(instance.getInstanceNamespace())) != null) {
						currContext.setPeriodValue("forever");
					} else {
						currContext.setPeriodValue(periodElement.getChild("instant", toJDOM(instance.getInstanceNamespace())).getText());
					}
				}
			}

			/*
			 * set multidimensional information - parse both <scenario> and
			 * <segment> element
			 */
			List<Element> scenSegElementList = new ArrayList<Element>();
			Element scenarioElement = currContextElement.getChild("scenario", toJDOM(instance.getInstanceNamespace()));
			/* <segment> is a child element of <entity> */
			Element segmentElement = currContextElement.getChild("entity", toJDOM(instance.getInstanceNamespace())).getChild("segment",
					toJDOM(instance.getInstanceNamespace()));
			if (scenarioElement != null) {
				scenSegElementList.add(scenSegElementList.size(), scenarioElement);
				for (Object elem : scenarioElement.getChildren())
					currContext.addScenarioElement((Element) elem);
			}
			if (segmentElement != null) {
				scenSegElementList.add(scenSegElementList.size(), segmentElement);
				for (Object elem : segmentElement.getChildren())
					currContext.addSegmentElement((Element) elem);
			}

			for (int i = 0; i < scenSegElementList.size(); i++) {
				Element currElement = scenSegElementList.get(i);
				@SuppressWarnings("unchecked")
				List<Element> explicitMemberElementList = currElement.getChildren("explicitMember",
						toJDOM(instance.getNamespace(NamespaceConstants.XBRLDI_NAMESPACE.getURI())));
				@SuppressWarnings("unchecked")
				List<Element> typedMemberElementList = currElement.getChildren("typedMember",
						toJDOM(instance.getNamespace(NamespaceConstants.XBRLDI_NAMESPACE.getURI())));
				Iterator<Element> explicitMemberElementListIterator = explicitMemberElementList.iterator();
				Iterator<Element> typedMemberElementListIterator = typedMemberElementList.iterator();
				MultipleDimensionType mdt = null;
				/* set explicit member */
				while (explicitMemberElementListIterator.hasNext()) {
					Element currExplicitMemberElement = explicitMemberElementListIterator.next();

					/* determine dimension element */
					String dimensionAttribute = currExplicitMemberElement.getAttributeValue("dimension");
					String prefix = dimensionAttribute.substring(0, dimensionAttribute.indexOf(":"));
					String dimensionElementName = dimensionAttribute.substring(dimensionAttribute.indexOf(":") + 1, dimensionAttribute.length());
					org.jdom.Namespace currExplicitMemberNamespace = currExplicitMemberElement.getNamespace(prefix);
					if (instance.getSchemaForURI(Namespace.getNamespace(currExplicitMemberNamespace.getPrefix(), currExplicitMemberNamespace.getURI())) == null) {
						throw new InstanceException(ExceptionConstants.EX_INSTANCE_CREATION_NO_SCHEMA_PREFIX + prefix);
					}
					Concept dimensionElement = instance.getSchemaForURI(
							Namespace.getNamespace(currExplicitMemberNamespace.getPrefix(), currExplicitMemberNamespace.getURI())).getConceptByName(
							dimensionElementName);

					/* determine domain member element */
					String value = currExplicitMemberElement.getValue();
					String domainMemberElementName = value.substring(value.indexOf(":") + 1, value.length());
					Concept domainMemberElement = getConceptByName(domainMemberElementName);

					if (dimensionElement == null || domainMemberElement == null) {
						throw new InstanceException(ExceptionConstants.EX_INSTANCE_CREATION_DIMENSIONS + id);
					}

					if (mdt == null) {
						mdt = new MultipleDimensionType(dimensionElement, domainMemberElement);
					} else {
						mdt.addPredecessorDimensionDomain(new SingleDimensionType(dimensionElement, domainMemberElement));
					}
				}
				/* set typed member */
				while (typedMemberElementListIterator.hasNext()) {
					Element currTypedMemberElement = typedMemberElementListIterator.next();

					/* determine dimension element */
					String dimensionAttribute = currTypedMemberElement.getAttributeValue("dimension");
					String prefix = dimensionAttribute.substring(0, dimensionAttribute.indexOf(":"));
					String dimensionElementName = dimensionAttribute.substring(dimensionAttribute.indexOf(":") + 1, dimensionAttribute.length());
					org.jdom.Namespace currTypedMemberNamespace = currTypedMemberElement.getNamespace(prefix);
					Concept dimensionElement = instance.getSchemaForURI(
							Namespace.getNamespace(currTypedMemberNamespace.getPrefix(), currTypedMemberNamespace.getURI())).getConceptByName(
							dimensionElementName);

					/*
					 * SingleDimensionType represtents the typed dimension
					 * element and its content
					 */
					SingleDimensionType sdt = null;

					/* set typed dimension element */
					if (currTypedMemberElement.getChildren().size() != 0) {
						Element childElement = (Element) currTypedMemberElement.getChildren().get(0);
						sdt = new SingleDimensionType(dimensionElement, childElement);
					}

					if (mdt == null) {
						mdt = new MultipleDimensionType(sdt);
					} else {
						mdt.addPredecessorDimensionDomain(sdt);
					}
				}

				if (mdt != null && currElement.getName().equals("scenario")) {
					currContext.setDimensionalInformation(mdt, GeneralConstants.DIM_SCENARIO);
				} else if (mdt != null && currElement.getName().equals("segment")) {
					currContext.setDimensionalInformation(mdt, GeneralConstants.DIM_SEGMENT);
				}
			}

			contextMap.put(id, currContext);
			instance.addContext(currContext);
		}

	}

	/**
	 * Sets facts of the instance.
	 * 
	 * @throws InstanceException
	 */
	private void setFactsAndTuples() throws InstanceException {
		@SuppressWarnings("unchecked")
		List<Element> factElementList = xmlInstance.getRootElement().getChildren();
		for (Element currFactElement : factElementList) {
			if (!currFactElement.getName().equals("context") && !currFactElement.getName().equals("schemaRef") && !currFactElement.getName().equals("unit")) {

				TaxonomySchema schema = instance.getSchemaForURI(Namespace.getNamespace(currFactElement.getNamespace().getPrefix(), currFactElement
						.getNamespace().getURI()));
				Concept currFactConcept = schema.getConceptByName(currFactElement.getName());

				if (currFactConcept instanceof TupleDefinition) {
					Tuple newTuple = createTuple((TupleDefinition) currFactConcept, currFactElement);
					instance.addTuple(newTuple);
				} else { // is item?
					Fact newFact = createFact(currFactConcept, currFactElement);
					instance.addFact(newFact);
				}
			}
		}
	}

	private Fact createFact(Concept concept, Element element) throws InstanceException {
		if (concept == null) {
			throw new InstanceException(ExceptionConstants.EX_INSTANCE_CREATION_FACT + element.getName());
		}
		// now it is a fact element
		Fact newFact = new Fact(concept);

		// check if it refers to a valid context and unit
		String contextID = element.getAttributeValue("contextRef");
		InstanceContext ctx = contextMap.get(contextID);
		if (ctx == null) {
			throw new InstanceException(ExceptionConstants.EX_INSTANCE_CREATION_NO_CONTEXT + element.getName());
		}

		newFact.setInstanceContext(ctx);
		if (element.getAttributeValue("id") != null) {
			newFact.setID(element.getAttributeValue("id"));
		}

		if (concept.isNumericItem()) {
			String unitID = element.getAttributeValue("unitRef");
			InstanceUnit unit = unitMap.get(unitID);
			newFact.setInstanceUnit(unit);

			// set remaining information
			if (element.getAttributeValue("decimals") != null) {
				newFact.setDecimals(Integer.parseInt(element.getAttributeValue("decimals")));
			}
			if (element.getAttributeValue("precision") != null) {
				newFact.setPrecision(Integer.parseInt(element.getAttributeValue("precision")));
			}
		}
		if (element.getContentSize() == 0) {
			newFact.setValue(null);
		} else {
			newFact.setValue(element.getValue());
		}
		return newFact;
	}

	private Tuple createTuple(TupleDefinition tupleDef, Element element) throws InstanceException {
		if (tupleDef == null) {
			throw new InstanceException(ExceptionConstants.EX_INSTANCE_CREATION_FACT + element.getName());
		}

		// now it is a tuple element
		Tuple tuple = new Tuple(tupleDef);

		// check if it refers to a valid context and unit
		String contextID = element.getAttributeValue("contextRef");
		InstanceContext ctx = contextMap.get(contextID);
		if (ctx == null) {
			throw new InstanceException(ExceptionConstants.EX_INSTANCE_CREATION_NO_CONTEXT + element.getName());
		}

		tuple.setInstanceContext(ctx);

		/*
		 * if (element.getAttributeValue("id") != null) {
		 * tuple.setID(element.getAttributeValue("id")); }
		 */

		@SuppressWarnings("unchecked")
		List<Element> childElementList = element.getChildren();
		for (Element currElement : childElementList) {
			TaxonomySchema schema = instance.getSchemaForURI(Namespace
					.getNamespace(currElement.getNamespace().getPrefix(), currElement.getNamespace().getURI()));
			Concept currConcept = schema.getConceptByName(currElement.getName());

			if (currConcept instanceof TupleDefinition) {
				Tuple newChildTuple = createTuple((TupleDefinition) currConcept, currElement);
				tuple.addSelection(currConcept, newChildTuple);
			} else { // is item?
				Fact newFact = createFact(currConcept, currElement);
				tuple.addSelection(currConcept, newFact);
			}
		}
		return tuple;
	}

	protected static File getFileFromURL(URL url) throws MalformedURLException {
		if (!url.getProtocol().equals("file"))
			throw new IllegalArgumentException("The passed URL does not point to a file.");

		try {
			return new File(url.toURI());
		} catch (URISyntaxException e) {
			throw new MalformedURLException(e.getMessage());
		}
	}

	protected static String getNameFromURL(URL url) {
		String s = url.toString();
		if (s.length() <= 1)
			return s;
		int p = s.lastIndexOf('/');
		if (p == s.length() - 1) {
			int p2 = s.lastIndexOf('/', p - 1);
			if (p2 >= 0) {
				if (p2 == p - 1)
					return "";

				return s.substring(p + 1, p);
			}

			return s.substring(0, p);
		}

		if (p >= 0)
			return s.substring(p + 1);

		return s;
	}
}
