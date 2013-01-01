package de.benjaminborbe.distributed.index.dao;

import java.util.Map.Entry;

import com.google.inject.Inject;

import de.benjaminborbe.distributed.index.DistributedIndexConstants;
import de.benjaminborbe.distributed.index.api.DistributedIndexPageIdentifier;
import de.benjaminborbe.distributed.index.api.DistributedIndexSearchResult;
import de.benjaminborbe.distributed.index.api.DistributedIndexSearchResultIterator;
import de.benjaminborbe.distributed.index.util.DistributedIndexSearchResultImpl;
import de.benjaminborbe.distributed.index.util.DistributedIndexSearchResultIteratorAdapter;
import de.benjaminborbe.distributed.index.util.DistributedIndexSearchResultMapper;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.api.StorageValue;

public class DistributedIndexWordDaoStorage implements DistributedIndexWordDao {

	private static final String COLUMN_FAMILY = "distributed_index_word";

	private final StorageService storageService;

	private final DistributedIndexSearchResultMapper distributedIndexSearchResultMapper;

	@Inject
	public DistributedIndexWordDaoStorage(final StorageService storageService, final DistributedIndexSearchResultMapper distributedIndexSearchResultMapper) {
		this.storageService = storageService;
		this.distributedIndexSearchResultMapper = distributedIndexSearchResultMapper;
	}

	@Override
	public void add(final DistributedIndexEntryBean bean) throws StorageException {
		final DistributedIndexPageIdentifier id = bean.getId();
		for (final Entry<String, Integer> e : bean.getData().entrySet()) {
			final StorageValue columnName = buildColumnName(e.getValue(), id);
			final StorageValue columnValue = new StorageValue();
			storageService.set(COLUMN_FAMILY, new StorageValue(e.getKey(), DistributedIndexConstants.ENCODING), columnName, columnValue);
		}
	}

	@Override
	public void remove(final DistributedIndexEntryBean bean) throws StorageException {
		final DistributedIndexPageIdentifier id = bean.getId();
		for (final Entry<String, Integer> e : bean.getData().entrySet()) {
			final StorageValue columnName = buildColumnName(e.getValue(), id);
			storageService.delete(COLUMN_FAMILY, new StorageValue(e.getKey(), DistributedIndexConstants.ENCODING), columnName);
		}
	}

	private StorageValue buildColumnName(final Integer rating, final DistributedIndexPageIdentifier id) {
		final DistributedIndexSearchResult distributedIndexSearchResult = new DistributedIndexSearchResultImpl(rating, id);
		return new StorageValue(distributedIndexSearchResultMapper.toString(distributedIndexSearchResult), DistributedIndexConstants.ENCODING);
	}

	@Override
	public DistributedIndexSearchResultIterator search(final String word) throws StorageException {
		return new DistributedIndexSearchResultIteratorAdapter(distributedIndexSearchResultMapper, storageService.columnIterator(COLUMN_FAMILY, new StorageValue(word,
				DistributedIndexConstants.ENCODING)));
	}
}
