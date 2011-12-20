package de.benjaminborbe.storage.util;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.cassandra.thrift.Cassandra.Client;
import org.apache.cassandra.thrift.Cassandra.Iface;
import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.ColumnOrSuperColumn;
import org.apache.cassandra.thrift.ColumnParent;
import org.apache.cassandra.thrift.ColumnPath;
import org.apache.cassandra.thrift.ConsistencyLevel;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.KeyRange;
import org.apache.cassandra.thrift.KeySlice;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.SlicePredicate;
import org.apache.cassandra.thrift.SliceRange;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;
import org.slf4j.Logger;

import com.google.inject.Inject;

public class StorageDaoUtilImpl implements StorageDaoUtil {

	private static final int LIMIT = 10000;

	private final StorageConfig config;

	private final StorageConnection connection;

	private final Logger logger;

	@Inject
	public StorageDaoUtilImpl(final StorageConnection connection, final StorageConfig config, final Logger logger) {
		this.connection = connection;
		this.config = config;
		this.logger = logger;
	}

	/*
	 * (non-Javadoc)
	 * @see example.DaoUtil#insert(java.lang.String, java.lang.String, java.lang.String,
	 * java.util.Map)
	 */
	@Override
	public void insert(final String keySpace, final String columnFamily, final String id, final Map<String, String> data)
			throws InvalidRequestException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException {
		final Iface client = getClient(keySpace);

		logger.debug("insert keyspace: " + keySpace + " columnfamily: " + columnFamily + " id: " + id + " data: " + data);

		final long timestamp = System.currentTimeMillis();
		for (final Entry<String, String> e : data.entrySet()) {
			final ByteBuffer key = ByteBuffer.wrap(id.getBytes(config.getEncoding()));
			final ColumnParent column_parent = new ColumnParent(columnFamily);
			final Column column = new Column(ByteBuffer.wrap(e.getKey().getBytes(config.getEncoding())), ByteBuffer.wrap(e
					.getValue().getBytes(config.getEncoding())), timestamp);
			final ConsistencyLevel consistency_level = ConsistencyLevel.ONE;

			// schreiben eines datensatzes
			client.insert(key, column_parent, column, consistency_level);
		}
	}

	@Override
	public String read(final String keySpace, final String columnFamily, final String id, final String field)
			throws InvalidRequestException, NotFoundException, UnavailableException, TimedOutException, TException,
			UnsupportedEncodingException {
		final Iface client = getClient(keySpace);

		logger.debug("read keyspace: " + keySpace + " columnfamily: " + columnFamily + " id: " + id + " key: " + field);

		final ByteBuffer key = ByteBuffer.wrap(id.getBytes(config.getEncoding()));
		final ColumnPath column_path = new ColumnPath(columnFamily);
		column_path.setColumn(field.getBytes(config.getEncoding()));
		final ConsistencyLevel consistency_level = ConsistencyLevel.ONE;

		// lesen eines datensatzes
		final ColumnOrSuperColumn column = client.get(key, column_path, consistency_level);

		return new String(column.getColumn().getValue(), config.getEncoding());
	}

	protected Iface getClient(final String keySpace) throws InvalidRequestException, TException {
		final Client client = connection.getClient();
		client.set_keyspace(keySpace);
		return client;
	}

	@Override
	public void delete(final String keySpace, final String columnFamily, final String id, final String field)
			throws InvalidRequestException, NotFoundException, UnavailableException, TimedOutException, TException,
			UnsupportedEncodingException {
		final Iface client = getClient(keySpace);

		logger.debug("delete keyspace: " + keySpace + " columnfamily: " + columnFamily + " id: " + id + " key: " + field);

		final ByteBuffer key = ByteBuffer.wrap(id.getBytes(config.getEncoding()));
		final ColumnPath column_path = new ColumnPath(columnFamily);
		column_path.setColumn(field.getBytes(config.getEncoding()));
		final ConsistencyLevel consistency_level = ConsistencyLevel.ONE;
		final ColumnOrSuperColumn column = client.get(key, column_path, consistency_level);

		// loescht einen datensatz aus
		client.remove(key, column_path, column.getColumn().getTimestamp(), consistency_level);
	}

	@Override
	public List<String> list(final String keySpace, final String columnFamily) throws InvalidRequestException,
			UnavailableException, TimedOutException, TException, UnsupportedEncodingException, NotFoundException {
		final Iface client = getClient(keySpace);
		logger.debug("list keyspace: " + keySpace + " columnfamily: " + columnFamily);

		final List<String> list = new ArrayList<String>();
		final ColumnParent column_parent = new ColumnParent(columnFamily);
		final SlicePredicate predicate = new SlicePredicate();
		predicate.setSlice_range(new SliceRange(ByteBuffer.wrap(new byte[0]), ByteBuffer.wrap(new byte[0]), false, LIMIT));
		final KeyRange keyRange = new KeyRange(LIMIT);
		keyRange.setStart_key(new byte[0]);
		keyRange.setEnd_key(new byte[0]);
		final ConsistencyLevel consistency_level = ConsistencyLevel.ONE;
		final List<KeySlice> keySlices = client.get_range_slices(column_parent, predicate, keyRange, consistency_level);
		for (final KeySlice keySlice : keySlices) {
			if (keySlice.getColumnsSize() > 0) {
				list.add(new String(keySlice.getKey(), config.getEncoding()));
			}
		}
		logger.debug("found " + list.size() + " elements in keyspace: " + keySpace + " columnfamily: " + columnFamily);
		return list;
	}
}
