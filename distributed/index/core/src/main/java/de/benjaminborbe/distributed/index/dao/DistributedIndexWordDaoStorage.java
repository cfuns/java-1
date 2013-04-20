package de.benjaminborbe.distributed.index.dao;

import javax.inject.Inject;
import de.benjaminborbe.distributed.index.DistributedIndexConstants;
import de.benjaminborbe.distributed.index.api.DistributedIndexSearchResult;
import de.benjaminborbe.distributed.index.api.DistributedIndexSearchResultIterator;
import de.benjaminborbe.distributed.index.util.DistributedIndexSearchResultImpl;
import de.benjaminborbe.distributed.index.util.DistributedIndexSearchResultIteratorAdapter;
import de.benjaminborbe.distributed.index.util.DistributedIndexSearchResultMapper;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.api.StorageValue;
import org.slf4j.Logger;

import java.util.Map.Entry;

public class DistributedIndexWordDaoStorage implements DistributedIndexWordDao {

	private static final String COLUMN_FAMILY = "distributed_index_word";

	private final StorageService storageService;

	private final DistributedIndexSearchResultMapper distributedIndexSearchResultMapper;

	private final Logger logger;

	@Inject
	public DistributedIndexWordDaoStorage(final Logger logger, final StorageService storageService, final DistributedIndexSearchResultMapper distributedIndexSearchResultMapper) {
		this.logger = logger;
		this.storageService = storageService;
		this.distributedIndexSearchResultMapper = distributedIndexSearchResultMapper;
	}

	@Override
	public void add(final DistributedIndexEntryBean bean) throws StorageException {
		logger.trace("add");
		final DistributedIndexEntryIdentifier id = bean.getId();
		for (final Entry<String, Integer> e : bean.getData().entrySet()) {
			final StorageValue columnName = buildColumnName(e.getValue(), id);
			final StorageValue columnValue = new StorageValue(new byte[0], DistributedIndexConstants.ENCODING);
			storageService.set(COLUMN_FAMILY, new StorageValue(id.getIndex() + DistributedIndexConstants.SEPERATOR + e.getKey(), DistributedIndexConstants.ENCODING), columnName,
				columnValue);
		}
	}

	@Override
	public void remove(final DistributedIndexEntryBean bean) throws StorageException {
		logger.debug("remove");
		final DistributedIndexEntryIdentifier id = bean.getId();
		for (final Entry<String, Integer> e : bean.getData().entrySet()) {
			final StorageValue columnName = buildColumnName(e.getValue(), id);
			storageService.delete(COLUMN_FAMILY, new StorageValue(id.getIndex() + DistributedIndexConstants.SEPERATOR + e.getKey(), DistributedIndexConstants.ENCODING), columnName);
		}
	}

	private StorageValue buildColumnName(final Integer rating, final DistributedIndexEntryIdentifier id) {
		final DistributedIndexSearchResult distributedIndexSearchResult = new DistributedIndexSearchResultImpl(rating, id);
		return new StorageValue(distributedIndexSearchResultMapper.toString(distributedIndexSearchResult), DistributedIndexConstants.ENCODING);
	}

	@Override
	public DistributedIndexSearchResultIterator search(final DistributedIndexWordIdentifier word) throws StorageException {
		logger.debug("search: " + word);
		final StorageValue wordIdentifier = new StorageValue(word.getId(), DistributedIndexConstants.ENCODING);
		return new DistributedIndexSearchResultIteratorAdapter(distributedIndexSearchResultMapper, storageService.columnIterator(COLUMN_FAMILY, wordIdentifier));
	}
}
