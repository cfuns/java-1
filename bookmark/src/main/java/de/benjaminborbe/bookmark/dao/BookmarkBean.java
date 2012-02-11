package de.benjaminborbe.bookmark.dao;

import java.util.List;

import de.benjaminborbe.bookmark.api.Bookmark;
import de.benjaminborbe.storage.tools.EntityLong;

public class BookmarkBean implements EntityLong, Bookmark {

	private static final long serialVersionUID = 6058606350883201939L;

	private Long id;

	private String name;

	private String url;

	private String description;

	private List<String> keywords;

	private boolean favorite;

	private String ownerUsername;

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

	public void setKeywords(final List<String> keywords) {
		this.keywords = keywords;
	}

	@Override
	public List<String> getKeywords() {
		return keywords;
	}

	public boolean isFavorite() {
		return favorite;
	}

	public void setFavorite(final boolean favorite) {
		this.favorite = favorite;
	}

	public String getOwnerUsername() {
		return ownerUsername;
	}

	public void setOwnerUsername(final String ownerUsername) {
		this.ownerUsername = ownerUsername;
	}
}
