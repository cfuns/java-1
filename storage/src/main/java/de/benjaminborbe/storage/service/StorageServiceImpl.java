package de.benjaminborbe.storage.service;

import java.io.File;
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
import de.benjaminborbe.storage.util.StorageExporter;
import de.benjaminborbe.storage.util.StorageImporter;
import de.benjaminborbe.tools.util.Duration;
import de.benjaminborbe.tools.util.DurationUtil;

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

	private final StorageExporter storageExporter;

	private final DurationUtil durationUtil;

	private final StorageImporter storageImporter;

	@Inject
	public StorageServiceImpl(
			final Logger logger,
			final StorageConfig config,
			final StorageDaoUtil storageDaoUtil,
			final StorageConnectionPool storageConnectionPool,
			final StorageExporter storageExporter,
			final StorageImporter storageImporter,
			final DurationUtil durationUtil) {
		this.logger = logger;
		this.config = config;
		this.storageDaoUtil = storageDaoUtil;
		this.storageConnectionPool = storageConnectionPool;
		this.storageExporter = storageExporter;
		this.storageImporter = storageImporter;
		this.durationUtil = durationUtil;
	}

	@Override
	public String get(final String columnFamily, final String id, final String key) throws StorageException {
		final Duration duration = durationUtil.getDuration();
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
		finally {
			logger.trace("duration " + duration.getTime());
		}
	}

	@Override
	public void delete(final String columnFamily, final String id, final String key) throws StorageException {
		final Duration duration = durationUtil.getDuration();
		try {
			delete(columnFamily, id, Arrays.asList(key));
		}
		finally {
			logger.trace("duration " + duration.getTime());
		}
	}

	@Override
	public void set(final String columnFamily, final String id, final String key, final String value) throws StorageException {
		final Duration duration = durationUtil.getDuration();
		try {
			final Map<String, String> data = new HashMap<String, String>();
			data.put(key, value);
			set(columnFamily, id, data);
		}
		finally {
			logger.trace("duration " + duration.getTime());
		}
	}

	@Override
	public void set(final String columnFamily, final String id, final Map<String, String> data) throws StorageException {
		final Duration duration = durationUtil.getDuration();
		try {
			storageDaoUtil.insert(config.getKeySpace(), columnFamily, id, data);
		}
		catch (final Exception e) {
			logger.trace(e.getClass().getSimpleName(), e);
			throw new StorageException(e);
		}
		finally {
			logger.trace("duration " + duration.getTime());
		}
	}

	@Override
	public StorageIterator keyIteratorWithPrefix(final String columnFamily, final String idPrefix) throws StorageException {
		final Duration duration = durationUtil.getDuration();
		try {
			final StorageIterator i = storageDaoUtil.keyIterator(config.getKeySpace(), columnFamily);
			return new StorageIteratorPrefix(idPrefix, i);
		}
		catch (final Exception e) {
			logger.trace("Exception", e);
			throw new StorageException(e);
		}
		finally {
			logger.trace("duration " + duration.getTime());
		}
	}

	@Override
	public StorageIterator keyIterator(final String columnFamily) throws StorageException {
		final Duration duration = durationUtil.getDuration();
		try {
			return storageDaoUtil.keyIterator(config.getKeySpace(), columnFamily);
		}
		catch (final Exception e) {
			logger.trace("Exception", e);
			throw new StorageException(e);
		}
		finally {
			logger.trace("duration " + duration.getTime());
		}
	}

	@Override
	public StorageIterator keyIterator(final String columnFamily, final Map<String, String> where) throws StorageException {
		final Duration duration = durationUtil.getDuration();
		try {
			return storageDaoUtil.keyIterator(config.getKeySpace(), columnFamily, where);
		}
		catch (final Exception e) {
			throw new StorageException(e);
		}
		finally {
			logger.trace("duration " + duration.getTime());
		}
	}

	@Override
	public void delete(final String columnFamily, final String id, final Collection<String> keys) throws StorageException {
		final Duration duration = durationUtil.getDuration();
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
		finally {
			logger.trace("duration " + duration.getTime());
		}
	}

	@Override
	public void delete(final String columnFamily, final String id) throws StorageException {
		final Duration duration = durationUtil.getDuration();
		try {
			storageDaoUtil.delete(config.getKeySpace(), columnFamily, id);
		}
		catch (final Exception e) {
			throw new StorageException(e);
		}
		finally {
			logger.trace("duration " + duration.getTime());
		}
	}

	@Override
	public List<String> get(final String columnFamily, final String id, final List<String> keys) throws StorageException {
		final Duration duration = durationUtil.getDuration();
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
		finally {
			logger.trace("duration " + duration.getTime());
		}
	}

	@Override
	public int getFreeConnections() {
		final Duration duration = durationUtil.getDuration();
		try {
			return storageConnectionPool.getFreeConnections();
		}
		finally {
			logger.trace("duration " + duration.getTime());
		}
	}

	@Override
	public int getConnections() {
		final Duration duration = durationUtil.getDuration();
		try {
			return storageConnectionPool.getConnections();
		}
		finally {
			logger.trace("duration " + duration.getTime());
		}
	}

	@Override
	public int getMaxConnections() {
		final Duration duration = durationUtil.getDuration();
		try {
			return storageConnectionPool.getMaxConnections();
		}
		finally {
			logger.trace("duration " + duration.getTime());
		}
	}

	@Override
	public StorageRowIterator rowIterator(final String columnFamily, final List<String> columnNames) throws StorageException {
		final Duration duration = durationUtil.getDuration();
		try {
			return storageDaoUtil.rowIterator(config.getKeySpace(), columnFamily, columnNames);
		}
		catch (final Exception e) {
			logger.trace("Exception", e);
			throw new StorageException(e);
		}
		finally {
			logger.trace("duration " + duration.getTime());
		}
	}

	@Override
	public StorageRowIterator rowIterator(final String columnFamily, final List<String> columnNames, final Map<String, String> where) throws StorageException {
		final Duration duration = durationUtil.getDuration();
		try {
			return storageDaoUtil.rowIterator(config.getKeySpace(), columnNames, columnFamily, where);
		}
		catch (final Exception e) {
			logger.trace("Exception", e);
			throw new StorageException(e);
		}
		finally {
			logger.trace("duration " + duration.getTime());
		}
	}

	@Override
	public void backup() throws StorageException {
		final Duration duration = durationUtil.getDuration();
		try {
			final File targetDirectory = new File(config.getBackpuDirectory());
			storageExporter.export(targetDirectory, config.getKeySpace());
		}
		catch (final Exception e) {
			logger.trace("Exception", e);
			throw new StorageException(e);
		}
		finally {
			logger.trace("duration " + duration.getTime());
		}
	}

	@Override
	public void restore(final String columnfamily, final String jsonContent) throws StorageException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.info("restore - columnfamily: " + columnfamily + " jsonContent: " + jsonContent);

			storageImporter.importJson(config.getKeySpace(), columnfamily, jsonContent);
		}
		catch (final Exception e) {
			logger.trace("Exception", e);
			throw new StorageException(e);
		}
		finally {
			logger.trace("duration " + duration.getTime());
		}
	}

	@Override
	public long count(final String columnFamily) throws StorageException {
		final Duration duration = durationUtil.getDuration();
		try {
			return storageDaoUtil.count(config.getKeySpace(), columnFamily);
		}
		catch (final Exception e) {
			logger.trace("Exception", e);
			throw new StorageException(e);
		}
		finally {
			logger.trace("duration " + duration.getTime());
		}
	}

	@Override
	public long count(final String columnFamily, final String columnName) throws StorageException {
		final Duration duration = durationUtil.getDuration();
		try {
			return storageDaoUtil.count(config.getKeySpace(), columnFamily, columnName);
		}
		catch (final Exception e) {
			logger.trace("Exception", e);
			throw new StorageException(e);
		}
		finally {
			logger.trace("duration " + duration.getTime());
		}
	}

	@Override
	public long count(final String columnFamily, final String columnName, final String columnValue) throws StorageException {
		final Duration duration = durationUtil.getDuration();
		try {
			return storageDaoUtil.count(config.getKeySpace(), columnFamily, columnName, columnValue);
		}
		catch (final Exception e) {
			logger.trace("Exception", e);
			throw new StorageException(e);
		}
		finally {
			logger.trace("duration " + duration.getTime());
		}
	}

}
