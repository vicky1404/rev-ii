package xbrlcore.taxonomy.sax;

public final class DefaultStatusCallback implements BuilderStatusCallback {
	private long startTime;

	@Override
	public void startBuild() {
		startTime = System.currentTimeMillis();
		SAXBuilder.LOGGER.info("Loading top schema");
	}

	@Override
	public void loadingTaxonomy(String name) {
		SAXBuilder.LOGGER.info("Loading " + name);
	}

	@Override
	public void loadingLinkBase(int total, int index, String name) {
		SAXBuilder.LOGGER.info(String.format("Loading linkbase %s of %s : %s", index, total, name));
	}

	@Override
	public void endBuild() {
		long seconds = (System.currentTimeMillis() - startTime) / 1000;
		SAXBuilder.LOGGER.info(String.format("Taxnomy loaded in %s seconds", seconds ));
	}
}