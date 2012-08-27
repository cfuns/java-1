package de.benjaminborbe.storage.tools;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.api.Identifier;
import de.benjaminborbe.storage.api.StorageException;

@Singleton
public abstract class DaoCache<E extends Entity<? extends I>, I extends Identifier<?>> implements Dao<E, I> {

	protected final Logger logger;

	private final Map<I, E> data = new HashMap<I, E>();

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
		delete(entity.getId());
	}

	@Override
	public void delete(final I id) {
		logger.trace("delete");
		data.remove(id);
	}

	@Override
	public E load(final I id) {
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

	@Override
	public Collection<I> getIdentifiers() throws StorageException {
		return data.keySet();
	}

	@Override
	public boolean exists(final I id) throws StorageException {
		return data.containsKey(id);
	}

}
