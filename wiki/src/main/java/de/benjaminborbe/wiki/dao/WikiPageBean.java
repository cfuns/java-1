package de.benjaminborbe.wiki.dao;

import de.benjaminborbe.storage.tools.Entity;
import de.benjaminborbe.wiki.api.WikiPage;
import de.benjaminborbe.wiki.api.WikiPageIdentifier;

public class WikiPageBean implements Entity<WikiPageIdentifier>, WikiPage {

	private static final long serialVersionUID = 6058606350883201939L;

	private WikiPageIdentifier id;

	private String title;

	private String content;

	@Override
	public WikiPageIdentifier getId() {
		return id;
	}

	@Override
	public void setId(final WikiPageIdentifier id) {
		this.id = id;
	}

	@Override
	public String getTitle() {
		return title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@Override
	public String getContent() {
		return content;
	}

	public void setContent(final String content) {
		this.content = content;
	}
}
