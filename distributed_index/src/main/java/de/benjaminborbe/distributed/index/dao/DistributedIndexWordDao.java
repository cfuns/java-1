package de.benjaminborbe.distributed.index.dao;

import de.benjaminborbe.storage.api.StorageException;

public interface DistributedIndexWordDao {

	void add(DistributedIndexEntryBean bean) throws StorageException;

	void remove(DistributedIndexEntryBean bean) throws StorageException;

}
