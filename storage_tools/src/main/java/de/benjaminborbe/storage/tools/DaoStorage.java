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

import de.benjaminborbe.api.Identifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.tools.mapper.MapException;
import de.benjaminborbe.tools.mapper.Mapper;

@Singleton
public abstract class DaoStorage<E extends Entity<I>, I extends Identifier<String>> implements Dao<E, I> {

	private final Logger logger;

	private final StorageService storageService;

	private final Provider<E> beanProvider;

	private final Mapper<E> mapper;

	@Inject
	public DaoStorage(final Logger logger, final StorageService storageService, final Provider<E> beanProvider, final Mapper<E> mapper) {
		this.logger = logger;
		this.storageService = storageService;
		this.beanProvider = beanProvider;
		this.mapper = mapper;
	}

	@Override
	public void save(final E entity) throws StorageException {
		if (entity.getId() == null) {
			throw new StorageException("could not save without identifier");
		}
		try {
			logger.trace("save");
			final Map<String, String> data = mapper.map(entity);
			for (final Entry<String, String> entry : data.entrySet()) {
				storageService.set(getColumnFamily(), entity.getId().getId(), entry.getKey(), entry.getValue());
			}
		}
		catch (final MapException e) {
			throw new StorageException("MapException", e);
		}
	}

	protected abstract String getColumnFamily();

	@Override
	public void delete(final E entity) throws StorageException {
		try {
			logger.trace("delete");
			for (final String fieldName : getFieldNames(entity)) {
				storageService.delete(getColumnFamily(), entity.getId().getId(), fieldName);
			}
		}
		catch (final MapException e) {
			throw new StorageException("MapException", e);
		}
	}

	@Override
	public E create() {
		logger.trace("create");
		return beanProvider.get();
	}

	@Override
	public E load(final I id) throws StorageException {
		return load(id.getId());
	}

	public E load(final String id) throws StorageException {
		try {
			logger.trace("load");
			final Map<String, String> data = new HashMap<String, String>();
			final E entity = create();
			for (final String fieldName : getFieldNames(entity)) {
				logger.trace("load fieldName: " + fieldName);
				final String value = storageService.get(getColumnFamily(), id, fieldName);
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
	public Collection<E> getAll() throws StorageException {
		logger.trace("getAll");
		final Set<E> result = new HashSet<E>();
		final String c = getColumnFamily();
		for (final String id : storageService.list(c)) {
			result.add(load(id));
		}
		return result;
	}

	protected List<String> getFieldNames(final E entity) throws MapException {
		return new ArrayList<String>(mapper.map(entity).keySet());
	}
}
