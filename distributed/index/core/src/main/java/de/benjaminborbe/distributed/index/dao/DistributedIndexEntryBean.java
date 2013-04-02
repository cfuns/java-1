package de.benjaminborbe.distributed.index.dao;

import de.benjaminborbe.storage.tools.EntityBase;

import java.util.Map;

public class DistributedIndexEntryBean extends EntityBase<DistributedIndexEntryIdentifier> {

	private static final long serialVersionUID = -921428835583316483L;

	private DistributedIndexEntryIdentifier id;

	private Map<String, Integer> data;

	public Map<String, Integer> getData() {
		return data;
	}

	public void setData(final Map<String, Integer> data) {
		this.data = data;
	}

	@Override
	public DistributedIndexEntryIdentifier getId() {
		return id;
	}

	@Override
	public void setId(final DistributedIndexEntryIdentifier id) {
		this.id = id;
	}

}
