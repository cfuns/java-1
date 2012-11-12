package de.benjaminborbe.storage.util;

import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.cassandra.thrift.Cassandra.Iface;
import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.ColumnOrSuperColumn;
import org.apache.cassandra.thrift.ColumnParent;
import org.apache.cassandra.thrift.ColumnPath;
import org.apache.cassandra.thrift.ConsistencyLevel;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;
import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageIterator;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.map.MapChain;

public class StorageDaoUtilImpl implements StorageDaoUtil {

	private final CalendarUtil calendarUtil;

	private final StorageConfig config;

	private final Logger logger;

	private final StorageConnectionPool storageConnectionPool;

	@Inject
	public StorageDaoUtilImpl(final StorageConnectionPool storageConnectionPool, final StorageConfig config, final Logger logger, final CalendarUtil calendarUtil) {
		this.storageConnectionPool = storageConnectionPool;
		this.config = config;
		this.logger = logger;
		this.calendarUtil = calendarUtil;
	}

	@Override
	public int count(final String keySpace, final String columnFamily) throws UnsupportedEncodingException, InvalidRequestException, UnavailableException, TimedOutException,
			TException, NotFoundException, StorageException {
		int result = 0;
		final StorageIterator i = keyIterator(keySpace, columnFamily);
		while (i.hasNext()) {
			i.nextByte();
			result++;
		}
		return result;
	}

	@Override
	public int count(final String keySpace, final String columnFamily, final String field) throws UnsupportedEncodingException, InvalidRequestException, UnavailableException,
			TimedOutException, TException, NotFoundException, StorageException, SocketException, StorageConnectionPoolException {
		int result = 0;
		final StorageIterator i = keyIterator(keySpace, columnFamily);
		while (i.hasNext()) {
			final byte[] key = i.nextByte();
			final String value = read(keySpace, columnFamily, key, field);
			if (value != null && value.length() > 0) {
				result++;
			}
		}
		return result;
	}

	@Override
	public void delete(final String keySpace, final String columnFamily, final byte[] id, final List<String> columnNames) throws InvalidRequestException, NotFoundException,
			UnavailableException, TimedOutException, TException, UnsupportedEncodingException, SocketException, StorageConnectionPoolException {
		StorageConnection connection = null;
		try {
			connection = storageConnectionPool.getConnection();
			final Iface client = connection.getClient(keySpace);

			logger.trace("delete keyspace: " + keySpace + " columnFamily: " + columnFamily + " key: " + id + " columnNames: " + columnNames);

			final ByteBuffer key = ByteBuffer.wrap(id);
			final ConsistencyLevel consistency_level = ConsistencyLevel.ONE;

			for (final String columnName : columnNames) {
				final ColumnPath column_path = new ColumnPath(columnFamily);
				column_path.setColumn(columnName.getBytes(config.getEncoding()));
				final ColumnOrSuperColumn column = client.get(key, column_path, consistency_level);
				client.remove(key, column_path, column.getColumn().getTimestamp(), consistency_level);
			}
		}
		finally {
			storageConnectionPool.releaseConnection(connection);
		}
	}

	@Override
	public void delete(final String keySpace, final String columnFamily, final byte[] key, final String columnName) throws InvalidRequestException, NotFoundException,
			UnavailableException, TimedOutException, TException, UnsupportedEncodingException, SocketException, StorageConnectionPoolException {
		delete(keySpace, columnFamily, key, Arrays.asList(columnName));
	}

	@Override
	public void delete(final String keySpace, final String columnFamily, final String id, final List<String> columnNames) throws InvalidRequestException, NotFoundException,
			UnavailableException, TimedOutException, TException, UnsupportedEncodingException, SocketException, StorageConnectionPoolException {
		delete(keySpace, columnFamily, id.getBytes(config.getEncoding()), columnNames);
	}

	@Override
	public void delete(final String keySpace, final String columnFamily, final String key, final String columnName) throws InvalidRequestException, NotFoundException,
			UnavailableException, TimedOutException, TException, UnsupportedEncodingException, SocketException, StorageConnectionPoolException {
		delete(keySpace, columnFamily, key, Arrays.asList(columnName));
	}

	@Override
	public void insert(final String keySpace, final String columnFamily, final byte[] id, final String columnName, final String columnValue) throws InvalidRequestException,
			UnavailableException, TimedOutException, TException, UnsupportedEncodingException, NotFoundException, SocketException, StorageConnectionPoolException {
		insert(keySpace, columnFamily, id, new MapChain<String, String>().add(columnName, columnValue));
	}

	@Override
	public void insert(final String keySpace, final String columnFamily, final String id, final String columnName, final String columnValue) throws InvalidRequestException,
			UnavailableException, TimedOutException, TException, UnsupportedEncodingException, NotFoundException, SocketException, StorageConnectionPoolException {
		insert(keySpace, columnFamily, id, new MapChain<String, String>().add(columnName, columnValue));
	}

