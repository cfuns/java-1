package de.benjaminborbe.tools.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public abstract class DaoCache<E extends Entity<T>, T> implements Dao<E, T> {

	protected final Logger logger;

	private final Map<T, E> data = new HashMap<T, E>();

	private final Provider<E> provider;

	@Inject
	public DaoCache(final Logger logger, final Provider<E> provider) {
		this.logger = logger;
		this.provider = provider;
	}

	@Override
	public void save(final E entity) {
		logger.trace("save");
		if (entity.getId() == null) {
			throw new PrimaryKeyMissingException("primary-key field id is null!");
		}
		data.put(entity.getId(), entity);
	}

	@Override
	public void delete(final E entity) {
		logger.trace("delete");
		data.remove(entity.getId());
	}

	@Override
	public E load(final T id) {
		logger.trace("load");
		return data.get(id);
	}

	@Override
	public E create() {
		logger.trace("create");
		return provider.get();
	}

	@Override
	public Collection<E> getAll() {
		return data.values();
	}
}
