package de.benjaminborbe.distributed.search.dao;

import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.Dao;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.IdentifierIterator;

public interface DistributedSearchPageDao extends Dao<DistributedSearchPageBean, DistributedSearchPageIdentifier> {

	IdentifierIterator<DistributedSearchPageIdentifier> getIdentifierIteratorByIndex(String index) throws StorageException;

	EntityIterator<DistributedSearchPageBean> getEntityIteratorByIndex(String index) throws StorageException;

}
