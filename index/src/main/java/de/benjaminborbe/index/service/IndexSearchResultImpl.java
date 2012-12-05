package de.benjaminborbe.index.service;

import de.benjaminborbe.index.api.IndexSearchResult;

public class IndexSearchResultImpl implements IndexSearchResult {

	private final String url;

	private final String content;

	private final String title;

	private final String index;

	public IndexSearchResultImpl(final String index, final String url, final String title, final String content) {
		this.index = index;
		this.url = url;
		this.title = title;
		this.content = content;
	}

	@Override
	public String getIndex() {
		return index;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getContent() {
		return content;
	}

	@Override
	public String getURL() {
		return url;
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (url == null)
			return false;
		if (!(other instanceof IndexSearchResultImpl))
			return false;
		if (!getClass().equals(other.getClass()))
			return false;
		return url.equals(((IndexSearchResultImpl) other).url);
	}

	@Override
	public int hashCode() {
		return url != null ? url.hashCode() : super.hashCode();
	}

}
