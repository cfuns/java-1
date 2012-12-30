package de.benjaminborbe.distributed.index.dao;

import java.util.Calendar;
import java.util.Map;

import de.benjaminborbe.distributed.index.api.DistributedIndexIdentifier;
import de.benjaminborbe.storage.tools.Entity;
import de.benjaminborbe.storage.tools.HasCreated;
import de.benjaminborbe.storage.tools.HasModified;

public class DistributedIndexBean implements Entity<DistributedIndexIdentifier>, HasCreated, HasModified {

	private static final long serialVersionUID = -921428835583316483L;

	private DistributedIndexIdentifier id;

	private Calendar modified;

	private Calendar created;

	private Map<String, Integer> data;

	@Override
	public DistributedIndexIdentifier getId() {
		return id;
	}

	@Override
	public void setId(final DistributedIndexIdentifier id) {
		this.id = id;
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

	public Map<String, Integer> getData() {
		return data;
	}

	public void setData(Map<String, Integer> data) {
		this.data = data;
	}
}
