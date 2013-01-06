package de.benjaminborbe.distributed.search.dao;

import java.util.Calendar;

import de.benjaminborbe.storage.tools.EntityBase;
import de.benjaminborbe.storage.tools.HasCreated;
import de.benjaminborbe.storage.tools.HasModified;

public class DistributedSearchPageBean extends EntityBase<DistributedSearchPageIdentifier> implements HasCreated, HasModified {

	private static final long serialVersionUID = -921428835583316483L;

	private String index;

	private String title;

	private String content;

	private Calendar created;

	private Calendar modified;

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

	public String getIndex() {
		return index;
	}

	public void setIndex(final String indexName) {
		this.index = indexName;
	}

}
