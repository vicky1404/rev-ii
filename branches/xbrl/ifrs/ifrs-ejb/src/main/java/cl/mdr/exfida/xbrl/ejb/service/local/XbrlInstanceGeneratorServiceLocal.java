package cl.mdr.exfida.xbrl.ejb.service.local;

import javax.ejb.Local;

import xbrlcore.instance.Instance;
import xbrlcore.taxonomy.DiscoverableTaxonomySet;
import cl.mdr.exfida.xbrl.ejb.entity.XbrlTaxonomia;
import cl.mdr.ifrs.ejb.entity.Periodo;

@Local
public interface XbrlInstanceGeneratorServiceLocal {
  Instance generarInstancia(DiscoverableTaxonomySet dts, XbrlTaxonomia xbrlTaxonomia, String rut, Periodo periodo);

    
}
