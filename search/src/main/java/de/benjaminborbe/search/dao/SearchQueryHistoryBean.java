package de.benjaminborbe.search.dao;

import java.util.Calendar;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.storage.tools.EntityBase;
import de.benjaminborbe.storage.tools.HasCreated;
import de.benjaminborbe.storage.tools.HasModified;

public class SearchQueryHistoryBean extends EntityBase<SearchQueryHistoryIdentifier> implements HasCreated, HasModified {

	private static final long serialVersionUID = -8803301003126328406L;

	private String query;

	private Calendar created;

	private Calendar modified;

	private UserIdentifier user;

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

	public UserIdentifier getUser() {
		return user;
	}

	public void setUser(final UserIdentifier user) {
		this.user = user;
	}

}
