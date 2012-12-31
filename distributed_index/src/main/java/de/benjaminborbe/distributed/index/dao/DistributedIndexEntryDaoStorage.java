package de.benjaminborbe.distributed.index.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.distributed.index.api.DistributedIndexIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.tools.mapper.MapperInteger;

@Singleton
public class DistributedIndexEntryDaoStorage implements DistributedIndexEntryDao {

	private static final String COLUMN_FAMILY = "distributed_index_entry";

	private final Provider<DistributedIndexEntryBean> provider;

	private final StorageService storageService;

	private final MapperInteger mapperInteger;

	private final DistributedIndexWordDao distributedIndexWordDao;

	@Inject
	public DistributedIndexEntryDaoStorage(
			final Provider<DistributedIndexEntryBean> provider,
			final StorageService storageService,
			final MapperInteger mapperInteger,
			final DistributedIndexWordDao distributedIndexWordDao) {
		this.provider = provider;
		this.storageService = storageService;
		this.mapperInteger = mapperInteger;
		this.distributedIndexWordDao = distributedIndexWordDao;
	}

	@Override
	public DistributedIndexEntryBean load(final DistributedIndexIdentifier id) throws StorageException {
		final Map<String, Integer> data = new HashMap<String, Integer>();
		final Map<String, String> row = storageService.get(COLUMN_FAMILY, id.getId());
		for (final Entry<String, String> e : row.entrySet()) {
			data.put(e.getKey(), mapperInteger.fromString(e.getValue()));
		}
		final DistributedIndexEntryBean bean = create();
		bean.setId(id);
		bean.setData(data);
		return bean;
	}

	@Override
	public void remove(final DistributedIndexIdentifier id) throws StorageException {
		final DistributedIndexEntryBean bean = load(id);
		distributedIndexWordDao.remove(bean);
		storageService.delete(COLUMN_FAMILY, id.getId());
	}

	@Override
	public void save(final DistributedIndexEntryBean bean) throws StorageException {
		final Map<String, String> data = new HashMap<String, String>();
		for (final Entry<String, Integer> e : bean.getData().entrySet()) {
			data.put(e.getKey(), mapperInteger.toString(e.getValue()));
		}
		storageService.set(COLUMN_FAMILY, bean.getId().getId(), data);

		distributedIndexWordDao.add(bean);
	}

	@Override
	public DistributedIndexEntryBean create() {
		return provider.get();
	}
}
