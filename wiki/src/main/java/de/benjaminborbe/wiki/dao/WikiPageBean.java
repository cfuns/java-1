package de.benjaminborbe.wiki.dao;

import java.util.Calendar;

import de.benjaminborbe.storage.tools.EntityBase;
import de.benjaminborbe.storage.tools.HasCreated;
import de.benjaminborbe.storage.tools.HasModified;
import de.benjaminborbe.wiki.api.WikiPage;
import de.benjaminborbe.wiki.api.WikiPageContentType;
import de.benjaminborbe.wiki.api.WikiPageIdentifier;
import de.benjaminborbe.wiki.api.WikiSpaceIdentifier;

public class WikiPageBean extends EntityBase<WikiPageIdentifier> implements WikiPage, HasCreated, HasModified {

	private static final long serialVersionUID = 6058606350883201939L;

	private String title;

	private String content;

	private WikiSpaceIdentifier space;

	private WikiPageContentType contentType;

	private Calendar modified;

	private Calendar created;

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

	public WikiSpaceIdentifier getSpace() {
		return space;
	}

	public void setSpace(final WikiSpaceIdentifier space) {
		this.space = space;
	}

	@Override
	public WikiPageContentType getContentType() {
		return contentType;
	}

	public void setContentType(final WikiPageContentType contentType) {
		this.contentType = contentType;
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
