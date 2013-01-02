package de.benjaminborbe.distributed.search.dao;

import java.util.Calendar;

import de.benjaminborbe.storage.tools.Entity;
import de.benjaminborbe.storage.tools.HasCreated;
import de.benjaminborbe.storage.tools.HasModified;

public class DistributedSearchPageBean implements Entity<DistributedSearchPageIdentifier>, HasCreated, HasModified {

	private static final long serialVersionUID = -921428835583316483L;

	private DistributedSearchPageIdentifier id;

	private String index;

	private String title;

	private String content;

	private Calendar created;

	private Calendar modified;

	@Override
	public DistributedSearchPageIdentifier getId() {
		return id;
	}

	@Override
	public void setId(final DistributedSearchPageIdentifier id) {
		this.id = id;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(final String index) {
		this.index = index;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(final String content) {
		this.content = content;
	}

	public Calendar getCreated() {
		return created;
	}

	public void setCreated(Calendar created) {
		this.created = created;
	}

	public Calendar getModified() {
		return modified;
	}

	public void setModified(Calendar modified) {
		this.modified = modified;
	}

}
