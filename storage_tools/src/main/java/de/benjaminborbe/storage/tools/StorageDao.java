package de.benjaminborbe.storage.tools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.tools.mapper.MapException;
import de.benjaminborbe.tools.mapper.Mapper;

@Singleton
public abstract class StorageDao<T extends Entity<String>> implements Dao<T, String> {

	private final Logger logger;

	private final StorageService storageService;

	private final Provider<T> beanProvider;

	private final Mapper<T> mapper;

	@Inject
	public StorageDao(final Logger logger, final StorageService storageService, final Provider<T> beanProvider, final Mapper<T> mapper) {
		this.logger = logger;
		this.storageService = storageService;
		this.beanProvider = beanProvider;
		this.mapper = mapper;
	}

	@Override
	public void save(final T entity) throws StorageException {
		try {
			logger.trace("save");
			final Map<String, String> data = mapper.map(entity);
			for (final Entry<String, String> entry : data.entrySet()) {
				storageService.set(getColumnFamily(), entity.getId(), entry.getKey(), entry.getValue());
			}
		}
		catch (final MapException e) {
			throw new StorageException("MapException", e);
		}
	}

	protected abstract String getColumnFamily();

	@Override
	public void delete(final T entity) throws StorageException {
		try {
			logger.trace("delete");
			for (final String fieldName : getFieldNames(entity)) {
				storageService.delete(getColumnFamily(), entity.getId(), fieldName);
			}
		}
		catch (final MapException e) {
			throw new StorageException("MapException", e);
		}
	}

	@Override
	public T create() {
		logger.trace("create");
		return beanProvider.get();
	}

	@Override
	public T load(final String id) throws StorageException {
		try {
			logger.trace("load");
			final Map<String, String> data = new HashMap<String, String>();
			final T entity = create();
			for (final String fieldName : getFieldNames(entity)) {
				logger.trace("load fieldName: " + fieldName);
				final String value = storageService.get(getColumnFamily(), String.valueOf(id), fieldName);
				if ("id".equals(fieldName) && value == null) {
					return null;
				}
				logger.trace("found value " + fieldName + "=" + value);
				data.put(fieldName, value);
			}
			mapper.map(data, entity);
			return entity;
		}
		catch (final MapException e) {
			throw new StorageException("MapException load with id " + id + " failed]", e);
		}
	}

	@Override
	public Collection<T> getAll() {
		logger.trace("getAll");
		final Set<T> result = new HashSet<T>();
		final String c = getColumnFamily();
		try {
			for (final String id : storageService.list(c)) {
				result.add(load(id));
			}
		}
		catch (final StorageException e) {
			logger.trace("StorageException while listing columnfamily " + c, e);
		}
		return result;
	}

	protected List<String> getFieldNames(final T entity) throws MapException {
		return new ArrayList<String>(mapper.map(entity).keySet());
	}
}
