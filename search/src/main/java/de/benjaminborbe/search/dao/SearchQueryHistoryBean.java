package de.benjaminborbe.search.dao;

import java.util.Calendar;

import de.benjaminborbe.storage.tools.Entity;
import de.benjaminborbe.storage.tools.HasCreated;
import de.benjaminborbe.storage.tools.HasModified;

public class SearchQueryHistoryBean implements Entity<SearchQueryHistoryIdentifier>, HasCreated, HasModified {

	private static final long serialVersionUID = -8803301003126328406L;

	private SearchQueryHistoryIdentifier id;

	private String query;

	private Calendar created;

	private Calendar modified;

	@Override
	public SearchQueryHistoryIdentifier getId() {
		return id;
	}

	@Override
	public void setId(final SearchQueryHistoryIdentifier id) {
		this.id = id;
	}

	@Override
	public Calendar getCreated() {
		return created;
	}

	@Override
	public void setCreated(final Calendar created) {
		this.created = created;
	}

	@Override
	public Calendar getModified() {
		return modified;
	}

	@Override
	public void setModified(final Calendar modified) {
		this.modified = modified;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(final String query) {
		this.query = query;
	}

}
