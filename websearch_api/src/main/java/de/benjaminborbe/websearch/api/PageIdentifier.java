package de.benjaminborbe.websearch.api;

import java.net.URL;

import de.benjaminborbe.api.IdentifierBase;

public class PageIdentifier extends IdentifierBase<String> {

	private final URL url;

	public PageIdentifier(final URL url) {
		super(url.toExternalForm());
		this.url = url;
	}

	public URL getUrl() {
		return url;
	}

}
