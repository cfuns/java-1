package de.benjaminborbe.distributed.index.dao;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.distributed.index.DistributedIndexConstants;
import de.benjaminborbe.distributed.index.api.DistributedIndexIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.api.StorageValue;
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
		try {
			final Map<String, Integer> data = new HashMap<String, Integer>();
			final Map<StorageValue, StorageValue> row = storageService.get(COLUMN_FAMILY, new StorageValue(id.getId(), DistributedIndexConstants.ENCODING));
			for (final Entry<StorageValue, StorageValue> e : row.entrySet()) {
				data.put(e.getKey().getString(), mapperInteger.fromString(e.getValue().getString()));
			}
			final DistributedIndexEntryBean bean = create();
			bean.setId(id);
			bean.setData(data);
			return bean;
		}
		catch (final UnsupportedEncodingException e) {
			throw new StorageException(e);
		}
	}

	@Override
	public void remove(final DistributedIndexIdentifier id) throws StorageException {
		final DistributedIndexEntryBean bean = load(id);
		distributedIndexWordDao.remove(bean);
		storageService.delete(COLUMN_FAMILY, new StorageValue(id.getId(), DistributedIndexConstants.ENCODING));
	}

	@Override
	public void save(final DistributedIndexEntryBean bean) throws StorageException {
		final Map<StorageValue, StorageValue> data = new HashMap<StorageValue, StorageValue>();
		for (final Entry<String, Integer> e : bean.getData().entrySet()) {
			data.put(new StorageValue(e.getKey(), DistributedIndexConstants.ENCODING), new StorageValue(mapperInteger.toString(e.getValue()), DistributedIndexConstants.ENCODING));
		}
		storageService.set(COLUMN_FAMILY, new StorageValue(bean.getId().getId(), DistributedIndexConstants.ENCODING), data);

		distributedIndexWordDao.add(bean);
	}

	@Override
	public DistributedIndexEntryBean create() {
		return provider.get();
	}
}
