package de.benjaminborbe.distributed.index.dao;

import java.util.Map.Entry;

import com.google.inject.Inject;

import de.benjaminborbe.distributed.index.api.DistributedIndexSearchResultIterator;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;

public class DistributedIndexWordDaoStorage implements DistributedIndexWordDao {

	private static final String COLUMN_FAMILY = "distributed_index_word";

	private final StorageService storageService;

	@Inject
	public DistributedIndexWordDaoStorage(final StorageService storageService) {
		this.storageService = storageService;
	}

	@Override
	public void add(final DistributedIndexEntryBean bean) throws StorageException {
		final String id = bean.getId().getId();
		for (final Entry<String, Integer> e : bean.getData().entrySet()) {
			final String columnName = buildColumnName(e.getValue(), id);
			final String columnValue = null;
			storageService.set(COLUMN_FAMILY, e.getKey(), columnName, columnValue);
		}
	}

	@Override
	public void remove(final DistributedIndexEntryBean bean) throws StorageException {
		final String id = bean.getId().getId();
		for (final Entry<String, Integer> e : bean.getData().entrySet()) {
			final String columnName = buildColumnName(e.getValue(), id);
			storageService.delete(COLUMN_FAMILY, e.getKey(), columnName);
		}
	}

	private String buildColumnName(final Integer rating, final String id) {
		return rating + "_" + id;
	}

	public DistributedIndexSearchResultIterator search(final String word) throws StorageException {
		storageService.columnIterator(COLUMN_FAMILY, word);
		return null;
	}
}
