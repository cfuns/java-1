package de.benjaminborbe.wiki.render;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.wiki.api.WikiPageContentType;

@Singleton
public class WikiRendererFactoryImpl implements WikiRendererFactory {

	private final WikiConfluenceRenderer wikiConfluenceRenderer;

	private final WikiPlainRenderer wikiPlainRenderer;

	@Inject
	public WikiRendererFactoryImpl(final WikiPlainRenderer wikiPlainRenderer, final WikiConfluenceRenderer wikiConfluenceRenderer) {
		this.wikiPlainRenderer = wikiPlainRenderer;
		this.wikiConfluenceRenderer = wikiConfluenceRenderer;
	}

	@Override
	public WikiRenderer getRenderer(final WikiPageContentType wikiPageContentType) throws WikiRendererNotFoundException {
		if (WikiPageContentType.CONFLUENCE.equals(wikiPageContentType)) {
			return wikiConfluenceRenderer;
		}
		if (WikiPageContentType.PLAIN.equals(wikiPageContentType)) {
			return wikiPlainRenderer;
		}
		throw new WikiRendererNotFoundException("can't find for wikiPageContentType: " + wikiPageContentType);
	}
}
