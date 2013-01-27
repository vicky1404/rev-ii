package xbrlcore.junit.svs;

import java.nio.charset.Charset;
import java.util.HashSet;

import org.apache.log4j.Logger;
import org.junit.Test;

import xbrlcore.constants.GeneralConstants;
import xbrlcore.constants.NamespaceConstants;
import xbrlcore.dimensions.MultipleDimensionType;
import xbrlcore.exception.InstanceException;
import xbrlcore.instance.Fact;
import xbrlcore.instance.Instance;
import xbrlcore.instance.InstanceContext;
import xbrlcore.instance.InstanceOutputter;
import xbrlcore.instance.InstanceUnit;
import xbrlcore.instance.SchemaRefMapping;
import xbrlcore.junit.sax.TestHelper;
import xbrlcore.taxonomy.Concept;
import xbrlcore.taxonomy.DiscoverableTaxonomySet;

public class GenerarInstanciaEEFFSvsTests extends AbstractSVSTests  {

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
		
		DiscoverableTaxonomySet dts2 = TestHelper.getDTS("/xbrl/SVS CL-CS 2012-10-05 Modificada el 2012-11-21/cl-cs_eeff/cl-cs_eeff_role-600000.xsd");
		
		HashSet<DiscoverableTaxonomySet> hashSet = new HashSet<DiscoverableTaxonomySet>();
		hashSet.add(dts2);
		Instance instance = new Instance(hashSet);
		instance.addNamespace(NamespaceConstants.ISO4217_NAMESPACE);

		InstanceUnit unit = new InstanceUnit("CLP");
		unit.setValue("iso4217:CLP");
		unit.setNamespaceURI(NamespaceConstants.ISO4217_NAMESPACE.toString());
		instance.addUnit(unit);

		addCapitalPagado(dts2, instance, unit);
		addOtrasReservas(dts2, instance, unit);
		addOtrosAjustes(dts2, instance, unit);
		addOtrosResultadosConAjustePatrimonio(dts2, instance, unit);
		addReservaAjustePorCalce(dts2, instance, unit);
		addReservaDescalceSeguros(dts2, instance, unit);
		addReservas(dts2, instance, unit);
		addResultadoFlujoCajas(dts2, instance, unit);
		addResultadoPariodo(dts2, instance, unit);
		addResultadoEvaluacion(dts2, instance, unit);
		
		

		

