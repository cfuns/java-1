package de.benjaminborbe.storage.memory.service;

import de.benjaminborbe.storage.api.StorageColumnIterator;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageIterator;
import de.benjaminborbe.storage.api.StorageRowIterator;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.api.StorageValue;
import de.benjaminborbe.tools.util.DurationUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Singleton
public class StorageServiceImpl implements StorageService {

	private static final long DURATION_WARN = 200;

	private final Logger logger;

	@Inject
	public StorageServiceImpl(
		final Logger logger,
		final DurationUtil durationUtil
	) {
		this.logger = logger;
	}

	@Override
	public String getEncoding() {
		return null;
	}

	@Override
	public void backup() throws StorageException {
	}

	@Override
	public long count(final String columnFamily) throws StorageException {
		return 0;
	}

	@Override
	public long count(final String columnFamily, final StorageValue columnName) throws StorageException {
		return 0;
	}

	@Override
	public long count(
		final String columnFamily, final StorageValue columnName, final StorageValue columnValue
	) throws StorageException {
		return 0;
	}

	@Override
	public void delete(final String columnFamily, final StorageValue key) throws StorageException {
	}

	@Override
	public void delete(
		final String columnFamily, final StorageValue key, final Collection<StorageValue> columnNames
	) throws StorageException {
	}

	@Override
	public void delete(final String columnFamily, final StorageValue key, final StorageValue columnName) throws StorageException {
	}

	@Override
	public Map<StorageValue, StorageValue> get(final String columnFamily, final StorageValue key) throws StorageException {
		return null;
	}

	@Override
	public List<StorageValue> get(final String columnFamily, final StorageValue key, final List<StorageValue> columnNames) throws StorageException {
		return null;
	}

	@Override
	public Collection<List<StorageValue>> get(
		final String columnFamily, final Collection<StorageValue> key, final List<StorageValue> columnNames
	) throws StorageException {
		return null;
	}

	@Override
	public StorageValue get(final String columnFamily, final StorageValue key, final StorageValue columnName) throws StorageException {
		return null;
	}

	@Override
	public int getConnections() {
		return 0;
	}

	@Override
	public int getFreeConnections() {
		return 0;
	}

	@Override
	public int getMaxConnections() {
		return 0;
	}

	@Override
	public StorageColumnIterator columnIterator(final String columnFamily, final StorageValue key) throws StorageException {
		return null;
	}

	@Override
	public StorageColumnIterator columnIteratorReversed(final String columnFamily, final StorageValue storageValue) throws StorageException {
		return null;
	}

	@Override
	public StorageIterator keyIterator(final String columnFamily) throws StorageException {
		return null;
	}

	@Override
	public StorageIterator keyIterator(
		final String columnFamily, final Map<StorageValue, StorageValue> where
	) throws StorageException {
		return null;
	}

	@Override
	public void restore(final String columnfamily, final String jsonContent) throws StorageException {
	}

	@Override
	public StorageRowIterator rowIterator(final String columnFamily, final List<StorageValue> columnNames) throws StorageException {
		return null;
	}

	@Override
	public StorageRowIterator rowIterator(
		final String columnFamily, final List<StorageValue> columnNames, final Map<StorageValue, StorageValue> where
	) throws StorageException {
		return null;
	}

	@Override
	public void set(
		final String columnFamily, final StorageValue key, final Map<StorageValue, StorageValue> data
	) throws StorageException {
	}

	@Override
	public void set(
		final String columnFamily, final StorageValue key, final StorageValue columnName, final StorageValue columnValue
	) throws StorageException {
	}

	@Override
	public void backup(final String columnFamily) throws StorageException {
	}

	@Override
	public boolean isAvailable() {
		return false;
	}
}
