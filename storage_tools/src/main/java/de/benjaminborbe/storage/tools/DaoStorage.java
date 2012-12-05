package de.benjaminborbe.storage.tools;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
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
import de.benjaminborbe.storage.api.StorageRow;
import de.benjaminborbe.storage.api.StorageRowIterator;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.mapper.MapException;
import de.benjaminborbe.tools.mapper.Mapper;

@Singleton
public abstract class DaoStorage<E extends Entity<I>, I extends Identifier<String>> implements Dao<E, I> {

	private final class StorageRowIteratorWithId implements StorageRowIterator {

		private final StorageRowIterator i;

		private StorageRow next;

		private StorageRowIteratorWithId(final StorageRowIterator i) {
			this.i = i;
		}

		@Override
		public boolean hasNext() throws StorageException {
			try {
				if (next != null) {
					return true;
				}
				while (i.hasNext()) {
					final StorageRow e = i.next();
					if (e.getString(ID_FIELD) != null) {
						next = e;
						return true;
					}
				}
				return false;
			}
			catch (final UnsupportedEncodingException e) {
				throw new StorageException(e);
			}
		}

		@Override
		public StorageRow next() throws StorageException {
			if (hasNext()) {
				final StorageRow result = next;
				next = null;
				return result;
			}
			else {
				throw new NoSuchElementException();
			}
		}
	}

	private final class EntityIteratorRow implements EntityIterator<E> {

		private final StorageRowIterator r;

		private EntityIteratorRow(final StorageRowIterator r) {
			this.r = r;
		}

		@Override
		public boolean hasNext() throws EntityIteratorException {
			try {
				return r.hasNext();
			}
			catch (final StorageException e) {
				throw new EntityIteratorException(e);
			}
		}

		@Override
		public E next() throws EntityIteratorException {
			try {
				return rowToBean(r.next());
			}
			catch (final StorageException e) {
				throw new EntityIteratorException(e);
			}
			catch (final UnsupportedEncodingException e) {
				throw new EntityIteratorException(e);
			}
			catch (final MapException e) {
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

	private final Provider<E> beanProvider;

	private final IdentifierBuilder<String, I> identifierBuilder;

	private final Logger logger;

	private final Mapper<E> mapper;

	private final StorageService storageService;

	private final CalendarUtil calendarUtil;

	@Inject
	public DaoStorage(
			final Logger logger,
			final StorageService storageService,
			final Provider<E> beanProvider,
			final Mapper<E> mapper,
			final IdentifierBuilder<String, I> identifierBuilder,
			final CalendarUtil calendarUtil) {
		this.logger = logger;
		this.storageService = storageService;
		this.beanProvider = beanProvider;
		this.mapper = mapper;
		this.identifierBuilder = identifierBuilder;
		this.calendarUtil = calendarUtil;
	}

	@Override
	public E create() {
		logger.trace("create");
		return beanProvider.get();
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
	public void delete(final I id) throws StorageException {
		delete(load(id));
	}

	@Override
	public boolean exists(final I id) throws StorageException {
		return storageService.get(getColumnFamily(), id.getId(), ID_FIELD) != null;
	}

	protected abstract String getColumnFamily();

	@Override
	public EntityIterator<E> getEntityIterator() throws StorageException {
		try {
			return new EntityIteratorRow(getRowIterator());
		}
		catch (final MapException e) {
			throw new StorageException(e);
		}
	}

	@Override
	public EntityIterator<E> getEntityIterator(final Map<String, String> where) throws StorageException {
		try {
			return new EntityIteratorRow(getRowIterator(where));
		}
		catch (final MapException e) {
			throw new StorageException(e);
		}
	}

	protected List<String> getFieldNames(final E entity) throws MapException {
		return new ArrayList<String>(mapper.map(entity).keySet());
	}

	@Override
	public IdentifierIterator<I> getIdentifierIterator() throws StorageException {
		return new IdentifierIteratorImpl(storageService.keyIterator(getColumnFamily()));
	}

	@Override
	public IdentifierIterator<I> getIdentifierIterator(final Map<String, String> where) throws StorageException {
		return new IdentifierIteratorImpl(storageService.keyIterator(getColumnFamily(), where));
	}

	private StorageRowIterator getRowIterator() throws StorageException, MapException {
		return new StorageRowIteratorWithId(storageService.rowIterator(getColumnFamily(), getFieldNames(create())));
	}

	private StorageRowIterator getRowIterator(final Map<String, String> where) throws StorageException, MapException {
		return new StorageRowIteratorWithId(storageService.rowIterator(getColumnFamily(), getFieldNames(create()), where));
	}

	@Override
	public E load(final I id) throws StorageException {
		return load(id.getId());
	}

	protected E load(final String id) throws StorageException {
		try {
			if (id == null) {
				throw new NullPointerException("parameter id = null");
			}

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

	protected E rowToBean(final StorageRow row) throws UnsupportedEncodingException, MapException {
		final String id = row.getKeyString();
		logger.trace("load - id: " + id);
		final E entity = create();
		final Collection<String> columnNames = row.getColumnNames();
		final Map<String, String> data = new HashMap<String, String>();
		for (final String columnName : columnNames) {
			data.put(columnName, row.getString(columnName));
		}
		mapper.map(data, entity);
		return entity;
	}

	@Override
	public void save(final E entity) throws StorageException {
		if (entity.getId() == null) {
			throw new StorageException("could not save without identifier");
		}
		try {
			logger.debug("save " + entity.getClass().getName() + " " + entity.getId());

			if (entity instanceof HasModified) {
				final HasModified hasModified = (HasModified) entity;
				hasModified.setModified(calendarUtil.now());
			}
			if (entity instanceof HasCreated) {
				final HasCreated hasCreated = (HasCreated) entity;
				if (hasCreated.getCreated() == null) {
					hasCreated.setCreated(calendarUtil.now());
				}
			}

			final Map<String, String> data = mapper.map(entity);
			storageService.set(getColumnFamily(), entity.getId().getId(), data);
		}
		catch (final MapException e) {
			throw new StorageException("MapException", e);
		}
	}
}
