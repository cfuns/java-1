package de.benjaminborbe.storage.tools;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.api.Identifier;
import de.benjaminborbe.storage.api.StorageException;

@Singleton
public abstract class DaoCache<E extends Entity<? extends I>, I extends Identifier<?>> implements Dao<E, I> {

	private final class IdentifierIteratorImpl implements IdentifierIterator<I> {

		private final Iterator<I> iterator;

		public IdentifierIteratorImpl() {
			iterator = data.keySet().iterator();
		}

		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}

		@Override
		public I next() {
			return iterator.next();
		}
	}

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
	public boolean exists(final I id) throws StorageException {
		return data.containsKey(id);
	}

	@Override
	public EntityIterator<E> getEntityIterator() throws StorageException {
		return new EntityIteratorImpl<E>(data.values().iterator());
	}

	@Override
	public IdentifierIterator<I> getIdentifierIterator() throws StorageException {
		return new IdentifierIteratorImpl();
	}

	@Override
	public IdentifierIterator<I> getIdentifierIterator(final Map<String, String> where) throws StorageException {
		return getIdentifierIterator();
	}

	@Override
	public EntityIterator<E> getEntityIterator(final Map<String, String> where) throws StorageException {
		return getEntityIterator();
	}

	@Override
	public void load(final E entity, final Collection<String> fieldNames) throws StorageException {
		final E load = load(entity.getId());
		for (final String fieldName : fieldNames) {
			copyFieldValue(load, entity, fieldName);
		}
	}

	private void copyFieldValue(final E from, final E to, final String fieldname) {
		try {
			final Object value = PropertyUtils.getProperty(from, fieldname);
			PropertyUtils.setProperty(to, fieldname, value);
		}
		catch (final IllegalAccessException e) {
		}
		catch (final InvocationTargetException e) {
		}
		catch (final NoSuchMethodException e) {
		}
	}

	@Override
	public void save(final E entity, final Collection<String> fieldNames) throws StorageException {
		save(entity);
	}
}
