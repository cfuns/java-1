package de.benjaminborbe.wiki.render;

public class WikiPlainRenderer implements WikiRenderer {

	@Override
	public String render(final String markupContent) {
		return markupContent;
	}

}
