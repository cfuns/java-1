package de.benjaminborbe.distributed.index.dao;

import de.benjaminborbe.distributed.index.api.DistributedIndexIdentifier;
import de.benjaminborbe.storage.api.StorageException;

public interface DistributedIndexDao {

	DistributedIndexBean load(final DistributedIndexIdentifier id) throws StorageException;

	void remove(final DistributedIndexIdentifier id) throws StorageException;

	void save(final DistributedIndexBean bean) throws StorageException;

	DistributedIndexBean create() throws StorageException;
}
