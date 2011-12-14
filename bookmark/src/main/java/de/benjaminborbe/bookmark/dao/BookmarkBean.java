package de.benjaminborbe.bookmark.dao;

import de.benjaminborbe.bookmark.api.Bookmark;
import de.benjaminborbe.tools.dao.Entity;

public class BookmarkBean implements Entity, Bookmark {

	private static final long serialVersionUID = 6058606350883201939L;

	private Long id;

	private String name;

	private String url;

	private String description;

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
	public void setId(final Long id) {
		this.id = id;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}
}
