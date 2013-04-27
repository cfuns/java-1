package de.benjaminborbe.distributed.search.util;

import de.benjaminborbe.distributed.search.api.DistributedSearchResult;

import java.util.Calendar;

public class DistributedSearchResultImpl implements DistributedSearchResult {

	private final String index;

	private final String url;

	private final String title;

	private final String content;

	private final Calendar added;

	private final Calendar updated;

	private final Calendar date;

	public DistributedSearchResultImpl(
		final String index,
		final String url,
		final String title,
		final String content,
		final Calendar date,
		final Calendar added,
		final Calendar updated
	) {
		this.index = index;
		this.url = url;
		this.title = title;
		this.content = content;
		this.date = date;
		this.added = added;
		this.updated = updated;
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
	public Calendar getUpdated() {
		return updated;
	}

	@Override
	public Calendar getAdded() {
		return added;
	}

	@Override
	public Calendar getDate() {
		return date;
	}

}
