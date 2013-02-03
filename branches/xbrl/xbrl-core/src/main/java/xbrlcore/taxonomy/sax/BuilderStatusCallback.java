package xbrlcore.taxonomy.sax;

/**
 * Call back para eventos del estados de la carga de la taxonomia
 * @author saburto
 *
 */
public interface BuilderStatusCallback {
	
	void startBuild();
	void endBuild();
	void loadingTaxonomy(String name);
	void loadingLinkBase(int total, int index, String name);

}
