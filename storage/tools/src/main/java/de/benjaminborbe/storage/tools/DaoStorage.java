package de.benjaminborbe.storage.tools;

import com.google.inject.Provider;
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
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;

@Singleton
public abstract class DaoStorage<E extends Entity<I>, I extends Identifier<String>> implements Dao<E, I> {

	private final String ID_FIELD = "id";

	private final Provider<E> beanProvider;

	private final CalendarUtil calendarUtil;

	private final IdentifierBuilder<String, I> identifierBuilder;

	private final Logger logger;

	private final MapObjectMapper<E> mapper;

	private final StorageService storageService;

	@Inject
	public DaoStorage(
		final Logger logger,
		final StorageService storageService,
		final Provider<E> beanProvider,
		final MapObjectMapper<E> mapper,
		final IdentifierBuilder<String, I> identifierBuilder,
		final CalendarUtil calendarUtil
	) {
		this.logger = logger;
		this.storageService = storageService;
		this.beanProvider = beanProvider;
		this.mapper = mapper;
		this.identifierBuilder = identifierBuilder;
		this.calendarUtil = calendarUtil;
	}

	public long count() throws StorageException {
		try {
			return storageService.count(getColumnFamily());
		} catch (final StorageException e) {
			throw new StorageException(e.getClass().getName(), e);
		}
	}

	public long count(final StorageValue columnName) throws StorageException {
		try {
			return storageService.count(getColumnFamily(), columnName);
		} catch (final StorageException e) {
			throw new StorageException(e.getClass().getName(), e);
		}
	}

