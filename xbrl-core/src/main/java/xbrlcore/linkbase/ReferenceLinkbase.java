package xbrlcore.linkbase;

import xbrlcore.taxonomy.DiscoverableTaxonomySet;

/**
 * This class represents a label linkbase of a DTS. This contains one or more
 * labels for concepts. <br/><br/>
 * 
 * @author Daniel Hamm
 */
public class ReferenceLinkbase extends Linkbase {

	private static final long serialVersionUID = 1794952740633234300L;

    public static final String NAME = "reference";

	/**
	 * Constructor.
	 * 
	 * @param dts
	 *            The taxonomy this label linkbase refers to.
	 */
	public ReferenceLinkbase(DiscoverableTaxonomySet dts) {
		super(dts, NAME);
	}

	@Override
	public void buildLinkbase() {
	}
}
