package de.benjaminborbe.storage.mock;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;

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

@Singleton
public class StorageServiceMock implements StorageService {

	private final class StorageRowIteratorMock implements StorageRowIterator {

		private final Iterator<String> i;

		private final List<String> columnNames;

		private final String columnFamily;

		private StorageRow next;

		private final Map<String, String> where;

		private StorageRowIteratorMock(final Iterator<String> i, final List<String> columnNames, final String columnFamily, final Map<String, String> where) {
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
					final String key = i.next();
					final StorageRowMock row = new StorageRowMock(columnFamily, columnNames, key);
					boolean match = true;
					for (final Entry<String, String> e : where.entrySet()) {
						if (!e.getValue().equals(row.getString(e.getKey()))) {
							final String msg = "where " + e.getValue() + " != " + row.getString(e.getKey());
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

	private final class StorageRowMock implements StorageRow {

		private final String columnFamily;

		private final List<String> columnNames;

		private final String key;

		private StorageRowMock(final String columnFamily, final List<String> columnNames, final String key) {
			logger.info("columnFamily: " + columnFamily + " columnNames: " + columnNames + " key: " + key);
			this.columnFamily = columnFamily;
			this.columnNames = columnNames;
			this.key = key;
		}

		@Override
		public String getString(final String columnName) throws UnsupportedEncodingException {
			return get(columnFamily, key, columnName);
		}

		@Override
		public String getKeyString() throws UnsupportedEncodingException {
			return key;
		}

		@Override
		public byte[] getKeyByte() {
			try {
				return key.getBytes("UTF8");
			}
			catch (final UnsupportedEncodingException e) {
				return null;
			}
		}

		@Override
		public Collection<String> getColumnNames() {
			return columnNames;
		}

		@Override
		public byte[] getByte(final String columnName) {
			try {
				return getString(columnName).getBytes("UTF8");
			}
			catch (final UnsupportedEncodingException e) {
				return null;
			}
		}
	}

	private final class StorageIteratorImpl implements StorageIterator {

		private final Iterator<String> i;

		private StorageIteratorImpl(final Iterator<String> i) {
			this.i = i;
		}

		@Override
		public String nextString() throws StorageException {
			return i.next();
		}

		@Override
		public byte[] nextByte() throws StorageException {
			try {
				return nextString().getBytes("UTF-8");
			}
			catch (final UnsupportedEncodingException e) {
				throw new StorageException(e);
			}
		}

		@Override
		public boolean hasNext() throws StorageException {
			return i.hasNext();
		}
	}

	protected final HashMap<String, HashMap<String, HashMap<String, String>>> storageData = new HashMap<String, HashMap<String, HashMap<String, String>>>();

	private final Logger logger;

	@Inject
	public StorageServiceMock(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public String get(final String columnFamily, final String id, final String key) {
		logger.info("get " + columnFamily + " " + id + " " + key);
		final HashMap<String, HashMap<String, String>> cfData = storageData.get(columnFamily);
		if (cfData == null)
			return null;
		final HashMap<String, String> idData = cfData.get(id);
		if (idData == null)
			return null;
		logger.info("get[" + id + "][" + key + "] = " + idData.get(key));
		return idData.get(key);
	}

	@Override
	public void delete(final String columnFamily, final String id, final String key) {
		delete(columnFamily, id, Arrays.asList(key));
	}

	@Override
	public void delete(final String columnFamily, final String id, final Collection<String> keys) {
		final HashMap<String, HashMap<String, String>> cfData = storageData.get(columnFamily);
		if (cfData == null)
			return;
		final HashMap<String, String> idData = cfData.get(id);
		if (idData == null)
			return;
		for (final String key : keys) {
			idData.remove(key);
		}
	}

	@Override
	public void set(final String columnFamily, final String id, final String key, final String value) {
		HashMap<String, HashMap<String, String>> cfData = storageData.get(columnFamily);
		if (cfData == null) {
			cfData = new HashMap<String, HashMap<String, String>>();
			storageData.put(columnFamily, cfData);
		}
		HashMap<String, String> idData = cfData.get(id);
		if (idData == null) {
			idData = new HashMap<String, String>();
			cfData.put(id, idData);
		}
		idData.put(key, value);
	}

	@Override
	public void set(final String columnFamily, final String id, final Map<String, String> data) {
		HashMap<String, HashMap<String, String>> cfData = storageData.get(columnFamily);
		if (cfData == null) {
			cfData = new HashMap<String, HashMap<String, String>>();
			storageData.put(columnFamily, cfData);
		}
		final HashMap<String, String> idData = new HashMap<String, String>();
		cfData.put(id, idData);
		for (final Entry<String, String> e : data.entrySet()) {
			logger.info("write " + e.getKey() + " " + e.getValue());
			idData.put(e.getKey(), e.getValue());
		}
	}

	@Override
	public StorageIterator keyIteratorWithPrefix(final String columnFamily, final String idStartWith) {

		final Set<String> result = new HashSet<String>();
		final HashMap<String, HashMap<String, String>> cfData = storageData.get(columnFamily);
		if (cfData == null)
			return null;
		for (final String id : cfData.keySet()) {
			if (id.startsWith(idStartWith)) {
				result.add(id);
			}
		}
		final Iterator<String> i = result.iterator();
		return new StorageIteratorImpl(i);
	}

	@Override
	public StorageIterator keyIterator(final String columnFamily) {
		final HashMap<String, HashMap<String, String>> cfData = storageData.get(columnFamily);
		List<String> result;
		if (cfData == null) {
			result = new ArrayList<String>();
		}
		else {
			result = new ArrayList<String>(cfData.keySet());
		}
		final Iterator<String> i = result.iterator();
		return new StorageIteratorImpl(i);
	}

	@Override
	public List<String> get(final String columnFamily, final String id, final List<String> keys) throws StorageException {
		final List<String> result = new ArrayList<String>();
		for (final String key : keys) {
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
	public StorageIterator keyIterator(final String columnFamily, final Map<String, String> where) throws StorageException {
		return keyIterator(columnFamily);
	}

	@Override
	public StorageRowIterator rowIterator(final String columnFamily, final List<String> columnNames) throws StorageException {
		return rowIterator(columnFamily, columnNames, new HashMap<String, String>());
	}

	@Override
	public StorageRowIterator rowIterator(final String columnFamily, final List<String> columnNames, final Map<String, String> where) throws StorageException {
		final Iterator<String> i;
		if (storageData.containsKey(columnFamily)) {
			i = storageData.get(columnFamily).keySet().iterator();
		}
		else {
			i = new ArrayList<String>().iterator();
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
	public long count(final String columnFamily, final String columnName) throws StorageException {
		throw new NotImplementedException();
	}

	@Override
	public long count(final String columnFamily, final String columnName, final String columnValue) throws StorageException {
		throw new NotImplementedException();
	}

	@Override
	public void delete(final String columnFamily, final String id) throws StorageException {
		final HashMap<String, HashMap<String, String>> cfData = storageData.get(columnFamily);
		if (cfData == null)
			return;
		cfData.remove(id);
	}

	@Override
	public Map<String, String> get(final String columnFamily, final String key) throws StorageException {
		final HashMap<String, HashMap<String, String>> cfData = storageData.get(columnFamily);
		if (cfData == null)
			return null;
		return cfData.get(key);
	}

	@Override
	public StorageColumnIterator columnIterator(final String columnFamily, final String key) throws StorageException {
		throw new NotImplementedException();
	}
}