	public long count(final StorageValue columnName, final StorageValue columnValue) throws StorageException {
		try {
			return storageService.count(getColumnFamily(), columnName, columnValue);
		} catch (final StorageException e) {
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
		delete(entity.getId());
	}

	@Override
	public void delete(final I id) throws StorageException {
		logger.trace("delete");
		onPreDelete(id);
		storageService.delete(getColumnFamily(), new StorageValue(id.getId(), getEncoding()));
		onPostDelete(id);
	}

	public void onPostDelete(final I id) throws StorageException {
		logger.trace("onPostDelete - id: " + id);
	}

	public void onPreDelete(final I id) throws StorageException {
		logger.trace("onPreDelete - id: " + id);
	}

	public void onPostSave(final E entity, final Collection<StorageValue> fieldNames) throws StorageException {
		logger.trace("onPostSave - id: " + entity.getId() + " fieldNames: " + fieldNames);
	}

	public void onPreSave(final E entity, final Collection<StorageValue> fieldNames) throws StorageException {
		logger.trace("onPreSave - id: " + entity.getId() + " fieldNames: " + fieldNames);
	}

	@Override
	public boolean exists(final I id) throws StorageException {
		return exists(new StorageValue(id.getId(), getEncoding()));
	}

	private boolean exists(final StorageValue id) throws StorageException {
		final StorageValue storageValue = storageService.get(getColumnFamily(), id, new StorageValue(ID_FIELD, getEncoding()));
		return storageValue != null && !storageValue.isEmpty();
	}

	protected abstract String getColumnFamily();

	@Override
	public EntityIterator<E> getEntityIterator() throws StorageException {
		try {
			return new EntityIteratorRow(getRowIterator());
		} catch (final MapException e) {
			throw new StorageException(e);
		}
	}

	@Override
	public EntityIterator<E> getEntityIterator(final Map<StorageValue, StorageValue> where) throws StorageException {
		try {
			return new EntityIteratorRow(getRowIterator(where));
		} catch (final MapException e) {
			throw new StorageException(e);
		}
	}

	protected List<StorageValue> getFieldNames(final E entity) throws MapException {
		final List<StorageValue> result = new ArrayList<>();
		for (final String fieldName : mapper.map(entity).keySet()) {
			result.add(new StorageValue(fieldName, getEncoding()));
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
		final List<StorageValue> values = storageService.get(getColumnFamily(), new StorageValue(entity.getId().getId(), getEncoding()), new ArrayList<>(fieldNames));
		load(entity, fieldNames, values);
	}

	private void load(final E entity, final Collection<StorageValue> fieldNames, final List<StorageValue> values) throws StorageException {
		try {
			final Map<String, String> data = new HashMap<>();
			final Iterator<StorageValue> fi = fieldNames.iterator();
			final Iterator<StorageValue> vi = values.iterator();
			while (fi.hasNext() && vi.hasNext()) {
				final StorageValue fieldname = fi.next();
				final StorageValue fieldvalue = vi.next();
				data.put(fieldname.getString(), fieldvalue != null ? fieldvalue.getString() : null);
			}
			mapper.map(data, entity);
		} catch (final MapException | UnsupportedEncodingException e) {
			throw new StorageException(e);
		}
	}

	@Override
	public E load(final I id) throws StorageException {
		try {
			if (id == null || id.getId() == null) {
				throw new NullPointerException("can't load without id");
			}
			final E entity = create();
			final List<StorageValue> fieldNames = getFieldNames(entity);
			final List<StorageValue> values = storageService.get(getColumnFamily(), new StorageValue(id.getId(), getEncoding()), new ArrayList<>(fieldNames));
			load(entity, fieldNames, values);
			if (entity.getId() != null && entity.getId().getId() != null) {
				return entity;
			} else {
				return null;
			}
		} catch (final MapException e) {
			throw new StorageException("MapException load with id " + id + " failed]", e);
		}
	}

	protected E rowToBean(final StorageRow row) throws UnsupportedEncodingException, MapException {
		final StorageValue id = row.getKey();
		logger.trace("load - id: " + id);
		final E entity = create();
		final Collection<StorageValue> columnNames = row.getColumnNames();
		final Map<String, String> data = new HashMap<>();
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
		try {
			if (entity.getId() == null) {
				throw new StorageException("could not save without identifier");
			}

			logger.trace("save " + entity.getClass().getName() + " " + entity.getId());

			onPreSave(entity, fieldNames);

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

			final Map<StorageValue, StorageValue> data = new HashMap<>();
			if (fieldNames == null) {
				for (final Entry<String, String> e : mapper.map(entity).entrySet()) {
					data.put(new StorageValue(e.getKey(), getEncoding()), new StorageValue(e.getValue(), getEncoding()));
				}
			} else {
				final List<String> fields = new ArrayList<>();
				for (final StorageValue fieldName : fieldNames) {
					fields.add(fieldName.getString());
				}

				for (final Entry<String, String> e : mapper.map(entity, fields).entrySet()) {
					data.put(new StorageValue(e.getKey(), getEncoding()), new StorageValue(e.getValue(), getEncoding()));
				}
			}
			storageService.set(getColumnFamily(), new StorageValue(entity.getId().getId(), getEncoding()), data);

			onPostSave(entity, fieldNames);

		} catch (final MapException | UnsupportedEncodingException e) {
			throw new StorageException(e);
		}
	}

	@Override
	public String getEncoding() {
		return storageService.getEncoding();
	}

	@Override
	public StorageValue buildValue(final String content) {
		return new StorageValue(content, getEncoding());
	}

	@Override
	public Collection<E> load(final Collection<I> ids) throws StorageException {
		try {
			final Set<StorageValue> keys = new HashSet<>();
			for (final I id : ids) {
				keys.add(new StorageValue(id.getId(), getEncoding()));
			}
			final List<StorageValue> columnNames = getFieldNames(create());
			final Collection<List<StorageValue>> data = storageService.get(getColumnFamily(), keys, columnNames);
			final Set<E> result = new HashSet<>();
			for (final List<StorageValue> columnValues : data) {
				final E entity = create();
				load(entity, columnNames, columnValues);
				if (entity.getId() != null && entity.getId().getId() != null) {
					result.add(entity);
				}
			}
			return result;
		} catch (final MapException e) {
			throw new StorageException(e);
		}

	}

	@Override
	public E findOrCreate(final I id) throws StorageException {
		{
			final E entity = load(id);
			if (entity != null) {
				if (logger.isTraceEnabled())
					logger.trace("found entry with id: " + id);
				return entity;
			}
		}
		{
			final E entity = create();
			if (logger.isTraceEnabled())
				logger.trace("didn't found entry with id: " + id + " => create new entry");
			entity.setId(id);
			save(entity);
			return entity;
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
			} catch (final StorageException e) {
				throw new EntityIteratorException(e);
			}
		}

		@Override
		public E next() throws EntityIteratorException {
			try {
				return rowToBean(r.next());
			} catch (final StorageException | MapException | UnsupportedEncodingException e) {
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
				} else {
					while (i.hasNext()) {
						final I id = identifierBuilder.buildIdentifier(i.next().getString());
						if (exists(id)) {
							next = id;
							return true;
						}
					}
				}
				return false;
			} catch (final StorageException | UnsupportedEncodingException | IdentifierBuilderException e) {
				throw new IdentifierIteratorException(e);
			}
		}

		@Override
		public I next() throws IdentifierIteratorException {
			if (hasNext()) {
				final I result = next;
				next = null;
				return result;
			} else {
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
					final StorageValue value = e.getValue(new StorageValue(ID_FIELD, getEncoding()));
					if (value != null && !value.isEmpty()) {
						next = e;
						return true;
					}
				}
				return false;
			} finally {
			}
		}

		@Override
		public StorageRow next() throws StorageException {
			if (hasNext()) {
				final StorageRow result = next;
				next = null;
				return result;
			} else {
				throw new NoSuchElementException();
			}
		}
	}
}
