package cl.mdr.exfida.modules.xbrl.mb.generador;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.log4j.Logger;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.xml.sax.InputSource;

import xbrlcore.instance.Instance;
import xbrlcore.instance.InstanceOutputter;
import xbrlcore.instance.SchemaRefMapping;
import xbrlcore.taxonomy.DiscoverableTaxonomySet;
import xbrlcore.taxonomy.sax.SAXBuilder;
import cl.mdr.exfida.xbrl.ejb.service.local.XbrlInstanceGeneratorServiceLocal;
import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.ejb.entity.Empresa;

@ManagedBean(name = "generadorInstancia")
@ViewScoped
public class GeneradorInstanciaBackingBean extends AbstractBackingBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private transient final Logger LOG = Logger.getLogger(GeneradorInstanciaBackingBean.class);

	@EJB
	private XbrlInstanceGeneratorServiceLocal generator;

	private String instanciaString;
	private Instance instancia;
	private boolean verDescarga = false;

	private static Map<String, DiscoverableTaxonomySet> dtsMap = new HashMap<String, DiscoverableTaxonomySet>();

	public void generarInstancia() {
		try {

			String uri = getFiltroBackingBean().getXbrlTaxonomia().getUri();
			DiscoverableTaxonomySet dts = dtsMap.get(uri);
			if (dts == null) {
				dts = new SAXBuilder().build(new InputSource(uri));
				dtsMap.put(uri, dts);
			}

			Empresa empresa = getFiltroBackingBean().getEmpresa();
			String rut = String.format("%s-%s", empresa.getIdRut(), empresa.getDv());
			instancia = generator.generarInstancia(dts, getFiltroBackingBean().getXbrlTaxonomia(), rut, getComponenteBackingBean().getPeriodoActual());

			InstanceOutputter instanceOutputter = new InstanceOutputter(instancia);
			instanceOutputter.setSchemaRefMapping(new SchemaRefMapping() {
				@Override
				public String getSchemaRefHREF(String taxonomyName) {
					if (taxonomyName.equals("cl-cs_shell_2012-10-05.xsd")) {
						return "http://www.svs.cl/cl/fr/cs/2012-10-05/" + taxonomyName;
					}
					return taxonomyName;
				}

			});
			instanceOutputter.setAddConceptNamespaceToFact(true);
			instanciaString = instanceOutputter.getXMLString(Charset.defaultCharset());
			addInfoMessage("Se han generado correctamente la instancia de XBRL");
			verDescarga = true;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);

			addErrorMessage("Error al generar la instancia de xbrl");
			StringWriter stringWriter = new StringWriter();
			e.printStackTrace(new PrintWriter(stringWriter));
			instanciaString = stringWriter.toString();
		}
	}

	public StreamedContent getFile() {
		ByteArrayInputStream stream = new ByteArrayInputStream(instanciaString.getBytes());
		StreamedContent file = new DefaultStreamedContent(stream, "application/xml", "instance.xbrl");
		return file;
	}

	public String getInstanciaString() {
		return instanciaString;
	}

	public boolean isVerDescarga() {
		return verDescarga;
	}

}
