package de.benjaminborbe.search.api;

import java.net.URL;


public class SearchResultImpl implements SearchResult {

	private final URL url;

	private final String title;

	public SearchResultImpl(final String title, final URL url) {
		this.title = title;
		this.url = url;

	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public URL getUrl() {
		return url;
	}

}
