package de.benjaminborbe.storage.tools;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
import de.benjaminborbe.storage.api.StorageValue;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.mapper.MapException;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapper;

@Singleton
public abstract class DaoStorage<E extends Entity<I>, I extends Identifier<String>> implements Dao<E, I> {

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
						final I id = identifierBuilder.buildIdentifier(i.next().toString());
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
					if (e.getValue(ID_FIELD) != null) {
						next = e;
						return true;
					}
				}
				return false;
			}
			finally {
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

	private final StorageValue ID_FIELD;

	private final Provider<E> beanProvider;

	private final CalendarUtil calendarUtil;

	private final IdentifierBuilder<String, I> identifierBuilder;

	private final Logger logger;

	private final MapObjectMapper<E> mapper;

	private final StorageService storageService;

	private final String encoding;

	@Inject
	public DaoStorage(
			final Logger logger,
			final StorageService storageService,
			final Provider<E> beanProvider,
			final MapObjectMapper<E> mapper,
			final IdentifierBuilder<String, I> identifierBuilder,
			final CalendarUtil calendarUtil) {
		this.logger = logger;
		this.storageService = storageService;
		this.beanProvider = beanProvider;
		this.mapper = mapper;
		this.identifierBuilder = identifierBuilder;
		this.calendarUtil = calendarUtil;
		this.encoding = storageService.getEncoding();
		ID_FIELD = new StorageValue("id", encoding);
	}

	public long count() throws StorageException {
		try {
			return storageService.count(getColumnFamily());
		}
		catch (final StorageException e) {
			throw new StorageException(e.getClass().getName(), e);
		}
	}

	public long count(final StorageValue columnName) throws StorageException {
		try {
			return storageService.count(getColumnFamily(), columnName);
		}
		catch (final StorageException e) {
			throw new StorageException(e.getClass().getName(), e);
		}
	}

	public long count(final StorageValue columnName, final StorageValue columnValue) throws StorageException {
		try {
			return storageService.count(getColumnFamily(), columnName, columnValue);
		}
		catch (final StorageException e) {
			throw new StorageException(e.getClass().getName(), e);
		}
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
			storageService.delete(getColumnFamily(), new StorageValue(entity.getId().getId(), encoding));
		}
		finally {
		}
	}

	@Override
	public void delete(final I id) throws StorageException {
		delete(load(id));
	}

	@Override
	public boolean exists(final I id) throws StorageException {
		return exists(new StorageValue(id.getId(), encoding));
	}

	private boolean exists(final StorageValue id) throws StorageException {
		final StorageValue storageValue = storageService.get(getColumnFamily(), id, ID_FIELD);
		return storageValue != null && !storageValue.isEmpty();
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
	public EntityIterator<E> getEntityIterator(final Map<StorageValue, StorageValue> where) throws StorageException {
		try {
			return new EntityIteratorRow(getRowIterator(where));
		}
		catch (final MapException e) {
			throw new StorageException(e);
		}
	}

	protected List<StorageValue> getFieldNames(final E entity) throws MapException {
		final List<StorageValue> result = new ArrayList<StorageValue>();
		for (final String f : mapper.map(entity).keySet()) {
			result.add(new StorageValue(f, encoding));
		}
		return result;
	}

	@Override
	public IdentifierIterator<I> getIdentifierIterator() throws StorageException {
		return new IdentifierIteratorImpl(storageService.keyIterator(getColumnFamily()));
	}

	@Override
	public IdentifierIterator<I> getIdentifierIterator(final Map<StorageValue, StorageValue> where) throws StorageException {
		return new IdentifierIteratorImpl(storageService.keyIterator(getColumnFamily(), where));
	}

	private StorageRowIterator getRowIterator() throws StorageException, MapException {
		return new StorageRowIteratorWithId(storageService.rowIterator(getColumnFamily(), getFieldNames(create())));
	}

	private StorageRowIterator getRowIterator(final Map<StorageValue, StorageValue> where) throws StorageException, MapException {
		return new StorageRowIteratorWithId(storageService.rowIterator(getColumnFamily(), getFieldNames(create()), where));
	}

	@Override
	public void load(final E entity, final Collection<StorageValue> fieldNames) throws StorageException {
		try {
			final Map<String, String> data = new HashMap<String, String>();
			final List<StorageValue> values = storageService.get(getColumnFamily(), new StorageValue(entity.getId().getId(), encoding), new ArrayList<StorageValue>(fieldNames));
			final Iterator<StorageValue> fi = fieldNames.iterator();
			final Iterator<StorageValue> vi = values.iterator();
			while (fi.hasNext() && vi.hasNext()) {
				final String fieldname = fi.next().getString();
				final String fieldvalue = vi.next().getString();
				data.put(fieldname, fieldvalue);
			}
			mapper.map(data, entity);
		}
		catch (final MapException e) {
			throw new StorageException(e);
		}
		catch (final UnsupportedEncodingException e) {
			throw new StorageException(e);
		}
	}

	@Override
	public E load(final I id) throws StorageException {
		try {
			if (id == null || id.getId() == null) {
				throw new NullPointerException("can't load without id");
			}
			if (!exists(id)) {
				return null;
			}
			final E entity = create();
			entity.setId(id);
			final List<StorageValue> fieldNames = getFieldNames(entity);
			load(entity, fieldNames);
			return entity;
		}
		catch (final MapException e) {
			throw new StorageException("MapException load with id " + id + " failed]", e);
		}
	}

	protected E rowToBean(final StorageRow row) throws UnsupportedEncodingException, MapException {
		final StorageValue id = row.getKey();
		logger.trace("load - id: " + id);
		final E entity = create();
		final Collection<StorageValue> columnNames = row.getColumnNames();
		final Map<String, String> data = new HashMap<String, String>();
		for (final StorageValue columnName : columnNames) {
			final StorageValue columnValue = row.getValue(columnName);
			data.put(columnName != null ? columnName.getString() : null, columnValue != null ? columnValue.getString() : null);
		}
		mapper.map(data, entity);
		return entity;
	}

	@Override
	public void save(final E entity) throws StorageException {
		save(entity, null);
	}

	@Override
	public void save(final E entity, final Collection<StorageValue> fieldNames) throws StorageException {
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

			final Map<StorageValue, StorageValue> data = new HashMap<StorageValue, StorageValue>();
			if (fieldNames == null) {
				for (final Entry<String, String> e : mapper.map(entity).entrySet()) {
					data.put(new StorageValue(e.getKey(), encoding), new StorageValue(e.getValue(), encoding));
				}
			}
			else {
				final List<String> fields = new ArrayList<String>();
				for (final StorageValue fieldName : fieldNames) {
					fields.add(fieldName.getString());
				}

				for (final Entry<String, String> e : mapper.map(entity, fields).entrySet()) {
					data.put(new StorageValue(e.getKey(), encoding), new StorageValue(e.getValue(), encoding));
				}
			}
			storageService.set(getColumnFamily(), new StorageValue(entity.getId().getId(), encoding), data);
		}
		catch (final MapException e) {
			throw new StorageException(e);
		}
		catch (final UnsupportedEncodingException e) {
			throw new StorageException(e);
		}
	}

	@Override
	public String getEncoding() {
		return encoding;
	}
}
