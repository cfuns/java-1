package de.benjaminborbe.distributed.index.util;

import java.io.UnsupportedEncodingException;

import de.benjaminborbe.distributed.index.api.DistributedIndexSearchResult;
import de.benjaminborbe.distributed.index.api.DistributedIndexSearchResultIterator;
import de.benjaminborbe.distributed.index.api.DistributedIndexServiceException;
import de.benjaminborbe.storage.api.StorageColumn;
import de.benjaminborbe.storage.api.StorageColumnIterator;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.tools.mapper.MapException;

public class DistributedIndexSearchResultIteratorAdapter implements DistributedIndexSearchResultIterator {

	private final StorageColumnIterator storageColumnIterator;

	private final DistributedIndexSearchResultMapper distributedIndexSearchResultMapper;

	public DistributedIndexSearchResultIteratorAdapter(final DistributedIndexSearchResultMapper distributedIndexSearchResultMapper, final StorageColumnIterator storageColumnIterator) {
		this.distributedIndexSearchResultMapper = distributedIndexSearchResultMapper;
		this.storageColumnIterator = storageColumnIterator;
	}

	@Override
	public boolean hasNext() throws DistributedIndexServiceException {
		try {
			return storageColumnIterator.hasNext();
		}
		catch (final StorageException e) {
			throw new DistributedIndexServiceException(e);
		}
	}

	@Override
	public DistributedIndexSearchResult next() throws DistributedIndexServiceException {
		try {
			final StorageColumn storageColumn = storageColumnIterator.next();
			return distributedIndexSearchResultMapper.fromString(storageColumn.getColumnNameString());
		}
		catch (final StorageException e) {
			throw new DistributedIndexServiceException(e);
		}
		catch (final MapException e) {
			throw new DistributedIndexServiceException(e);
		}
		catch (final UnsupportedEncodingException e) {
			throw new DistributedIndexServiceException(e);
		}
	}

}
