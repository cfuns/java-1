package de.benjaminborbe.bookmark.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.bookmark.api.Bookmark;
import de.benjaminborbe.bookmark.api.BookmarkIdentifier;
import de.benjaminborbe.storage.tools.Entity;
import de.benjaminborbe.storage.tools.HasCreated;
import de.benjaminborbe.storage.tools.HasModified;

public class BookmarkBean implements Entity<BookmarkIdentifier>, Bookmark, HasCreated, HasModified {

	private static final long serialVersionUID = 6058606350883201939L;

	private BookmarkIdentifier id;

	private String name;

	private String url;

	private String description;

	private List<String> keywords;

	private Boolean favorite;

	private UserIdentifier owner;

	private Calendar modified;

	private Calendar created;

	@Override
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public String getUrl() {
		return url;
	}

	public void setUrl(final String url) {
		this.url = url;
	}

	@Override
	public void setId(final BookmarkIdentifier id) {
		this.id = id;
	}

	@Override
	public BookmarkIdentifier getId() {
		return id;
	}

	@Override
	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public void setKeywords(final List<String> keywords) {
		this.keywords = keywords;
	}

	@Override
	public List<String> getKeywords() {
		final List<String> result = new ArrayList<String>();
		if (keywords != null) {
			for (final String keyword : keywords) {
				if (keyword != null && !keyword.isEmpty()) {
					result.add(keyword);
				}
			}
		}
		return result;
	}

	public Boolean getFavorite() {
		return favorite;
	}

	public void setFavorite(final Boolean favorite) {
		this.favorite = favorite;
	}

	public UserIdentifier getOwner() {
		return owner;
	}

	public void setOwner(final UserIdentifier owner) {
		this.owner = owner;
	}

	@Override
	public Calendar getModified() {
		return modified;
	}

	@Override
	public void setModified(final Calendar modified) {
		this.modified = modified;
	}

	@Override
	public Calendar getCreated() {
		return created;
	}

	@Override
	public void setCreated(final Calendar created) {
		this.created = created;
	}

}
