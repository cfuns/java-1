package de.benjaminborbe.distributed.index.dao;

import de.benjaminborbe.distributed.index.api.DistributedIndexIdentifier;

public interface DistributedIndexDao {

	DistributedIndexBean load(final DistributedIndexIdentifier id);

	void remove(final DistributedIndexIdentifier id);

	void save(final DistributedIndexBean bean);

	DistributedIndexBean create();
}
