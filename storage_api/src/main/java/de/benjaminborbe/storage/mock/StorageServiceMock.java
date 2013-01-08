package de.benjaminborbe.storage.mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.storage.api.StorageColumnIterator;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageIterator;
import de.benjaminborbe.storage.api.StorageRow;
import de.benjaminborbe.storage.api.StorageRowIterator;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.api.StorageValue;

@Singleton
public class StorageServiceMock implements StorageService {

	private final String encoding = "UTF8";

	private final class StorageRowIteratorMock implements StorageRowIterator {

		private final Iterator<StorageValue> i;

		private final List<StorageValue> columnNames;

		private final String columnFamily;

		private StorageRow next;

		private final Map<StorageValue, StorageValue> where;

		private StorageRowIteratorMock(final Iterator<StorageValue> i, final List<StorageValue> columnNames, final String columnFamily, final Map<StorageValue, StorageValue> where) {
			this.i = i;
			this.columnNames = columnNames;
			this.columnFamily = columnFamily;
			this.where = where;
		}

		@Override
		public boolean hasNext() throws StorageException {
			try {
				if (next != null) {
					return true;
				}
				while (i.hasNext()) {
					final StorageValue key = i.next();
					final StorageRowMock row = new StorageRowMock(columnFamily, columnNames, key);
					boolean match = true;
					for (final Entry<StorageValue, StorageValue> e : where.entrySet()) {
						if (!e.getValue().equals(row.getValue(e.getKey()))) {
							final String msg = "where " + e.getValue() + " != " + row.getValue(e.getKey());
							logger.info(msg);
							match = false;
						}
					}
					if (match) {
						next = row;
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

	private final class StorageRowMock implements StorageRow {

		private final String columnFamily;

		private final List<StorageValue> columnNames;

		private final StorageValue key;

		private StorageRowMock(final String columnFamily, final List<StorageValue> columnNames, final StorageValue key) {
			logger.info("columnFamily: " + columnFamily + " columnNames: " + columnNames + " key: " + key);
			this.columnFamily = columnFamily;
			this.columnNames = columnNames;
			this.key = key;
		}

		@Override
		public StorageValue getValue(final StorageValue columnName) {
			return get(columnFamily, key, columnName);
		}

		@Override
		public StorageValue getKey() {
			return key;
		}

		@Override
		public Collection<StorageValue> getColumnNames() {
			return columnNames;
		}

	}

	private final class StorageIteratorImpl implements StorageIterator {

		private final Iterator<StorageValue> i;

		private StorageIteratorImpl(final Iterator<StorageValue> i) {
			this.i = i;
		}

		@Override
		public StorageValue next() throws StorageException {
			return i.next();
		}

		@Override
		public boolean hasNext() throws StorageException {
			return i.hasNext();
		}
	}

	protected final HashMap<String, HashMap<StorageValue, HashMap<StorageValue, StorageValue>>> storageData = new HashMap<String, HashMap<StorageValue, HashMap<StorageValue, StorageValue>>>();

	private final Logger logger;

	@Inject
	public StorageServiceMock(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public StorageValue get(final String columnFamily, final StorageValue id, final StorageValue key) {
		logger.info("get " + columnFamily + " " + id + " " + key);
		final HashMap<StorageValue, HashMap<StorageValue, StorageValue>> cfData = storageData.get(columnFamily);
		if (cfData == null)
			return null;
		final HashMap<StorageValue, StorageValue> idData = cfData.get(id);
		if (idData == null)
			return null;
		logger.info("get[" + id + "][" + key + "] = " + idData.get(key));
		return idData.get(key);
	}

	@Override
	public void delete(final String columnFamily, final StorageValue id, final StorageValue key) throws StorageException {
		delete(columnFamily, id, Arrays.asList(key));
	}

	@Override
	public void delete(final String columnFamily, final StorageValue id, final Collection<StorageValue> keys) {
		final HashMap<StorageValue, HashMap<StorageValue, StorageValue>> cfData = storageData.get(columnFamily);
		if (cfData == null)
			return;
		final HashMap<StorageValue, StorageValue> idData = cfData.get(id);
		if (idData == null)
			return;
		for (final StorageValue key : keys) {
			idData.remove(key);
		}
	}

	@Override
	public void set(final String columnFamily, final StorageValue id, final StorageValue key, final StorageValue value) {
		HashMap<StorageValue, HashMap<StorageValue, StorageValue>> cfData = storageData.get(columnFamily);
		if (cfData == null) {
			cfData = new HashMap<StorageValue, HashMap<StorageValue, StorageValue>>();
			storageData.put(columnFamily, cfData);
		}
		HashMap<StorageValue, StorageValue> idData = cfData.get(id);
		if (idData == null) {
			idData = new HashMap<StorageValue, StorageValue>();
			cfData.put(id, idData);
		}
		idData.put(key, value);
	}

	@Override
	public void set(final String columnFamily, final StorageValue id, final Map<StorageValue, StorageValue> data) {
		HashMap<StorageValue, HashMap<StorageValue, StorageValue>> cfData = storageData.get(columnFamily);
		if (cfData == null) {
			cfData = new HashMap<StorageValue, HashMap<StorageValue, StorageValue>>();
			storageData.put(columnFamily, cfData);
		}
		final HashMap<StorageValue, StorageValue> idData = new HashMap<StorageValue, StorageValue>();
		cfData.put(id, idData);
		for (final Entry<StorageValue, StorageValue> e : data.entrySet()) {
			logger.info("write " + e.getKey() + " " + e.getValue());
			idData.put(e.getKey(), e.getValue());
		}
	}

	@Override
	public StorageIterator keyIterator(final String columnFamily) {
		final HashMap<StorageValue, HashMap<StorageValue, StorageValue>> cfData = storageData.get(columnFamily);
		List<StorageValue> result;
		if (cfData == null) {
			result = new ArrayList<StorageValue>();
		}
		else {
			result = new ArrayList<StorageValue>(cfData.keySet());
		}
		final Iterator<StorageValue> i = result.iterator();
		return new StorageIteratorImpl(i);
	}

	@Override
	public List<StorageValue> get(final String columnFamily, final StorageValue id, final List<StorageValue> keys) throws StorageException {
		final List<StorageValue> result = new ArrayList<StorageValue>();
		for (final StorageValue key : keys) {
			result.add(get(columnFamily, id, key));
		}
		return result;
	}

	@Override
	public int getFreeConnections() {
		return 0;
	}

	@Override
	public int getConnections() {
		return 0;
	}

	@Override
	public int getMaxConnections() {
		return 0;
	}

	@Override
	public StorageIterator keyIterator(final String columnFamily, final Map<StorageValue, StorageValue> where) throws StorageException {
		return keyIterator(columnFamily);
	}

	@Override
	public StorageRowIterator rowIterator(final String columnFamily, final List<StorageValue> columnNames) throws StorageException {
		return rowIterator(columnFamily, columnNames, new HashMap<StorageValue, StorageValue>());
	}

	@Override
	public StorageRowIterator rowIterator(final String columnFamily, final List<StorageValue> columnNames, final Map<StorageValue, StorageValue> where) throws StorageException {
		final Iterator<StorageValue> i;
		if (storageData.containsKey(columnFamily)) {
			i = storageData.get(columnFamily).keySet().iterator();
		}
		else {
			i = new ArrayList<StorageValue>().iterator();
		}
		return new StorageRowIteratorMock(i, columnNames, columnFamily, where);
	}

	@Override
	public void backup() throws StorageException {
		throw new NotImplementedException();
	}

	@Override
	public void restore(final String columnfamily, final String jsonContent) throws StorageException {
		throw new NotImplementedException();
	}

	@Override
	public long count(final String columnFamily) throws StorageException {
		throw new NotImplementedException();
	}

	@Override
	public long count(final String columnFamily, final StorageValue columnName) throws StorageException {
		throw new NotImplementedException();
	}

	@Override
	public long count(final String columnFamily, final StorageValue columnName, final StorageValue columnValue) throws StorageException {
		throw new NotImplementedException();
	}

	@Override
	public void delete(final String columnFamily, final StorageValue id) throws StorageException {
		final HashMap<StorageValue, HashMap<StorageValue, StorageValue>> cfData = storageData.get(columnFamily);
		if (cfData == null)
			return;
		cfData.remove(id);
	}

	@Override
	public Map<StorageValue, StorageValue> get(final String columnFamily, final StorageValue key) throws StorageException {
		final HashMap<StorageValue, HashMap<StorageValue, StorageValue>> cfData = storageData.get(columnFamily);
		if (cfData == null)
			return null;
		return cfData.get(key);
	}

	@Override
	public StorageColumnIterator columnIterator(final String columnFamily, final StorageValue key) throws StorageException {
		return new StorageColumnIteratorMock(get(columnFamily, key));
	}

	@Override
	public String getEncoding() {
		return encoding;
	}
}
