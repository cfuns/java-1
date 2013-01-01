package de.benjaminborbe.distributed.index.dao;

import java.util.Map;

import de.benjaminborbe.distributed.index.api.DistributedIndexPageIdentifier;
import de.benjaminborbe.storage.tools.Entity;

public class DistributedIndexEntryBean implements Entity<DistributedIndexPageIdentifier> {

	private static final long serialVersionUID = -921428835583316483L;

	private DistributedIndexPageIdentifier id;

	private Map<String, Integer> data;

	@Override
	public DistributedIndexPageIdentifier getId() {
		return id;
	}

	@Override
	public void setId(final DistributedIndexPageIdentifier id) {
		this.id = id;
	}

	public Map<String, Integer> getData() {
		return data;
	}

	public void setData(final Map<String, Integer> data) {
		this.data = data;
	}
}
