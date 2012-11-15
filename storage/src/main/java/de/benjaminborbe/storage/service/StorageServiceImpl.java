package de.benjaminborbe.storage.service;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.cassandra.thrift.NotFoundException;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageIterator;
import de.benjaminborbe.storage.api.StorageRowIterator;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.util.StorageConfig;
import de.benjaminborbe.storage.util.StorageConnectionPool;
import de.benjaminborbe.storage.util.StorageDaoUtil;

@Singleton
public class StorageServiceImpl implements StorageService {

	private final class StorageIteratorPrefix implements StorageIterator {

		private final String idPrefix;

		private final StorageIterator i;

		private String nextString = null;

		private StorageIteratorPrefix(final String idPrefix, final StorageIterator i) {
			this.idPrefix = idPrefix;
			this.i = i;
		}

		@Override
		public boolean hasNext() throws StorageException {
			if (nextString != null) {
				return true;
			}
			while (i.hasNext()) {
				final String id = i.nextString();
				if (id.startsWith(idPrefix)) {
					nextString = id;
					return true;
				}
			}
			return false;
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
		public String nextString() throws StorageException {
			if (hasNext()) {
				final String result = nextString;
				nextString = null;
				return result;
			}
			return null;
		}
	}

	private final StorageConfig config;

	private final StorageDaoUtil storageDaoUtil;

	private final Logger logger;

	private final StorageConnectionPool storageConnectionPool;

	@Inject
	public StorageServiceImpl(final Logger logger, final StorageConfig config, final StorageDaoUtil storageDaoUtil, final StorageConnectionPool storageConnectionPool) {
		this.logger = logger;
		this.config = config;
		this.storageDaoUtil = storageDaoUtil;
		this.storageConnectionPool = storageConnectionPool;
	}

	@Override
	public String get(final String columnFamily, final String id, final String key) throws StorageException {
		try {
			return storageDaoUtil.read(config.getKeySpace(), columnFamily, id, key);
		}
		catch (final NotFoundException e) {
			return null;
		}
		catch (final Exception e) {
			logger.trace("Exception", e);
			throw new StorageException(e);
		}
	}

	@Override
	public void delete(final String columnFamily, final String id, final String key) throws StorageException {
		delete(columnFamily, id, Arrays.asList(key));
	}

	@Override
	public void set(final String columnFamily, final String id, final String key, final String value) throws StorageException {
		final Map<String, String> data = new HashMap<String, String>();
		data.put(key, value);
		set(columnFamily, id, data);
	}

	@Override
	public void set(final String columnFamily, final String id, final Map<String, String> data) throws StorageException {
		try {
			storageDaoUtil.insert(config.getKeySpace(), columnFamily, id, data);
		}
		catch (final Exception e) {
			logger.trace(e.getClass().getSimpleName(), e);
			throw new StorageException(e);
		}
	}

	@Override
	public StorageIterator keyIteratorWithPrefix(final String columnFamily, final String idPrefix) throws StorageException {
		try {
			final StorageIterator i = storageDaoUtil.keyIterator(config.getKeySpace(), columnFamily);
			return new StorageIteratorPrefix(idPrefix, i);
		}
		catch (final Exception e) {
			logger.trace("Exception", e);
			throw new StorageException(e);
		}
	}

	@Override
	public StorageIterator keyIterator(final String columnFamily) throws StorageException {
		try {
			return storageDaoUtil.keyIterator(config.getKeySpace(), columnFamily);
		}
		catch (final Exception e) {
			logger.trace("Exception", e);
			throw new StorageException(e);
		}
	}

	@Override
	public StorageIterator keyIterator(final String columnFamily, final Map<String, String> where) throws StorageException {
		try {
			return storageDaoUtil.keyIterator(config.getKeySpace(), columnFamily, where);
		}
		catch (final Exception e) {
			logger.trace("Exception", e);
			throw new StorageException(e);
		}
	}

	@Override
	public void delete(final String columnFamily, final String id, final Collection<String> keys) throws StorageException {
		try {
			for (final String key : keys) {
				try {
					storageDaoUtil.delete(config.getKeySpace(), columnFamily, id, key);
				}
				catch (final NotFoundException e) {
					// nop, already deleted
				}
			}
		}
		catch (final Exception e) {
			logger.trace("Exception", e);
			throw new StorageException(e);
		}
	}

	@Override
	public void delete(final String columnFamily, final String id, final String... keys) throws StorageException {
		delete(columnFamily, id, Arrays.asList(keys));
	}

	@Override
	public List<String> get(final String columnFamily, final String id, final List<String> keys) throws StorageException {
		try {
			return storageDaoUtil.read(config.getKeySpace(), columnFamily, id, keys);
		}
		catch (final NotFoundException e) {
			return null;
		}
		catch (final Exception e) {
			logger.trace("Exception", e);
			throw new StorageException(e);
		}
	}

	@Override
	public int getFreeConnections() {
		return storageConnectionPool.getFreeConnections();
	}

	@Override
	public int getConnections() {
		return storageConnectionPool.getConnections();
	}

	@Override
	public int getMaxConnections() {
		return storageConnectionPool.getMaxConnections();
	}

	@Override
	public StorageRowIterator rowIterator(final String columnFamily, final List<String> columnNames) throws StorageException {
		try {
			return storageDaoUtil.rowIterator(config.getKeySpace(), columnFamily, columnNames);
		}
		catch (final Exception e) {
			logger.trace("Exception", e);
			throw new StorageException(e);
		}
	}

	@Override
	public StorageRowIterator rowIterator(final String columnFamily, final List<String> columnNames, final Map<String, String> where) throws StorageException {
		try {
			return storageDaoUtil.rowIterator(config.getKeySpace(), columnNames, columnFamily, where);
		}
		catch (final Exception e) {
			logger.trace("Exception", e);
			throw new StorageException(e);
		}
	}
}