		printInstance(instance);

	}

	private void addResultadoEvaluacion(DiscoverableTaxonomySet dts2, Instance instance, InstanceUnit unit) throws InstanceException {
		InstanceContext context = newContext("SaldoInicio_cl-cs_EstadoCambiosEnPatrimonioTabla_cl-cs_ResultadoEvaluacionPropiedadesMueblesYEquipoMiembro_ACT");

		Concept eje = dts2.getConceptByID("cl-cs_ComponentesDelPatrimonioEje");
		Concept miembro = dts2.getConceptByID("cl-cs_ResultadoEvaluacionPropiedadesMueblesYEquipoMiembro");

		context.setDimensionalInformation(new MultipleDimensionType(eje, miembro), GeneralConstants.DIM_SCENARIO);
		context.setPeriodValue("2011-12-31");

		instance.addContext(context);
		instance.addFact(addNewFact(dts2, unit, context, "cl-cs_Patrimonio", "0"));
	}

	private void addResultadoPariodo(DiscoverableTaxonomySet dts2, Instance instance, InstanceUnit unit) throws InstanceException {
		InstanceContext context = newContext("SaldoInicio_cl-cs_EstadoCambiosEnPatrimonioTabla_cl-cs_ResultadoDelPeriodoMiembro_ACT");

		Concept eje = dts2.getConceptByID("cl-cs_ComponentesDelPatrimonioEje");
		Concept miembro = dts2.getConceptByID("cl-cs_ResultadoDelPeriodoMiembro");

		context.setDimensionalInformation(new MultipleDimensionType(eje, miembro), GeneralConstants.DIM_SCENARIO);
		context.setPeriodValue("2011-12-31");

		instance.addContext(context);
		instance.addFact(addNewFact(dts2, unit, context, "cl-cs_Patrimonio", "17635063000"));
		instance.addFact(addNewFact(dts2, unit, context, "cl-cs_PatrimonioPreviamenteReportado", "17635063000"));
	}

	private void addResultadoFlujoCajas(DiscoverableTaxonomySet dts2, Instance instance, InstanceUnit unit) throws InstanceException {
		InstanceContext context = newContext("SaldoInicio_cl-cs_EstadoCambiosEnPatrimonioTabla_cl-cs_ResultadoCoberturasFlujoCajaMiembro_ACT");

		Concept eje = dts2.getConceptByID("cl-cs_ComponentesDelPatrimonioEje");
		Concept miembro = dts2.getConceptByID("cl-cs_ResultadoCoberturasFlujoCajaMiembro");

		context.setDimensionalInformation(new MultipleDimensionType(eje, miembro), GeneralConstants.DIM_SCENARIO);
		context.setPeriodValue("2011-12-31");

		instance.addContext(context);
		instance.addFact(addNewFact(dts2, unit, context, "cl-cs_Patrimonio", "0"));
	}

	private void addReservas(DiscoverableTaxonomySet dts2, Instance instance, InstanceUnit unit) throws InstanceException {
		InstanceContext context = newContext("SaldoInicio_cl-cs_EstadoCambiosEnPatrimonioTabla_cl-cs_ReservasMiembro_ACT");

		Concept eje = dts2.getConceptByID("cl-cs_ComponentesDelPatrimonioEje");
		Concept miembro = dts2.getConceptByID("cl-cs_ReservasMiembro");

		context.setDimensionalInformation(new MultipleDimensionType(eje, miembro), GeneralConstants.DIM_SCENARIO);
		context.setPeriodValue("2011-12-31");

		instance.addContext(context);
		
		instance.addFact(addNewFact(dts2, unit, context, "cl-cs_AjustesPatrimonioPeriodosAnteriores", "0"));
		
		
		instance.addFact(addNewFact(dts2, unit, context, "cl-cs_Patrimonio", "6429736000"));
		instance.addFact(addNewFact(dts2, unit, context, "cl-cs_PatrimonioPreviamenteReportado", "6429736000"));
	}

	private void addReservaDescalceSeguros(DiscoverableTaxonomySet dts2, Instance instance, InstanceUnit unit) throws InstanceException {
		InstanceContext context = newContext("SaldoInicio_cl-cs_EstadoCambiosEnPatrimonioTabla_cl-cs_ReservaDescalceSegurosCUIMiembro_ACT");

		Concept eje = dts2.getConceptByID("cl-cs_ComponentesDelPatrimonioEje");
		Concept miembro = dts2.getConceptByID("cl-cs_ReservaDescalceSegurosCUIMiembro");

		context.setDimensionalInformation(new MultipleDimensionType(eje, miembro), GeneralConstants.DIM_SCENARIO);
		context.setPeriodValue("2011-12-31");

		instance.addContext(context);
		
		instance.addFact(addNewFact(dts2, unit, context, "cl-cs_Patrimonio", "-132695000"));
		instance.addFact(addNewFact(dts2, unit, context, "cl-cs_PatrimonioPreviamenteReportado", "-132695000"));
	}

	private void addReservaAjustePorCalce(DiscoverableTaxonomySet dts2, Instance instance, InstanceUnit unit) throws InstanceException {
		InstanceContext context = newContext("SaldoInicio_cl-cs_EstadoCambiosEnPatrimonioTabla_cl-cs_ReservaAjustePorCalceMiembro_ACT");

		Concept eje = dts2.getConceptByID("cl-cs_ComponentesDelPatrimonioEje");
		Concept miembro = dts2.getConceptByID("cl-cs_ReservaAjustePorCalceMiembro");

		context.setDimensionalInformation(new MultipleDimensionType(eje, miembro), GeneralConstants.DIM_SCENARIO);
		context.setPeriodValue("2011-12-31");

		instance.addContext(context);
		
		instance.addFact(addNewFact(dts2, unit, context, "cl-cs_Patrimonio", "966050000"));
		instance.addFact(addNewFact(dts2, unit, context, "cl-cs_PatrimonioPreviamenteReportado", "966050000"));
	}

	private void addOtrosResultadosConAjustePatrimonio(DiscoverableTaxonomySet dts2, Instance instance, InstanceUnit unit) throws InstanceException {
		InstanceContext context = newContext("SaldoInicio_cl-cs_EstadoCambiosEnPatrimonioTabla_cl-cs_OtrosResultadosConAjusteEnPatrimonioMiembro_ACT");

		Concept eje = dts2.getConceptByID("cl-cs_ComponentesDelPatrimonioEje");
		Concept miembro = dts2.getConceptByID("cl-cs_OtrosResultadosConAjusteEnPatrimonioMiembro");

		context.setDimensionalInformation(new MultipleDimensionType(eje, miembro), GeneralConstants.DIM_SCENARIO);
		context.setPeriodValue("2011-12-31");

		instance.addContext(context);
		
		instance.addFact(addNewFact(dts2, unit, context, "cl-cs_Patrimonio", "0"));
	}

	private void addOtrosAjustes(DiscoverableTaxonomySet dts2, Instance instance, InstanceUnit unit) throws InstanceException {
		InstanceContext context = newContext("SaldoInicio_cl-cs_EstadoCambiosEnPatrimonioTabla_cl-cs_OtrasAjustesMiembro_ACT");

		Concept eje = dts2.getConceptByID("cl-cs_ComponentesDelPatrimonioEje");
		Concept miembro = dts2.getConceptByID("cl-cs_OtrosAjustesMiembro");

		context.setDimensionalInformation(new MultipleDimensionType(eje, miembro), GeneralConstants.DIM_SCENARIO);
		context.setPeriodValue("2011-12-31");

		instance.addContext(context);
		
		instance.addFact(addNewFact(dts2, unit, context, "cl-cs_AjustesPatrimonioPeriodosAnteriores", "0"));
		instance.addFact(addNewFact(dts2, unit, context, "cl-cs_Patrimonio", "0"));
		instance.addFact(addNewFact(dts2, unit, context, "cl-cs_PatrimonioPreviamenteReportado", "0"));
	}

	private void addOtrasReservas(DiscoverableTaxonomySet dts2, Instance instance, InstanceUnit unit) throws InstanceException {
		InstanceContext context = newContext("SaldoInicio_cl-cs_EstadoCambiosEnPatrimonioTabla_cl-cs_OtrasReservasMiembro_ACT");

		Concept eje = dts2.getConceptByID("cl-cs_ComponentesDelPatrimonioEje");
		Concept miembro = dts2.getConceptByID("cl-cs_OtrasReservasMiembro");

		context.setDimensionalInformation(new MultipleDimensionType(eje, miembro), GeneralConstants.DIM_SCENARIO);
		context.setPeriodValue("2011-12-31");

		instance.addContext(context);
		
		instance.addFact(addNewFact(dts2, unit, context, "cl-cs_Patrimonio", "2273284000"));
		instance.addFact(addNewFact(dts2, unit, context, "cl-cs_PatrimonioPreviamenteReportado", "2273284000"));
	}

	private void addCapitalPagado(DiscoverableTaxonomySet dts2, Instance instance, InstanceUnit unit) throws InstanceException {
			InstanceContext context = newContext("SaldoInicio_cl-cs_EstadoCambiosEnPatrimonioTabla_cl-cs_CapitalPagadoMiembro_ACT");

			Concept eje = dts2.getConceptByID("cl-cs_ComponentesDelPatrimonioEje");
			Concept miembro = dts2.getConceptByID("cl-cs_CapitalPagadoMiembro");

			context.setDimensionalInformation(new MultipleDimensionType(eje, miembro), GeneralConstants.DIM_SCENARIO);
			context.setPeriodValue("2011-12-31");

			instance.addContext(context);
			instance.addFact(addNewFact(dts2, unit, context, "cl-cs_Patrimonio", "159350293000"));
			instance.addFact(addNewFact(dts2, unit, context, "cl-cs_PatrimonioPreviamenteReportado", "159350293000"));

	}

	private Fact addNewFact(DiscoverableTaxonomySet dts, InstanceUnit unit, InstanceContext context, String concept, String value) throws InstanceException {
			Concept patrimonio = dts.getConceptByID(concept);
			Fact fact = new Fact(patrimonio);
			fact.setDecimals(0);
			fact.setValue(value);
			fact.setInstanceUnit(unit);
			fact.setInstanceContext(context);
			return fact;
	}

	private void printInstance(Instance instance) {
		InstanceOutputter instanceOutputter = new InstanceOutputter(instance);
		instanceOutputter.setSchemaRefMapping(new SchemaRefMapping() {
			@Override
			public String getSchemaRefHREF(String taxonomyName) {
				if (taxonomyName.equals("cl-cs_shell_2012-10-05.xsd")) {
					return "http://www.svs.cl/cl/fr/cs/2012-10-05/" + taxonomyName;
				}
				
				if(taxonomyName.equals("cl-cs_eeff_role-600000.xsd")){
					return "http://www.svs.cl/cl/fr/cs/2012-10-05/cl-cs_eeff/" + taxonomyName;
				}
				
				return taxonomyName;
			}

		});
		String xmlString = instanceOutputter.getXMLString(Charset.defaultCharset());
		LOGGER.info(String.format("%n%s",xmlString));
	}

	private InstanceContext newContext(String idContext) {
		InstanceContext trimestreAcumulado = new InstanceContext(idContext);
		trimestreAcumulado.setIdentifier("123456-5");
		trimestreAcumulado.setIdentifierScheme("http://www.svs.cl/rut");
		return trimestreAcumulado;
	}

}