	@Override
	public void insert(final String keySpace, final String columnFamily, final byte[] id, final Map<String, String> data) throws InvalidRequestException, UnavailableException,
			TimedOutException, TException, UnsupportedEncodingException, NotFoundException, SocketException, StorageConnectionPoolException {

		StorageConnection connection = null;
		try {
			connection = storageConnectionPool.getConnection();
			final Iface client = connection.getClient(keySpace);

			logger.trace("insert keyspace: " + keySpace + " columnFamily: " + columnFamily + " id: " + id + " data: " + data);

			final long timestamp = calendarUtil.getTime();
			for (final Entry<String, String> e : data.entrySet()) {
				final String key = e.getKey();
				final String value = e.getValue();
				logger.debug("write " + key + " = " + value);
				if (value != null) {

					final ColumnParent column_parent = new ColumnParent(columnFamily);
					final String encoding = config.getEncoding();
					logger.trace("storage " + encoding);

					final Column column = new Column(ByteBuffer.wrap(key.getBytes(encoding)));
					column.setValue(ByteBuffer.wrap(value.getBytes(encoding)));
					column.setTimestamp(timestamp);
					final ConsistencyLevel consistency_level = ConsistencyLevel.ONE;

					// schreiben eines datensatzes
					client.insert(ByteBuffer.wrap(id), column_parent, column, consistency_level);
				}
				else {
					try {
						delete(keySpace, columnFamily, id, Arrays.asList(e.getKey()));
					}
					catch (final NotFoundException e1) {
						// do nothing
					}
				}
			}
		}
		finally {
			storageConnectionPool.releaseConnection(connection);
		}
	}

	@Override
	public void insert(final String keySpace, final String columnFamily, final String id, final Map<String, String> data) throws InvalidRequestException, UnavailableException,
			TimedOutException, TException, UnsupportedEncodingException, NotFoundException, SocketException, StorageConnectionPoolException {
		insert(keySpace, columnFamily, id.getBytes(config.getEncoding()), data);
	}

	@Override
	public StorageIterator keyIterator(final String keySpace, final String columnFamily) throws InvalidRequestException, UnavailableException, TimedOutException, TException,
			UnsupportedEncodingException, NotFoundException {
		final ColumnParent column_parent = new ColumnParent(columnFamily);
		return new StorageKeyIterator(storageConnectionPool, keySpace, column_parent, config.getEncoding());
	}

	@Override
	public StorageIterator keyIterator(final String keySpace, final String columnFamily, final Map<String, String> where) throws InvalidRequestException, UnavailableException,
			TimedOutException, TException, UnsupportedEncodingException, NotFoundException, StorageConnectionPoolException {

		final ColumnParent column_parent = new ColumnParent(columnFamily);
		return new StorageKeyWhereIterator(storageConnectionPool, keySpace, column_parent, config.getEncoding(), where);

	}

	@Override
	public List<String> read(final String keySpace, final String columnFamily, final byte[] id, final List<String> columnNames) throws InvalidRequestException, NotFoundException,
			UnavailableException, TimedOutException, TException, UnsupportedEncodingException, SocketException, StorageConnectionPoolException {

		final List<String> result = new ArrayList<String>();
		for (final String columnName : columnNames) {
			result.add(read(keySpace, columnFamily, id, columnName));
		}
		return result;
	}

	@Override
	public String read(final String keySpace, final String columnFamily, final byte[] id, final String columnName) throws InvalidRequestException, NotFoundException,
			UnavailableException, TimedOutException, TException, UnsupportedEncodingException, SocketException, StorageConnectionPoolException {

		if (id == null || id.length == 0) {
			logger.info("can't read with null id");
			return null;
		}

		StorageConnection connection = null;
		try {
			connection = storageConnectionPool.getConnection();
			final Iface client = connection.getClient(keySpace);

			logger.trace("read keyspace: " + keySpace + " columnFamily: " + columnFamily + " id: " + id + " columnName: " + columnName);
			final ByteBuffer key = ByteBuffer.wrap(id);
			final ColumnPath column_path = new ColumnPath(columnFamily);
			column_path.setColumn(columnName.getBytes(config.getEncoding()));
			final ConsistencyLevel consistency_level = ConsistencyLevel.ONE;
			try {
				final ColumnOrSuperColumn column = client.get(key, column_path, consistency_level);
				return new String(column.getColumn().getValue(), config.getEncoding());
			}
			catch (final NotFoundException e) {
				return null;
			}
		}
		finally {
			storageConnectionPool.releaseConnection(connection);
		}
	}

	@Override
	public List<String> read(final String keySpace, final String columnFamily, final String key, final List<String> columnNames) throws InvalidRequestException, NotFoundException,
			UnavailableException, TimedOutException, TException, UnsupportedEncodingException, SocketException, StorageConnectionPoolException {
		return read(keySpace, columnFamily, key.getBytes(config.getEncoding()), columnNames);
	}

	@Override
	public String read(final String keySpace, final String columnFamily, final String id, final String columnName) throws InvalidRequestException, NotFoundException,
			UnavailableException, TimedOutException, TException, UnsupportedEncodingException, SocketException, StorageConnectionPoolException {
		return read(keySpace, columnFamily, id.getBytes(config.getEncoding()), columnName);
	}

}
