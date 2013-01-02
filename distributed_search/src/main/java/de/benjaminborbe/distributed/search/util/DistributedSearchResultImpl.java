package de.benjaminborbe.distributed.search.util;

import de.benjaminborbe.distributed.search.api.DistributedSearchResult;

public class DistributedSearchResultImpl implements DistributedSearchResult {

	private final String index;

	private final String url;

	private final String title;

	private final String content;

	public DistributedSearchResultImpl(final String index, final String url, final String title, final String content) {
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

}
