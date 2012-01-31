package de.benjaminborbe.search.api;

import java.net.URL;

public class SearchResultImpl implements SearchResult {

	private final URL url;

	private final String title;

	private final String description;

	private final String type;

	public SearchResultImpl(final String type, final String title, final URL url, final String description) {
		this.type = type;
		this.title = title;
		this.url = url;
		this.description = description;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public URL getUrl() {
		return url;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getType() {
		return type;
	}

}
