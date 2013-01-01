package de.benjaminborbe.distributed.index.dao;

import de.benjaminborbe.distributed.index.api.DistributedIndexPageIdentifier;
import de.benjaminborbe.storage.api.StorageException;

public interface DistributedIndexEntryDao {

	DistributedIndexEntryBean load(final DistributedIndexPageIdentifier id) throws StorageException;

	void remove(final DistributedIndexPageIdentifier id) throws StorageException;

	void save(final DistributedIndexEntryBean bean) throws StorageException;

	DistributedIndexEntryBean create() throws StorageException;
}
