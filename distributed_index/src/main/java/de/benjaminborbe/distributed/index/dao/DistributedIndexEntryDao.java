package de.benjaminborbe.distributed.index.dao;

import de.benjaminborbe.distributed.index.api.DistributedIndexIdentifier;
import de.benjaminborbe.storage.api.StorageException;

public interface DistributedIndexEntryDao {

	DistributedIndexEntryBean load(final DistributedIndexIdentifier id) throws StorageException;

	void remove(final DistributedIndexIdentifier id) throws StorageException;

	void save(final DistributedIndexEntryBean bean) throws StorageException;

	DistributedIndexEntryBean create() throws StorageException;
}
