package de.benjaminborbe.websearch.api;

import java.net.URL;

public class PageIdentifierImpl implements PageIdentifier {

	private final URL url;

	public PageIdentifierImpl(final URL url) {
		this.url = url;
	}

	@Override
	public URL getUrl() {
		return url;
	}

}
