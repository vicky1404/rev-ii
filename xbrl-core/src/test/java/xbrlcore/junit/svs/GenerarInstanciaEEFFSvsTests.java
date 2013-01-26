package xbrlcore.junit.svs;

import java.nio.charset.Charset;
import java.util.HashSet;

import org.apache.log4j.Logger;
import org.junit.Test;

import xbrlcore.instance.Fact;
import xbrlcore.instance.Instance;
import xbrlcore.instance.InstanceContext;
import xbrlcore.instance.InstanceOutputter;
import xbrlcore.instance.SchemaRefMapping;
import xbrlcore.taxonomy.Concept;
import xbrlcore.taxonomy.DiscoverableTaxonomySet;

public class GenerarInstanciaEEFFSvsTests extends AbstractSVSTests {

	private static final Logger LOGGER = Logger.getLogger(GenerarInstanciaEEFFSvsTests.class);

	@Test
	public void testGenerarInformacionAdicional110000() throws Exception {

		HashSet<DiscoverableTaxonomySet> hashSet = new HashSet<DiscoverableTaxonomySet>();
		hashSet.add(dts);
		Instance instance = new Instance(hashSet);

		InstanceContext trimestreAcumulado = newContext("TrimestreAcumuladoActual");
		trimestreAcumulado.setPeriodStartDate("2012-01-01");
		trimestreAcumulado.setPeriodEndDate("2012-09-30");
		instance.addContext(trimestreAcumulado);

		{
			Concept rutEntidad = dts.getConceptByID("cl-cs_RUTEntidadInforma");
			Fact fact = new Fact(rutEntidad, "123456-0");
			fact.setInstanceContext(trimestreAcumulado);
			instance.addFact(fact);
		}

		{
			Concept concept = dts.getConceptByID("cl-cs_RazonSocialEntidadQueInforma");
			Fact fact = new Fact(concept, "Compañia de Seguros");
			fact.setInstanceContext(trimestreAcumulado);
			instance.addFact(fact);
		}
		
		{
			Concept concept = dts.getConceptByID("cl-cs_GrupoAsegurador");
			Fact fact = new Fact(concept, "2");
			fact.setInstanceContext(trimestreAcumulado);
			instance.addFact(fact);
		}
		
		{
			Concept concept = dts.getConceptByID("cl-cs_TipoEstadoFinanciero");
			Fact fact = new Fact(concept, "I");
			fact.setInstanceContext(trimestreAcumulado);
			instance.addFact(fact);
		}
		
		{
			Concept concept = dts.getConceptByID("cl-cs_FechaCierrePeriodoQueInforma");
			Fact fact = new Fact(concept, "2012-09-30");
			fact.setInstanceContext(trimestreAcumulado);
			instance.addFact(fact);
		}
		
		{
			Concept concept = dts.getConceptByID("cl-cs_DescripcionMonedaPresentacion");
			Fact fact = new Fact(concept, "PESOS");
			fact.setInstanceContext(trimestreAcumulado);
			instance.addFact(fact);
		}
		
		

		printInstance(instance);

	}

	@Test
	public void testGenerarEstadoCambioPatrimonio60000() throws Exception {
		HashSet<DiscoverableTaxonomySet> hashSet = new HashSet<DiscoverableTaxonomySet>();
		hashSet.add(dts);
		Instance instance = new Instance(hashSet);

		
		
		

		printInstance(instance);

	}
	
	private void printInstance(Instance instance) {
		InstanceOutputter instanceOutputter = new InstanceOutputter(instance);
		instanceOutputter.setSchemaRefMapping(new SchemaRefMapping() {
			@Override
			public String getSchemaRefHREF(String taxonomyName) {
				if (taxonomyName.equals("cl-cs_shell_2012-10-05.xsd")) {
					return "http://www.svs.cl/cl/fr/cs/2012-10-05/" + taxonomyName;
				}
				return taxonomyName;
			}

		});
		String xmlString = instanceOutputter.getXMLString(Charset.defaultCharset());
		LOGGER.info(xmlString);
	}

	private InstanceContext newContext(String idContext) {
		InstanceContext trimestreAcumulado = new InstanceContext(idContext);
		trimestreAcumulado.setIdentifier("123456-5");
		trimestreAcumulado.setIdentifierScheme("http://www.svs.cl/rut");
		return trimestreAcumulado;
	}

}
