package de.benjaminborbe.storage.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.api.Identifier;
import de.benjaminborbe.api.IdentifierBuilder;
import de.benjaminborbe.api.IdentifierBuilderException;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageIterator;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.tools.mapper.MapException;
import de.benjaminborbe.tools.mapper.Mapper;

@Singleton
public abstract class DaoStorage<E extends Entity<I>, I extends Identifier<String>> implements Dao<E, I> {

	private final class EntityIteratorImpl implements EntityIterator<E> {

		private final IdentifierIterator<I> i;

		private EntityIteratorImpl(final IdentifierIterator<I> i) {
			this.i = i;
		}

		@Override
		public boolean hasNext() throws EntityIteratorException {
			try {
				return i.hasNext();
			}
			catch (final IdentifierIteratorException e) {
				throw new EntityIteratorException(e);
			}
		}

		@Override
		public E next() throws EntityIteratorException {
			try {
				final I id = i.next();
				return load(id);
			}
			catch (final IdentifierIteratorException e) {
				throw new EntityIteratorException(e);
			}
			catch (final StorageException e) {
				throw new EntityIteratorException(e);
			}
		}
	}

	private final class IdentifierIteratorImpl implements IdentifierIterator<I> {

		private final StorageIterator i;

		private I next;

		private IdentifierIteratorImpl(final StorageIterator i) {
			this.i = i;
		}

		@Override
		public boolean hasNext() throws IdentifierIteratorException {
			try {
				if (next != null) {
					return true;
				}
				else {
					while (i.hasNext()) {
						final I id = identifierBuilder.buildIdentifier(i.nextString());
						if (exists(id)) {
							next = id;
							return true;
						}
					}
				}
				return false;
			}
			catch (final StorageException e) {
				throw new IdentifierIteratorException(e);
			}
			catch (final IdentifierBuilderException e) {
				throw new IdentifierIteratorException(e);
			}
		}

		@Override
		public I next() throws IdentifierIteratorException {
			if (hasNext()) {
				final I result = next;
				next = null;
				return result;
			}
			else {
				throw new NoSuchElementException();
			}
		}
	}

	private static final String ID_FIELD = "id";

	private final Logger logger;

	private final StorageService storageService;

	private final Provider<E> beanProvider;

	private final Mapper<E> mapper;

	private final IdentifierBuilder<String, I> identifierBuilder;

	@Inject
	public DaoStorage(
			final Logger logger,
			final StorageService storageService,
			final Provider<E> beanProvider,
			final Mapper<E> mapper,
			final IdentifierBuilder<String, I> identifierBuilder) {
		this.logger = logger;
		this.storageService = storageService;
		this.beanProvider = beanProvider;
		this.mapper = mapper;
		this.identifierBuilder = identifierBuilder;
	}

	@Override
	public void save(final E entity) throws StorageException {
		if (entity.getId() == null) {
			throw new StorageException("could not save without identifier");
		}
		try {
			logger.trace("save");
			final Map<String, String> data = mapper.map(entity);
			storageService.set(getColumnFamily(), entity.getId().getId(), data);
		}
		catch (final MapException e) {
			throw new StorageException("MapException", e);
		}
	}

	protected abstract String getColumnFamily();

	@Override
	public void delete(final I id) throws StorageException {
		delete(load(id));
	}

	@Override
	public void delete(final E entity) throws StorageException {
		try {
			logger.trace("delete");
			storageService.delete(getColumnFamily(), entity.getId().getId(), getFieldNames(entity));
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

	@Override
	public boolean exists(final I id) throws StorageException {
		return storageService.get(getColumnFamily(), id.getId(), ID_FIELD) != null;
	}

	protected E load(final String id) throws StorageException {
		try {
			logger.trace("load - id: " + id);
			final Map<String, String> data = new HashMap<String, String>();
			final E entity = create();
			if (storageService.get(getColumnFamily(), id, ID_FIELD) == null) {
				return null;
			}
			final List<String> fieldNames = getFieldNames(entity);
			final List<String> values = storageService.get(getColumnFamily(), id, fieldNames);
			final Iterator<String> fi = fieldNames.iterator();
			final Iterator<String> vi = values.iterator();
			while (fi.hasNext() && vi.hasNext()) {
				data.put(fi.next(), vi.next());
			}
			mapper.map(data, entity);
			return entity;
		}
		catch (final MapException e) {
			throw new StorageException("MapException load with id " + id + " failed]", e);
		}
	}

	protected List<String> getFieldNames(final E entity) throws MapException {
		return new ArrayList<String>(mapper.map(entity).keySet());
	}

	@Override
	public EntityIterator<E> getEntityIterator() throws StorageException {
		final IdentifierIterator<I> i = getIdentifierIterator();
		return new EntityIteratorImpl(i);
	}

	@Override
	public EntityIterator<E> getEntityIterator(final Map<String, String> where) throws StorageException {
		final IdentifierIterator<I> i = getIdentifierIterator(where);
		return new EntityIteratorImpl(i);
	}

	@Override
	public IdentifierIterator<I> getIdentifierIterator() throws StorageException {
		return new IdentifierIteratorImpl(storageService.list(getColumnFamily()));
	}

	@Override
	public IdentifierIterator<I> getIdentifierIterator(final Map<String, String> where) throws StorageException {
		return new IdentifierIteratorImpl(storageService.list(getColumnFamily(), where));
	}
}
