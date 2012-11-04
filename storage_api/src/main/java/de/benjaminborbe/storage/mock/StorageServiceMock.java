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
import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageIterator;
import de.benjaminborbe.storage.api.StorageService;

@Singleton
public class StorageServiceMock implements StorageService {

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

	@Inject
	public StorageServiceMock() {
	}

	@Override
	public String get(final String columnFamily, final String id, final String key) {
		final HashMap<String, HashMap<String, String>> cfData = storageData.get(columnFamily);
		if (cfData == null)
			return null;
		final HashMap<String, String> idData = cfData.get(id);
		if (idData == null)
			return null;
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
			idData.put(e.getKey(), e.getValue());
		}
	}

	@Override
	public StorageIterator findByIdPrefix(final String columnFamily, final String idStartWith) {

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
	public StorageIterator list(final String columnFamily) {
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
	public void delete(final String columnFamily, final String id, final String... keys) throws StorageException {
		delete(columnFamily, id, Arrays.asList(keys));
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
}
