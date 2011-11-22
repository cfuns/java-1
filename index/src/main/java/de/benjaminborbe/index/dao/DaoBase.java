package de.benjaminborbe.index.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.index.util.IdGenerator;

@Singleton
public abstract class DaoBase<T extends Entity> implements Dao<T> {

	protected final Logger logger;

	private final Map<Long, T> data = new HashMap<Long, T>();

	private final IdGenerator idGenerator;

	private final Provider<T> provider;

	@Inject
	public DaoBase(final Logger logger, final IdGenerator idGenerator, final Provider<T> provider) {
		this.logger = logger;
		this.idGenerator = idGenerator;
		this.provider = provider;
	}

	protected abstract void init();

	@Override
	public void save(final T entity) {
		logger.debug("save");
		if (entity.getId() == null) {
			entity.setId(idGenerator.nextId());
		}
		data.put(entity.getId(), entity);
	}

	@Override
	public void delete(final T entity) {
		logger.debug("delete");
		data.remove(entity.getId());
	}

	@Override
	public T load(final long id) {
		logger.debug("load");
		return data.get(id);
	}

	@Override
	public T create() {
		logger.debug("create");
		return provider.get();
	}

	@Override
	public Collection<T> getAll() {
		return data.values();
	}
}
