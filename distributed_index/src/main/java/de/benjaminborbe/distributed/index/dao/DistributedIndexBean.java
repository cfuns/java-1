package de.benjaminborbe.distributed.index.dao;

import java.util.Map;

import de.benjaminborbe.distributed.index.api.DistributedIndexIdentifier;
import de.benjaminborbe.storage.tools.Entity;

public class DistributedIndexBean implements Entity<DistributedIndexIdentifier> {

	private static final long serialVersionUID = -921428835583316483L;

	private DistributedIndexIdentifier id;

	private Map<String, Integer> data;

	@Override
	public DistributedIndexIdentifier getId() {
		return id;
	}

	@Override
	public void setId(final DistributedIndexIdentifier id) {
		this.id = id;
	}

	public Map<String, Integer> getData() {
		return data;
	}

	public void setData(final Map<String, Integer> data) {
		this.data = data;
	}
}
