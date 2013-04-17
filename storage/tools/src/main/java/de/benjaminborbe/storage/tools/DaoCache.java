package de.benjaminborbe.storage.tools;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import de.benjaminborbe.api.Identifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageValue;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Singleton
public abstract class DaoCache<E extends Entity<I>, I extends Identifier<String>> implements Dao<E, I> {

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

	private final Map<I, E> data = new HashMap<>();

	private final Provider<E> provider;

	private final String encoding = "UTF8";

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
		return new EntityIteratorImpl<>(data.values().iterator());
	}

	@Override
	public IdentifierIterator<I> getIdentifierIterator() throws StorageException {
		return new IdentifierIteratorImpl();
	}

	@Override
	public IdentifierIterator<I> getIdentifierIterator(final Map<StorageValue, StorageValue> where) throws StorageException {
		return getIdentifierIterator();
	}

	@Override
	public EntityIterator<E> getEntityIterator(final Map<StorageValue, StorageValue> where) throws StorageException {
		return getEntityIterator();
	}

	@Override
	public void load(final E entity, final Collection<StorageValue> fieldNames) throws StorageException {
		try {
			final E load = load(entity.getId());
			for (final StorageValue fieldName : fieldNames) {
				copyFieldValue(load, entity, fieldName.getString());
			}
		} catch (final UnsupportedEncodingException e) {
			throw new StorageException(e);
		}
	}

	private void copyFieldValue(final E from, final E to, final String fieldname) {
		try {
			final Object value = PropertyUtils.getProperty(from, fieldname);
			PropertyUtils.setProperty(to, fieldname, value);
		} catch (final IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
		}
	}

	@Override
	public void save(final E entity, final Collection<StorageValue> fieldNames) throws StorageException {
		save(entity);
	}

	@Override
	public String getEncoding() {
		return encoding;
	}

	@Override
	public Collection<E> load(final Collection<I> ids) throws StorageException {
		final Set<E> result = new HashSet<>();
		for (final I id : ids) {
			result.add(load(id));
		}
		return result;
	}

	@Override
	public StorageValue buildValue(final String content) {
		return new StorageValue(content, getEncoding());
	}

	@Override
	public E findOrCreate(final I id) {
		{
			final E session = load(id);
			if (session != null) {
				return session;
			}
		}
		{
			final E session = create();
			session.setId(id);
			save(session);
			return session;
		}
	}
}
