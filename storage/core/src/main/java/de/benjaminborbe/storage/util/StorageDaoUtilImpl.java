package de.benjaminborbe.storage.util;

import de.benjaminborbe.storage.api.StorageColumnIterator;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageIterator;
import de.benjaminborbe.storage.api.StorageRow;
import de.benjaminborbe.storage.api.StorageRowIterator;
import de.benjaminborbe.storage.api.StorageValue;
import de.benjaminborbe.storage.config.StorageConfig;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.map.MapChain;
import org.apache.cassandra.thrift.Cassandra.Iface;
import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.ColumnOrSuperColumn;
import org.apache.cassandra.thrift.ColumnParent;
import org.apache.cassandra.thrift.ColumnPath;
import org.apache.cassandra.thrift.ConsistencyLevel;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.SlicePredicate;
import org.apache.cassandra.thrift.SliceRange;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class StorageDaoUtilImpl implements StorageDaoUtil {

	private final CalendarUtil calendarUtil;

	private final StorageConfig config;

	private final Logger logger;

	private final StorageConnectionPool storageConnectionPool;

	@Inject
	public StorageDaoUtilImpl(
		final StorageConnectionPool storageConnectionPool,
		final StorageConfig config,
		final Logger logger,
		final CalendarUtil calendarUtil
	) {
		this.storageConnectionPool = storageConnectionPool;
		this.config = config;
		this.logger = logger;
		this.calendarUtil = calendarUtil;
	}

	@Override
	public long count(
		final String keySpace,
		final String columnFamily
	) throws UnsupportedEncodingException, InvalidRequestException, UnavailableException, TimedOutException,
		TException, NotFoundException, StorageException {
		long result = 0;
		final StorageIterator i = keyIterator(keySpace, columnFamily);
		while (i.hasNext()) {
			i.next();
			result++;
		}
		return result;
	}

	@Override
	public long count(
		final String keySpace,
		final String columnFamily,
		final StorageValue columnName
	) throws UnsupportedEncodingException, InvalidRequestException,
		UnavailableException, TimedOutException, TException, NotFoundException, StorageException {
		long result = 0;
		final StorageRowIterator i = rowIterator(keySpace, columnFamily, Arrays.asList(columnName));
		while (i.hasNext()) {
			final StorageRow row = i.next();
			final StorageValue value = row.getValue(columnName);
			if (value != null && !value.isEmpty()) {
				result++;
			}
		}
		return result;
	}

	@Override
	public long count(
		final String keySpace,
		final String columnFamily,
		final StorageValue columnName,
		final StorageValue columnValue
	) throws UnsupportedEncodingException,
		InvalidRequestException, UnavailableException, TimedOutException, TException, NotFoundException, StorageException, SocketException, StorageConnectionPoolException {
		long result = 0;
		final StorageRowIterator i = rowIterator(keySpace, columnFamily, Arrays.asList(columnName));
		while (i.hasNext()) {
			final StorageRow row = i.next();
			final StorageValue value = row.getValue(columnName);
			if (value != null && value.equals(columnValue)) {
				result++;
			}
		}
		return result;
	}

	@Override
	public void delete(
		final String keySpace,
		final String columnFamily,
		final StorageValue id,
		final Collection<StorageValue> columnNames
	) throws InvalidRequestException,
		NotFoundException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException, SocketException, StorageConnectionPoolException {
		StorageConnection connection = null;
		try {
			connection = storageConnectionPool.getConnection();
			final Iface client = connection.getClient(keySpace);

			logger.trace("delete keyspace: " + keySpace + " columnFamily: " + columnFamily + " key: " + id + " columnNames: " + columnNames);

			final ByteBuffer key = ByteBuffer.wrap(id.getByte());
			final ConsistencyLevel consistency_level = ConsistencyLevel.ONE;

			for (final StorageValue columnName : columnNames) {
				final ColumnPath column_path = new ColumnPath(columnFamily);
				column_path.setColumn(columnName.getByte());
				final ColumnOrSuperColumn column = client.get(key, column_path, consistency_level);
				client.remove(key, column_path, column.getColumn().getTimestamp(), consistency_level);
			}
		} finally {
			storageConnectionPool.releaseConnection(connection);
		}
	}

	@Override
	public void delete(
		final String keySpace,
		final String columnFamily,
		final StorageValue key,
		final StorageValue columnName
	) throws InvalidRequestException, NotFoundException,
		UnavailableException, TimedOutException, TException, UnsupportedEncodingException, SocketException, StorageConnectionPoolException {
		delete(keySpace, columnFamily, key, Arrays.asList(columnName));
	}

	@Override
	public void insert(final String keySpace, final String columnFamily, final StorageValue id, final StorageValue columnName, final StorageValue columnValue)
		throws InvalidRequestException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException, NotFoundException, SocketException,
		StorageConnectionPoolException {
		insert(keySpace, columnFamily, id, new MapChain<StorageValue, StorageValue>().add(columnName, columnValue));
	}

	@Override
	public void insert(
		final String keySpace,
		final String columnFamily,
		final StorageValue key,
		final Map<StorageValue, StorageValue> data
	) throws InvalidRequestException,
		UnavailableException, TimedOutException, TException, UnsupportedEncodingException, NotFoundException, SocketException, StorageConnectionPoolException {

		StorageConnection connection = null;
		try {
			connection = storageConnectionPool.getConnection();
			final Iface client = connection.getClient(keySpace);

			logger.trace("insert keyspace: " + keySpace + " columnFamily: " + columnFamily + " key: " + key + " data: " + data);

			final long timestamp = getCurrentTimestamp();
			for (final Entry<StorageValue, StorageValue> e : data.entrySet()) {
				final StorageValue columnName = e.getKey();
				final StorageValue columnValue = e.getValue();
				// logger.debug("write " + key + " = " + value);
				if (columnValue != null && !columnValue.isEmpty()) {

					final ColumnParent column_parent = new ColumnParent(columnFamily);

					final Column column = new Column(ByteBuffer.wrap(columnName.getByte()));
					column.setValue(ByteBuffer.wrap(columnValue.getByte()));
					column.setTimestamp(timestamp);
					final ConsistencyLevel consistency_level = ConsistencyLevel.ONE;

					// schreiben eines datensatzes
					client.insert(ByteBuffer.wrap(key.getByte()), column_parent, column, consistency_level);
				} else {
					try {
						delete(keySpace, columnFamily, key, e.getKey());
					} catch (final NotFoundException e1) {
						// do nothing
					}
				}
			}
		} finally {
			storageConnectionPool.releaseConnection(connection);
		}
	}

	private long getCurrentTimestamp() {
		return calendarUtil.getTime() * 1000;
	}

	@Override
	public StorageIterator keyIterator(
		final String keySpace,
		final String columnFamily
	) throws InvalidRequestException, UnavailableException, TimedOutException, TException,
		UnsupportedEncodingException, NotFoundException {
		return new StorageKeyIteratorImpl(storageConnectionPool, keySpace, columnFamily, config.getEncoding());
	}

	@Override
	public StorageIterator keyIterator(
		final String keySpace,
		final String columnFamily,
		final Map<StorageValue, StorageValue> where
	) throws InvalidRequestException,
		UnavailableException, TimedOutException, TException, UnsupportedEncodingException, NotFoundException, StorageConnectionPoolException {
		return new StorageKeyIteratorWhere(storageConnectionPool, keySpace, columnFamily, config.getEncoding(), where);
	}

	@Override
	public List<StorageValue> read(
		final String keySpace,
		final String columnFamily,
		final StorageValue key,
		final List<StorageValue> columnNames
	) throws InvalidRequestException,
		NotFoundException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException, SocketException, StorageConnectionPoolException {

		if (key == null || key.isEmpty()) {
			logger.info("can't read with null id");
			return null;
		}

		StorageConnection connection = null;
		try {
			connection = storageConnectionPool.getConnection();
			final Iface client = connection.getClient(keySpace);

			logger.trace("read keyspace: " + keySpace + " columnFamily: " + columnFamily + " key: " + key + " columnNames: " + columnNames);

			final ConsistencyLevel consistency_level = ConsistencyLevel.ONE;
			final ColumnParent column_parent = new ColumnParent(columnFamily);

			final SlicePredicate predicate = new SlicePredicate();
			predicate.setColumn_names(buildByteBufferList(columnNames));
			final List<ColumnOrSuperColumn> columns = client.get_slice(ByteBuffer.wrap(key.getByte()), column_parent, predicate, consistency_level);

			final Map<StorageValue, StorageValue> data = new HashMap<>();
			for (final ColumnOrSuperColumn column : columns) {
				final StorageValue name = new StorageValue(column.getColumn().getName(), config.getEncoding());
				final StorageValue value = new StorageValue(column.getColumn().getValue(), config.getEncoding());
				data.put(name, value);
			}

			final List<StorageValue> result = new ArrayList<>();

			for (final StorageValue columnName : columnNames) {
				final StorageValue value = data.get(columnName);
				if (value != null) {
					result.add(value);
				} else {
					result.add(new StorageValue());
				}
			}

			return result;
		} finally {
			storageConnectionPool.releaseConnection(connection);
		}
	}

	private List<ByteBuffer> buildByteBufferList(final Collection<StorageValue> columnNames) throws UnsupportedEncodingException {
		final List<ByteBuffer> result = new ArrayList<>();
		for (final StorageValue columnName : columnNames) {
			result.add(ByteBuffer.wrap(columnName.getByte()));
		}
		return result;
	}

	@Override
	public StorageValue read(
		final String keySpace,
		final String columnFamily,
		final StorageValue key,
		final StorageValue columnName
	) throws InvalidRequestException,
		NotFoundException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException, SocketException, StorageConnectionPoolException {

		if (key == null || key.isEmpty()) {
			logger.info("can't read with null id");
			return null;
		}

		StorageConnection connection = null;
		try {
			connection = storageConnectionPool.getConnection();
			final Iface client = connection.getClient(keySpace);

			logger.trace("read keyspace: " + keySpace + " columnFamily: " + columnFamily + " key: " + key + " columnName: " + columnName);
			final ColumnPath column_path = new ColumnPath(columnFamily);
			column_path.setColumn(columnName.getByte());
			final ConsistencyLevel consistency_level = ConsistencyLevel.ONE;
			try {
				final ColumnOrSuperColumn column = client.get(ByteBuffer.wrap(key.getByte()), column_path, consistency_level);
				return new StorageValue(column.getColumn().getValue(), config.getEncoding());
			} catch (final NotFoundException e) {
				return new StorageValue();
			}
		} finally {
			storageConnectionPool.releaseConnection(connection);
		}
	}

	@Override
	public StorageRowIterator rowIterator(final String keySpace, final String columnFamily, final List<StorageValue> columnNames) throws InvalidRequestException,
		UnavailableException, TimedOutException, TException, UnsupportedEncodingException, NotFoundException {
		return new StorageRowIteratorImpl(storageConnectionPool, keySpace, columnFamily, config.getEncoding(), columnNames);
	}

	@Override
	public StorageRowIterator rowIterator(
		final String keySpace,
		final String columnFamily,
		final List<StorageValue> columnNames,
		final Map<StorageValue, StorageValue> where
	)
		throws InvalidRequestException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException, NotFoundException {
		return new StorageRowIteratorWhere(storageConnectionPool, keySpace, columnFamily, config.getEncoding(), columnNames, where);
	}

	@Override
	public void delete(
		final String keySpace,
		final String columnFamily,
		final StorageValue key
	) throws InvalidRequestException, NotFoundException, UnavailableException,
		TimedOutException, TException, UnsupportedEncodingException, SocketException, StorageConnectionPoolException {

		StorageConnection connection = null;
		try {
			connection = storageConnectionPool.getConnection();
			final Iface client = connection.getClient(keySpace);

			logger.trace("delete keyspace: " + keySpace + " columnFamily: " + columnFamily + " key: " + key);

			final ConsistencyLevel consistency_level = ConsistencyLevel.ONE;

			final ColumnPath column_path = new ColumnPath(columnFamily);
			client.remove(ByteBuffer.wrap(key.getByte()), column_path, getCurrentTimestamp(), consistency_level);
		} finally {
			storageConnectionPool.releaseConnection(connection);
		}
	}

	@Override
	public Map<StorageValue, StorageValue> read(
		final String keySpace,
		final String columnFamily,
		final StorageValue id
	) throws NotFoundException, StorageConnectionPoolException,
		InvalidRequestException, TException, UnavailableException, TimedOutException, UnsupportedEncodingException {
		StorageConnection connection = null;
		try {
			connection = storageConnectionPool.getConnection();
			final Iface client = connection.getClient(keySpace);

			logger.trace("delete keyspace: " + keySpace + " columnFamily: " + columnFamily + " key: " + id);

			final ByteBuffer key = ByteBuffer.wrap(id.getByte());
			final ConsistencyLevel consistency_level = ConsistencyLevel.ONE;

			final int count = 100;
			final Map<StorageValue, StorageValue> result = new HashMap<>();

			final ColumnParent columnParent = new ColumnParent(columnFamily);
			final SlicePredicate predicate = new SlicePredicate();
			final SliceRange slice_range = new SliceRange();
			predicate.setSlice_range(slice_range);
			slice_range.setCount(count);
			slice_range.setStart(new byte[0]);
			slice_range.setFinish(new byte[0]);

			List<ColumnOrSuperColumn> columns = null;
			do {
				if (columns != null) {
					slice_range.setStart(columns.get(columns.size() - 1).getColumn().getName());
				}
				columns = client.get_slice(key, columnParent, predicate, consistency_level);

				for (final ColumnOrSuperColumn column : columns) {
					final StorageValue name = new StorageValue(column.getColumn().getName(), config.getEncoding());
					final StorageValue value = new StorageValue(column.getColumn().getValue(), config.getEncoding());
					result.put(name, value);
				}
			} while (columns.size() == count);

			return result;
		} finally {
			storageConnectionPool.releaseConnection(connection);
		}
	}

	@Override
	public StorageColumnIterator columnIterator(final String keySpace, final String columnFamily, final StorageValue key) throws UnsupportedEncodingException {
		return new StorageColumnIteratorImpl(storageConnectionPool, keySpace, columnFamily, config.getEncoding(), key, false);
	}

	@Override
	public StorageColumnIterator columnIteratorReversed(
		final String keySpace,
		final String columnFamily,
		final StorageValue key
	) throws UnsupportedEncodingException {
		return new StorageColumnIteratorImpl(storageConnectionPool, keySpace, columnFamily, config.getEncoding(), key, true);
	}

	@Override
	public Collection<List<StorageValue>> read(
		final String keySpace,
		final String columnFamily,
		final Collection<StorageValue> keys,
		final List<StorageValue> columnNames
	)
		throws UnsupportedEncodingException, InvalidRequestException, UnavailableException, TimedOutException, TException, StorageConnectionPoolException {
		StorageConnection connection = null;
		try {
			connection = storageConnectionPool.getConnection();
			final Iface client = connection.getClient(keySpace);

			logger.trace("read keyspace: " + keySpace + " columnFamily: " + columnFamily + " keys: " + keys + " columnNames: " + columnNames);

			final ConsistencyLevel consistency_level = ConsistencyLevel.ONE;
			final ColumnParent column_parent = new ColumnParent(columnFamily);

			final SlicePredicate predicate = new SlicePredicate();
			predicate.setColumn_names(buildByteBufferList(columnNames));
			final Map<ByteBuffer, List<ColumnOrSuperColumn>> columnsData = client.multiget_slice(buildByteBufferList(keys), column_parent, predicate, consistency_level);

			final List<List<StorageValue>> results = new ArrayList<>();

			for (final Entry<ByteBuffer, List<ColumnOrSuperColumn>> e : columnsData.entrySet()) {
				final List<ColumnOrSuperColumn> columns = e.getValue();

				final Map<StorageValue, StorageValue> data = new HashMap<>();
				for (final ColumnOrSuperColumn column : columns) {
					final StorageValue name = new StorageValue(column.getColumn().getName(), config.getEncoding());
					final StorageValue value = new StorageValue(column.getColumn().getValue(), config.getEncoding());
					data.put(name, value);
				}

				final List<StorageValue> result = new ArrayList<>();

				for (final StorageValue columnName : columnNames) {
					final StorageValue value = data.get(columnName);
					if (value != null) {
						result.add(value);
					} else {
						result.add(new StorageValue());
					}
				}
				results.add(result);
			}

			return results;
		} finally {
			storageConnectionPool.releaseConnection(connection);
		}
	}
}
