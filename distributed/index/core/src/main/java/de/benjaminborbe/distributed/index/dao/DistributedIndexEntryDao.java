package de.benjaminborbe.distributed.index.dao;

import de.benjaminborbe.storage.api.StorageException;

public interface DistributedIndexEntryDao {

	DistributedIndexEntryBean load(final DistributedIndexEntryIdentifier id) throws StorageException;

	void remove(final DistributedIndexEntryIdentifier id) throws StorageException;

	void save(final DistributedIndexEntryBean bean) throws StorageException;

	DistributedIndexEntryBean create();
}
