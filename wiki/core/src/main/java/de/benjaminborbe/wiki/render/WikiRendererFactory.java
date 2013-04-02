package de.benjaminborbe.wiki.render;

import de.benjaminborbe.wiki.api.WikiPageContentType;

public interface WikiRendererFactory {

	WikiRenderer getRenderer(WikiPageContentType wikiPageContentType) throws WikiRendererNotFoundException;
}
