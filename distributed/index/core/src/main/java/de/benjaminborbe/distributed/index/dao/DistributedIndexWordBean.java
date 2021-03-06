package de.benjaminborbe.distributed.index.dao;

import de.benjaminborbe.storage.tools.EntityBase;

public class DistributedIndexWordBean extends EntityBase<DistributedIndexWordIdentifier> {

	private static final long serialVersionUID = -4706976758281851561L;

	private DistributedIndexWordIdentifier id;

	@Override
	public DistributedIndexWordIdentifier getId() {
		return id;
	}

	@Override
	public void setId(final DistributedIndexWordIdentifier id) {
		this.id = id;
	}

}
